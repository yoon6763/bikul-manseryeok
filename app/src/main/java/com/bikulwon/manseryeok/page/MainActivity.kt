package com.bikulwon.manseryeok.page

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.bikulwon.manseryeok.R
import com.bikulwon.manseryeok.adapter.ImageSliderAdapter
import com.bikulwon.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.bikulwon.manseryeok.databinding.ActivityMainBinding
import com.bikulwon.manseryeok.manseryeokdb.Season24SQLHelper
import com.bikulwon.manseryeok.models.notion.AdvertiseSliderModel
import com.bikulwon.manseryeok.models.notion.request.AdvertiseRequestDTO
import com.bikulwon.manseryeok.models.notion.request.Filter
import com.bikulwon.manseryeok.page.calendarname.CalendarInputActivity
import com.bikulwon.manseryeok.page.compass.CompassActivity
import com.bikulwon.manseryeok.page.terms.BusinessInfoDialogFragment
import com.bikulwon.manseryeok.page.terms.TermsDialogFragment
import com.bikulwon.manseryeok.page.user.UserDBActivity
import com.bikulwon.manseryeok.service.BusinessInfoService
import com.bikulwon.manseryeok.service.NotionAPI
import com.bikulwon.manseryeok.utils.Extras
import com.bikulwon.manseryeok.utils.SecretConstants
import com.bikulwon.manseryeok.utils.SharedPreferenceHelper
import com.bikulwon.manseryeok.utils.Utils
import com.google.firebase.FirebaseApp
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "MainActivityDev"
    private lateinit var sliderAdapter: ImageSliderAdapter
    private val notionAPI = NotionAPI.create()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var sliderJob: Job? = null
    private val businessInfoService by lazy { BusinessInfoService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true

        window.statusBarColor = getColor(R.color.navy)

        binding.run {
            clCalendar.setOnClickListener(this@MainActivity)
            clCompass.setOnClickListener(this@MainActivity)
            clDatabase.setOnClickListener(this@MainActivity)
            clMedia.setOnClickListener(this@MainActivity)
            clQuestion.setOnClickListener(this@MainActivity)
        }

        setStatusBar()
        setDayLuck()
        setGridDisplaySize()
        setBottomLayout()

        if (!businessInfoService.isInitialized()) {
            businessInfoService.fetchData()
        }

        CoroutineScope(Dispatchers.Main).launch {
            setAdvertise()
            startSlider()
        }

        openTermsAgreeDialog()

        // getHashKey()
    }

    private fun setBottomLayout() = with(binding) {
        tvTerms.setOnClickListener {
            val termsDialogFragment = TermsDialogFragment.newInstance()
            termsDialogFragment.show(supportFragmentManager, "termsDialog")

            termsDialogFragment.onDialogAgreeListener =
                object : TermsDialogFragment.OnDialogAgreeListener {
                    override fun onDialogAgree() {
                        termsDialogFragment.dismiss()
                    }
                }
        }

        tvBusinessInfo.setOnClickListener {
            if (!businessInfoService.isInitialized()) {
                return@setOnClickListener
            }
            val businessInfoDialogFragment = BusinessInfoDialogFragment.newInstance(businessInfoService.content)
            businessInfoDialogFragment.show(supportFragmentManager, "businessInfoDialog")
        }

    }

    private fun openTermsAgreeDialog() {
        if (SharedPreferenceHelper.isTermsAgree(applicationContext)) {
            return
        }

        val termsDialogFragment = TermsDialogFragment.newInstance()
        termsDialogFragment.show(supportFragmentManager, "termsDialog")

        termsDialogFragment.isCancelable = false
        termsDialogFragment.onDialogAgreeListener =
            object : TermsDialogFragment.OnDialogAgreeListener {
                override fun onDialogAgree() {
                    termsDialogFragment.dismiss()
                    SharedPreferenceHelper.setTermsAgree(applicationContext, true)
                }
            }

    }

    private fun startSlider() {
        sliderJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(5000)
                binding.vpAd.currentItem = (binding.vpAd.currentItem + 1) % sliderAdapter.itemCount
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sliderJob?.isActive == false) {
            startSlider()
        }
    }

    override fun onPause() {
        super.onPause()
        sliderJob?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        sliderJob?.cancel()
    }

    private suspend fun setAdvertise() {
        try {
            val advertiseRequestBody = AdvertiseRequestDTO(
                filter = Filter(
                    "Status",
                    com.bikulwon.manseryeok.models.notion.request.Status(
                        "게시 중"
                    )
                )
            )

            val advertiseCall = notionAPI.getAdvertiseInfo(
                notionVersion = NotionAPI.NOTION_API_VERSION,
                token = SecretConstants.NOTION_TOKEN,
                databaseId = SecretConstants.NOTION_ADVERTISE_DB_ID,
                advertiseRequestDTO = advertiseRequestBody
            ).awaitResponse()

            val imageSliderUrls = ArrayList<AdvertiseSliderModel>()

            if (advertiseCall.isSuccessful) {
                val advertiseResponseBody = advertiseCall.body()!!

                advertiseResponseBody.results.forEach { result ->
                    result.properties.Image.files.forEach { file ->
                        imageSliderUrls.add(
                            AdvertiseSliderModel(result.properties.Link.url, file.file.url)
                        )
                    }
                }
            }

            sliderAdapter = ImageSliderAdapter(this@MainActivity, imageSliderUrls)
            binding.vpAd.adapter = sliderAdapter
        } catch (e: Exception) {
            Log.e(TAG, "setAdvertise: ${e.message}")
        }
    }

    private fun setGridDisplaySize() {
        val displayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels.toDouble() * 0.7 / 3

        binding.glButtonSet.forEach { view ->
            if (view.id == binding.llToday.id) {
                view.layoutParams.width = dpWidth.toInt() * 2
                view.layoutParams.height = dpWidth.toInt() * 2
            } else {
                view.layoutParams.width = dpWidth.toInt()
                view.layoutParams.height = dpWidth.toInt()
            }
        }

//        binding.cvAd.layoutParams.width = displayMetrics.widthPixels * 7
    }


    private fun setDayLuck() {
        val today = Calendar.getInstance()
        val mSeasonHelper = Season24SQLHelper(applicationContext)
        mSeasonHelper.createDataBase()
        mSeasonHelper.open()
        mSeasonHelper.close()

        val mDBHelper = ManseryeokSQLHelper(applicationContext)
        mDBHelper.createDataBase()
        mDBHelper.open()

        // 유저의 생일 - 1년 부터 + 100년까지의 정보
        val db = mDBHelper.getTableData(today[Calendar.YEAR], today[Calendar.YEAR])!!

        mDBHelper.close()

        val todayDB = db.find {
            it.cd_sy == today[Calendar.YEAR] &&
                    it.cd_sm == today[Calendar.MONTH] + 1 &&
                    it.cd_sd == today[Calendar.DAY_OF_MONTH]
        }
        // hyganji, hmganji, hdganji,
        val yearGanji = todayDB!!.cd_hyganjee!!
        val monthGanji = todayDB!!.cd_hmganjee!!
        val dayGanji = todayDB!!.cd_hdganjee!!

        val timeGanji = Utils.getTimeGanji(dayGanji[0], today[Calendar.HOUR_OF_DAY])

        binding.tvToday.text =
            "${timeGanji[0]}${dayGanji[0]}${monthGanji[0]}${yearGanji[0]}\n${timeGanji[1]}${dayGanji[1]}${monthGanji[1]}${yearGanji[1]}"
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                0,
                APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    }

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }

    override fun onClick(p0: View?) {
        binding.run {
            when (p0?.id) {
                clCalendar.id -> {
                    val intent = Intent(this@MainActivity, CalendarInputActivity::class.java)
                    intent.putExtra(Extras.INTENT_EXTRAS_INFO_TYPE, Utils.InfoType.CREATE.value)
                    startActivity(intent)
                }

                clCompass.id -> startActivity(
                    Intent(
                        this@MainActivity,
                        CompassActivity::class.java
                    )
                )

                clQuestion.id -> startActivity(
                    Intent(
                        this@MainActivity,
                        InquiryActivity::class.java
                    )
                )

                clDatabase.id -> startActivity(
                    Intent(
                        this@MainActivity,
                        UserDBActivity::class.java
                    )
                )

                clMedia.id -> Toast.makeText(applicationContext, "곧 오픈 예정입니다", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun blurBitmap(context: Context, bitmap: Bitmap, blurRadius: Float): Bitmap {
        val rs = RenderScript.create(context)
        val input = Allocation.createFromBitmap(rs, bitmap)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(blurRadius)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(bitmap)
        return bitmap
    }
}