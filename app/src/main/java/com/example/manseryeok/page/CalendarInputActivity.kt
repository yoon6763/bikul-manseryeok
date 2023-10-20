package com.example.manseryeok.page

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.adapter.LocationAdapter
import com.example.manseryeok.databinding.ActivityCalendarInputBinding
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.Gender
import com.example.manseryeok.models.user.User
import com.example.manseryeok.utils.ParentActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar


class CalendarInputActivity : ParentActivity() {

    enum class NextPageType { NAME, CALENDAR }

    private val TAG = "CalendarInputActivity"
    private val binding by lazy { ActivityCalendarInputBinding.inflate(layoutInflater) }
    private var birth = Calendar.getInstance()
    private var birthPlace = "대한민국"
    private var timeDiff = -30
    private lateinit var fragment: LocationPickerFragment
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
        setContentView(binding.root)

        toolbarSetting()

        binding.run {
            etInputBirth.setOnClickListener { openBirthPicker() }
            etInputBirthTime.setOnClickListener { openBirthTimePicker() }
            etInputBirthPlace.setOnClickListener { openBirthPlacePicker() }
            cbInputBirthTime.setOnCheckedChangeListener { _, b -> etInputBirthTime.isEnabled = !b }
            btnNameInputFinish.setOnClickListener { nextPage(NextPageType.NAME) }
            btnCalenderInputFinish.setOnClickListener {
                if (birth[Calendar.YEAR] >= 2101) {
                    showShortToast("최대 2100년까지의 만세력 정보만 제공합니다")
                    return@setOnClickListener
                }
                nextPage(NextPageType.CALENDAR)
            }
            btnEditFinish.setOnClickListener {
                // TODO: 수정 완료 버튼 클릭 시
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

    private fun openBirthPicker() {
        val datePicker = DatePickerDialog(
            this@CalendarInputActivity,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                birth[Calendar.YEAR] = y
                birth[Calendar.MONTH] = m
                birth[Calendar.DAY_OF_MONTH] = d
                binding.etInputBirth.setText(Utils.dateSlideFormat.format(birth.timeInMillis))
            }, birth[Calendar.YEAR], birth[Calendar.MONTH] + 1, birth[Calendar.DAY_OF_MONTH]
        )

        datePicker.datePicker.calendarViewShown = false
        datePicker.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePicker.show()
    }

    private fun openBirthTimePicker() {
        val timePicker = TimePickerDialog(
            this@CalendarInputActivity,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                birth[Calendar.HOUR_OF_DAY] = h
                birth[Calendar.MINUTE] = m
                binding.etInputBirthTime.setText(Utils.timeFormat.format(birth.timeInMillis))
            }, birth[Calendar.HOUR_OF_DAY], birth[Calendar.MINUTE], false
        )
        timePicker.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        timePicker.show()
    }

    private fun openBirthPlacePicker() {
        fragment = LocationPickerFragment()
        fragment.onLocationClickListener = locationClickListener
        fragment.show(supportFragmentManager, "LocationPicker")
    }

    private fun nextPage(type: NextPageType) = with(binding) {
        if (etFirstName.text.toString().isEmpty()) {
            showShortToast("성을 입력해주세요")
            return
        }
        if (etName.text.toString().isEmpty()) {
            showShortToast("이름을 입력해주세요")
            return
        }

        if (rgCalType.checkedRadioButtonId == rbCalTypeMoon.id || rgCalType.checkedRadioButtonId == rbCalTypeMoonLeap.id) {
            val strTime = Utils.dateNumFormat.format(birth.timeInMillis).substring(0, 8)
            val moonBirth = Utils.convertLunarToSolar(strTime)
            birth = Calendar.getInstance().apply {
                timeInMillis = moonBirth
                this[Calendar.HOUR_OF_DAY] = birth[Calendar.HOUR_OF_DAY]
                this[Calendar.MINUTE] = birth[Calendar.MINUTE]
            }
        }

        val userModel = User(
            id = 0,
            firstName = etFirstName.text.toString(), // 성
            lastName = etName.text.toString(), // 이름
            gender = if (rgGender.checkedRadioButtonId == rbGenderMale.id) Gender.MALE.value else Gender.FEMALE.value, // 성별
            birthYear = birth[Calendar.YEAR],
            birthMonth = birth[Calendar.MONTH] + 1,
            birthDay = birth[Calendar.DAY_OF_MONTH],
            includeTime = !cbInputBirthTime.isChecked,
            birthHour = if (cbInputBirthTime.isChecked) -1 else birth[Calendar.HOUR_OF_DAY],
            birthMinute = if (cbInputBirthTime.isChecked) -1 else birth[Calendar.MINUTE],
            birthPlace = etInputBirthPlace.text.toString(), // 출생지
            timeDiff = timeDiff, // 시차
            useSummerTime = if (cbUseSummerTime.isChecked) 1 else 0, // 서머타임 사용
            useTokyoTime = if (cbUseTokyo.isChecked) 1 else 0, // 도쿄시간 사용
            memo = "",
        )


        runBlocking {
            launch(IO) {
                val savedUserId = userDao.insert(userModel)
                userModel.id = savedUserId
            }
        }

        //showProgress(this@CalendarInputActivity,"잠시만 기다려주세요")
        val intent = when (type) {
            NextPageType.NAME -> Intent(this@CalendarInputActivity, NameActivity::class.java)
            NextPageType.CALENDAR -> Intent(
                this@CalendarInputActivity,
                CalendarActivity::class.java
            )
        }

        intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, userModel.id)
        startActivity(intent)
        finish()
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