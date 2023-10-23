package com.example.manseryeok.page

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.manseryeok.R
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.adapter.LocationAdapter
import com.example.manseryeok.databinding.ActivityCalendarInputBinding
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.Gender
import com.example.manseryeok.models.UserInputViewModel
import com.example.manseryeok.models.user.User
import com.example.manseryeok.utils.ParentActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar


class CalendarInputActivity : ParentActivity() {

    enum class NextPageType { NAME, CALENDAR }

    private lateinit var userInputViewModel: UserInputViewModel
    private lateinit var fragment: LocationPickerFragment
    private lateinit var dataBinding: ActivityCalendarInputBinding

    private val binding by lazy { ActivityCalendarInputBinding.inflate(layoutInflater) }
    private var birth = Calendar.getInstance()
    private var birthPlace = "대한민국"
    private var timeDiff = -30
    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }

    private val locationClickListener = object : LocationAdapter.OnLocationClickListener {
        override fun onLocationClick(location: String, argsTimeDiff: Int) {
            birthPlace = location
            timeDiff = argsTimeDiff
            fragment.dismiss()
            binding.etInputBirthPlace.setText("$location (${argsTimeDiff}분)")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_input)
        userInputViewModel = ViewModelProvider(this)[UserInputViewModel::class.java]
        dataBinding.viewModel = userInputViewModel
        dataBinding.lifecycleOwner = this

        toolbarSetting()

        dataBinding.run {
            etInputBirthPlace.setOnClickListener { openBirthPlacePicker() }
            btnNameInputFinish.setOnClickListener { nextPage(NextPageType.NAME) }
            btnCalenderInputFinish.setOnClickListener {
                if (birth[Calendar.YEAR] >= 2101) {
                    showShortToast("최대 2100년까지의 만세력 정보만 제공합니다")
                    return@setOnClickListener
                }
                nextPage(NextPageType.CALENDAR)
            }
            btnEditFinish.setOnClickListener {

            }
        }

        setMode()
    }

    private fun setMode() {
        val inputType = intent.getStringExtra(Utils.INTENT_EXTRAS_INFO_TYPE)
        when (inputType) {
            Utils.InfoType.CREATE.value -> binding.containerEdit.visibility = View.GONE
            Utils.InfoType.EDIT.value -> editSetting()
        }
    }

    private fun editSetting() {
        val userId = intent.getLongExtra(Utils.INTENT_EXTRAS_USER_ID, -1)
        var user: User? = null

        runBlocking {
            launch(IO) {
                user = userDao.getUser(userId)
            }
        }

        if (user == null) {
            showShortToast("유저 정보를 불러오는데 실패했습니다")
            finish()
        }

        binding.run {
            etFirstName.setText(user?.firstName)
            etName.setText(user?.lastName)
            etInputBirth.setText(Utils.dateSlideFormat.format(user?.getBirthCalculated()?.timeInMillis))

            if (user?.includeTime!!) {
                etInputBirthTime.setText(Utils.timeFormat.format(user?.getBirthCalculated()?.timeInMillis))
            }
            birth = user!!.getBirthOrigin()

        }
    }

    private fun toolbarSetting() {
        setSupportActionBar(binding.toolbarCalendarInput)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun openBirthPlacePicker() {
        fragment = LocationPickerFragment()
        fragment.onLocationClickListener = locationClickListener
        fragment.show(supportFragmentManager, "LocationPicker")
    }

    private fun nextPage(type: NextPageType) = with(binding) {
        Toast.makeText(applicationContext, userInputViewModel.firstName.value, Toast.LENGTH_SHORT).show()

        return@with

//        if (etFirstName.text.toString().isEmpty()) {
//            showShortToast("성을 입력해주세요")
//            return
//        }
//        if (etName.text.toString().isEmpty()) {
//            showShortToast("이름을 입력해주세요")
//            return
//        }
//
//        if (rgCalType.checkedRadioButtonId == rbCalTypeMoon.id || rgCalType.checkedRadioButtonId == rbCalTypeMoonLeap.id) {
//            val strTime = Utils.dateNumFormat.format(birth.timeInMillis).substring(0, 8)
//            val moonBirth = Utils.convertLunarToSolar(strTime)
//            birth = Calendar.getInstance().apply {
//                timeInMillis = moonBirth
//                this[Calendar.HOUR_OF_DAY] = birth[Calendar.HOUR_OF_DAY]
//                this[Calendar.MINUTE] = birth[Calendar.MINUTE]
//            }
//        }
//
//        val userModel = User(
//            id = 0,
//            firstName = etFirstName.text.toString(), // 성
//            lastName = etName.text.toString(), // 이름
//            gender = if (rgGender.checkedRadioButtonId == rbGenderMale.id) Gender.MALE.value else Gender.FEMALE.value, // 성별
//            birthYear = birth[Calendar.YEAR],
//            birthMonth = birth[Calendar.MONTH] + 1,
//            birthDay = birth[Calendar.DAY_OF_MONTH],
//            includeTime = !cbInputBirthTime.isChecked,
//            birthHour = if (cbInputBirthTime.isChecked) -1 else birth[Calendar.HOUR_OF_DAY],
//            birthMinute = if (cbInputBirthTime.isChecked) -1 else birth[Calendar.MINUTE],
//            birthPlace = etInputBirthPlace.text.toString(), // 출생지
//            timeDiff = timeDiff, // 시차
//            useSummerTime = if (cbUseSummerTime.isChecked) 1 else 0, // 서머타임 사용
//            useTokyoTime = if (cbUseTokyo.isChecked) 1 else 0, // 도쿄시간 사용
//            memo = "",
//        )
//
//
//        runBlocking {
//            launch(IO) {
//                val savedUserId = userDao.insert(userModel)
//                userModel.id = savedUserId
//            }
//        }
//
//        //showProgress(this@CalendarInputActivity,"잠시만 기다려주세요")
//        val intent = when (type) {
//            NextPageType.NAME -> Intent(this@CalendarInputActivity, NameActivity::class.java)
//            NextPageType.CALENDAR -> Intent(
//                this@CalendarInputActivity,
//                CalendarActivity::class.java
//            )
//        }
//
//        intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, userModel.id)
//        startActivity(intent)
//        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}