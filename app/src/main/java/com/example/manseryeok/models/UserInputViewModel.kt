package com.example.manseryeok.models

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.manseryeok.models.user.User
import com.example.manseryeok.utils.Utils
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

class UserInputViewModel : ViewModel() {
    var name = MutableLiveData<String>("")
    var gender = MutableLiveData<Int>(0)

    var yearLabel = MutableLiveData<String>("")
    var monthLabel = MutableLiveData<String>("")
    var dayLabel = MutableLiveData<String>("")
    var hourLabel = MutableLiveData<String>("")
    var minuteLabel = MutableLiveData<String>("")

    var birthTimeLabel = MutableLiveData<String>("")
    var isIncludeTime = MutableLiveData<Boolean>(true)

    var birthPlace = MutableLiveData<String>("대한민국")
    var timeDiff = MutableLiveData<Int>(-30)
    var birthPlaceLabel = MutableLiveData<String>("대한민국 (-30분)")

    var useSummerTime = MutableLiveData<Boolean>(false)
    var useTokyoTime = MutableLiveData<Boolean>(false)

    var birthType = MutableLiveData<Int>(0)

    fun onGenderButtonClick(value: Int) {
        gender.value = value
    }

    fun onBirthTypeButtonClick(value: Int) {
        birthType.value = value
    }

    fun clearBirthTimeInfo(isChecked: Boolean) {
        if (isChecked) {
            hourLabel.value = ""
            minuteLabel.value = ""
        }
    }

    fun isValid(context: Context): Boolean {
        if (name.value!!.isEmpty()) {
            Toast.makeText(context, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (yearLabel.value!!.isEmpty() || monthLabel.value!!.isEmpty() || dayLabel.value!!.isEmpty()) {
            Toast.makeText(context, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidDate()) {
            Toast.makeText(context, "유효한 날짜가 아닙니다", Toast.LENGTH_SHORT).show()
            return false
        }

        if (yearLabel.value!!.toInt() !in 1900..2100) {
            Toast.makeText(context, "1900년 ~ 2100년 사이의 날짜만 지원합니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        if(isIncludeTime.value!!) {
            if ((hourLabel.value!!.isEmpty() || minuteLabel.value!!.isEmpty())) {
                Toast.makeText(context, "태어난 시간을 입력해주세요", Toast.LENGTH_SHORT).show()
                return false
            }

            if (!isValidTime()) {
                Toast.makeText(context, "유효한 시간이 아닙니다", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if (birthPlace.value!!.isEmpty()) {
            Toast.makeText(context, "출생지를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidDate(): Boolean {
        return try {
            LocalDate.of(
                yearLabel.value!!.toInt(),
                monthLabel.value!!.toInt(),
                dayLabel.value!!.toInt()
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun isValidTime(): Boolean {
        return hourLabel.value!!.toInt() in 0..23 && minuteLabel.value!!.toInt() in 0..59
    }

    fun toUserEntity(): User {
        var birthYear = 0
        var birthMonth = 0
        var birthDay = 0
        var birthHour = 0
        var birthMinute = 0

        if (birthType.value == 0) {
            birthYear = yearLabel.value!!.toInt()
            birthMonth = monthLabel.value!!.toInt()
            birthDay = dayLabel.value!!.toInt()
        } else {
            val lunar = Utils.convertLunarToSolar(
                yearLabel.value!!.toInt(),
                monthLabel.value!!.toInt(),
                dayLabel.value!!.toInt()
            )
            birthYear = lunar[Calendar.YEAR]
            birthMonth = lunar[Calendar.MONTH] + 1
            birthDay = lunar[Calendar.DAY_OF_MONTH]
        }

        if (isIncludeTime.value!!) {
            birthHour = hourLabel.value!!.toInt()
            birthMinute = minuteLabel.value!!.toInt()
        } else {
            birthHour = 0
            birthMinute = 0
        }

        return User(
            userId = 0,
            name = name.value!!,
            gender = gender.value!!,
            birthYear = birthYear,
            birthMonth = birthMonth,
            birthDay = birthDay,
            includeTime = isIncludeTime.value!!,
            birthHour = birthHour,
            birthMinute = birthMinute,
            birthPlace = birthPlace.value!!,
            timeDiff = timeDiff.value!!,
            useSummerTime = if (useSummerTime.value!!) 1 else 0,
            useTokyoTime = if (useTokyoTime.value!!) 1 else 0,
            memo = ""
        )
    }

    fun updateViewModel(user: User) {
        name.value = user.name
        gender.value = gender.value
        yearLabel.value = user.birthYear.toString()
        monthLabel.value = user.birthMonth.toString()
        dayLabel.value = user.birthDay.toString()
        isIncludeTime.value = user.includeTime

        if (user.includeTime) {
            hourLabel.value = user.birthHour.toString()
            minuteLabel.value = user.birthMinute.toString()
        } else {
            hourLabel.value = ""
            minuteLabel.value = ""
        }

        birthPlace.value = user.birthPlace
        timeDiff.value = user.timeDiff
        useSummerTime.value = user.useSummerTime == 1
        useTokyoTime.value = user.useTokyoTime == 1
    }
}