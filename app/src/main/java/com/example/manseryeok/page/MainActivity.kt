package com.example.manseryeok.page

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.manseryeok.R
import com.example.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.example.manseryeok.databinding.ActivityMainBinding
import com.example.manseryeok.page.calendarname.CalendarInputActivity
import com.example.manseryeok.page.compass.CompassActivity
import com.example.manseryeok.page.user.UserDBActivity
import com.example.manseryeok.utils.Extras
import com.example.manseryeok.utils.Utils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

        // getHashKey()
    }

    private fun setDayLuck() {
        val today = Calendar.getInstance()

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
                clCompass.id -> startActivity(Intent(this@MainActivity, CompassActivity::class.java))
                clQuestion.id -> startActivity(Intent(this@MainActivity, InquiryActivity::class.java))
                clDatabase.id -> startActivity(Intent(this@MainActivity, UserDBActivity::class.java))
                clMedia.id -> Toast.makeText(applicationContext, "곧 오픈 예정입니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}