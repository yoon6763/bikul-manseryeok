package com.example.manseryeok.page

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.adapter.LocationAdapter
import com.example.manseryeok.databinding.ActivityCalendarInputBinding
import com.example.manseryeok.models.User
import com.example.manseryeok.utils.ParentActivity


class CalendarInputActivity : ParentActivity() {

    private val TAG = "CalendarInputActivity"
    private val binding by lazy { ActivityCalendarInputBinding.inflate(layoutInflater) }
    private val birth = Calendar.getInstance()
    private var birthPlace = "대한민국"
    private var timeDiff = -30
    private lateinit var fragment: LocationPickerFragment

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

        setSupportActionBar(binding.toolbarCalendarInput)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }


        binding.run {
            etInputBirth.setOnClickListener { openBirthPicker() }

            etInputBirthTime.setOnClickListener { openBirthTimePicker() }

            etInputBirthPlace.setOnClickListener { openBirthPlacePicker() }

            cbInputBirthTime.setOnCheckedChangeListener { compoundButton, b ->
                etInputBirthTime.isEnabled = !b
            }

            btnNameInputFinish.setOnClickListener { nextPage(true) }

            btnCalenderInputFinish.setOnClickListener {
                if (birth[Calendar.YEAR] >= 2101) {
                    showShortToast("최대 2100년까지의 만세력 정보만 제공합니다")
                    return@setOnClickListener
                }
                nextPage(false)
            }
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

    private fun nextPage(isName: Boolean) {
        binding.run {
//            firstName: String?,
//            lastName: String?,
//            gender: Int, // 0 - 남자, 1 - 여자
//            birth: String?, // yyyyMMddHHmm or yyyyMMdd
//            birthPlace: String?,
//            timeDiff: Int

            if(isName) {
                if (etFirstName.text.toString().isEmpty()) {
                    showShortToast("성을 입력해주세요")
                    return
                }
                if (etName.text.toString().isEmpty()) {
                    showShortToast("이름을 입력해주세요")
                    return
                }
            }

            val date = if (rgCalType.checkedRadioButtonId == rbCalTypeSun.id) {
                // 양력
                Utils.dateTimeNumFormat.format(birth.timeInMillis)
            } else {
                // 음력
                val strTime = Utils.dateNumFormat.format(birth.timeInMillis).substring(0, 8)
                val moonBirth = Utils.convertLunarToSolar(strTime)
                val tempCal = Calendar.getInstance().apply {
                    timeInMillis = moonBirth
                    this[Calendar.HOUR_OF_DAY] = birth[Calendar.HOUR_OF_DAY]
                    this[Calendar.MINUTE] = birth[Calendar.MINUTE]
                }
                Utils.dateTimeNumFormat.format(tempCal.timeInMillis)
            }

            val userModel = User(
                etFirstName.text.toString(), // 성
                etName.text.toString(), // 이름
                if (rgGender.checkedRadioButtonId == rbGenderMale.id) 0 else 1, // 성별
                if (cbInputBirthTime.isChecked) date.substring(0, 8) else date, // 생일, 시간포함 - yyyyMMddHHmm, 미포함 - yyyyMMdd
                etInputBirthPlace.text.toString(), // 출생지
                timeDiff
            )

            //showProgress(this@CalendarInputActivity,"잠시만 기다려주세요")
            val intent = if (isName) Intent(this@CalendarInputActivity, NameActivity::class.java)
            else Intent(this@CalendarInputActivity, CalendarActivity::class.java)
            intent.putExtra(Utils.INTENT_EXTRAS_USER, userModel)
            startActivity(intent)
            finish()
        }
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