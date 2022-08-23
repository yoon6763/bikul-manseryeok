package com.example.manseryeok.page

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.manseryeok.Utils.Utils
import com.example.manseryeok.databinding.ActivityCalendarInputBinding
import com.example.manseryeok.models.User

class CalendarInputActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCalendarInputBinding.inflate(layoutInflater) }
    private val birth = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCalendarInput)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        binding.run {
            etInputBirth.setOnClickListener {
                openBirthPicker()
            }

            etInputBirthTime.setOnClickListener {
                openBirthTimePicker()
            }

            cbInputBirthTime.setOnCheckedChangeListener { compoundButton, b ->
                etInputBirthTime.isEnabled = !b
            }

            btnCalenderInputFinish.setOnClickListener {
                if(birth[Calendar.YEAR] >= 2101) {
                    Toast.makeText(applicationContext, "최대 2100년까지의 만세력 정보만 제공합니다",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                nextPage()
            }
        }
    }

    private fun openBirthPicker() {
        val datePicker = DatePickerDialog(this@CalendarInputActivity,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                birth[Calendar.YEAR] = y
                birth[Calendar.MONTH] = m
                birth[Calendar.DAY_OF_MONTH] = d
                binding.etInputBirth.setText(Utils.dateFormat.format(birth.timeInMillis))
        }, birth[Calendar.YEAR],birth[Calendar.MONTH] + 1,birth[Calendar.DAY_OF_MONTH])

        datePicker.datePicker.calendarViewShown = false
        datePicker.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePicker.show()
    }

    private fun openBirthTimePicker() {
        val timePicker = TimePickerDialog(this@CalendarInputActivity,
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

    private fun nextPage() {
        binding.run {
//            var firstName: String?,
//            var lastName: String?,
//            var gender: Int, // 0 - 남자, 1 - 여자
//            var birth: String?, // yyyy-MM-dd
//            var birthTimeHour: Int?,
//            var birthTimeMin: Int?,
//            var birthPlace: String?
            birth.add(Calendar.MINUTE, -30)

            val date = if(rgCalType.checkedRadioButtonId == rbCalTypeMoon.id) {
                Utils.convertLunarToSolar(Utils.dateTimeFormat.format(birth.timeInMillis))
            } else {
                Utils.dateTimeFormat.format(birth.timeInMillis)
            }


            val userModel = User(
                etFirstName.text.toString(),
                etName.text.toString(),
                if(rgGender.checkedRadioButtonId == rbGenderMale.id) 0 else 1,
                !cbInputBirthTime.isChecked,
                date,
                etInputBirthPlace.text.toString()
            )


            val intent = Intent(this@CalendarInputActivity, CalendarActivity::class.java)
            intent.putExtra(Utils.INTENT_EXTRAS_USER,userModel)
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