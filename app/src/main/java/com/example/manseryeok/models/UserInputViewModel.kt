package com.example.manseryeok.models

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.PrimaryKey
import com.example.manseryeok.adapter.LocationAdapter
import com.example.manseryeok.models.user.User
import com.example.manseryeok.page.LocationPickerFragment
import com.example.manseryeok.utils.Utils
import java.util.Calendar

class UserInputViewModel : ViewModel() {
    var firstName = MutableLiveData<String>("")
    var lastName = MutableLiveData<String>("")
    var gender = MutableLiveData<Int>(0)

    var year = MutableLiveData<Int>(-1)
    var month = MutableLiveData<Int>(-1)
    var day = MutableLiveData<Int>(-1)
    var hour = MutableLiveData<Int>(-1)
    var minute = MutableLiveData<Int>(-1)

    var birthLabel = MutableLiveData<String>("")
    var birthTimeLabel = MutableLiveData<String>("")
    var isIncludeTime = MutableLiveData<Boolean>(false)

    var birthPlace = MutableLiveData<String>("대한민국")
    var timeDiff = MutableLiveData<Int>(-30)
    var birthPlaceLabel = MutableLiveData<String>("대한민국 (-30분)")

    var useSummerTime = MutableLiveData<Boolean>(false)
    var useTokyoTime = MutableLiveData<Boolean>(false)


    init {
        val today = Calendar.getInstance()
        year.value = today.get(Calendar.YEAR)
        month.value = today.get(Calendar.MONTH) + 1
        day.value = today.get(Calendar.DAY_OF_MONTH)
    }

    fun onGenderButtonClick(value: Int) {
        gender.value = value
    }

    fun openBirthPicker(view: View) {
        val context = view.context

        DatePickerDialog(
            context,
            R.style.Theme_Holo_Light_Dialog_MinWidth,
            { datePicker, y, m, d ->
                year.value = y
                month.value = m + 1
                day.value = d

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year.value!!)
                    set(Calendar.MONTH, month.value!! - 1)
                    set(Calendar.DAY_OF_MONTH, day.value!!)
                }

                birthLabel.value = Utils.dateSlideFormat.format(calendar.timeInMillis)
            }, year.value!!, month.value!! - 1, day.value!!
        ).apply {
            datePicker.calendarViewShown = false
            window!!.setBackgroundDrawableResource(R.color.transparent)
            show()
        }
    }

    fun openBirthTimePicker(view: View) {
        val context = view.context

        TimePickerDialog(
            context,
            R.style.Theme_Holo_Light_Dialog_MinWidth,
            { timePicker, h, m ->
                hour.value = h
                minute.value = m

                birthTimeLabel.value = Utils.timeFormat.format(
                    Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hour.value!!)
                        set(Calendar.MINUTE, minute.value!!)
                    }.timeInMillis
                )
            }, hour.value!!, minute.value!!, false
        ).apply {
            window!!.setBackgroundDrawableResource(R.color.transparent)
            show()
        }
    }

    fun isValid(context: Context): Boolean {
        if (firstName.value!!.isEmpty()) {
            Toast.makeText(context, "성을 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lastName.value!!.isEmpty()) {
            Toast.makeText(context, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (birthLabel.value!!.isEmpty()) {
            Toast.makeText(context, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (isIncludeTime.value!! && birthTimeLabel.value!!.isEmpty()) {
            Toast.makeText(context, "태어난 시간을 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (birthPlace.value!!.isEmpty()) {
            Toast.makeText(context, "출생지를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }


        Toast.makeText(context, "유효성 검사 통과", Toast.LENGTH_SHORT).show()


        return true
    }

    fun toUserEntity(): User {
        return User(
            id = 0,
            firstName = firstName.value!!,
            lastName = lastName.value!!,
            gender = gender.value!!,
            birthYear = year.value!!,
            birthMonth = month.value!!,
            birthDay = day.value!!,
            includeTime = isIncludeTime.value!!,
            birthHour = hour.value!!,
            birthMinute = minute.value!!,
            birthPlace = birthPlace.value!!,
            timeDiff = timeDiff.value!!,
            useSummerTime = if (useSummerTime.value!!) 1 else 0,
            useTokyoTime = if (useTokyoTime.value!!) 1 else 0,
            memo = ""
        )
    }
}