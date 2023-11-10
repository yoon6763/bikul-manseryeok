package com.example.manseryeok.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.manseryeok.models.UserInputViewModel
import java.util.Calendar

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long,
    var firstName: String?,
    var lastName: String?,
    var gender: Int, // 0 - 남자, 1 - 여자

    var birthYear: Int,
    var birthMonth: Int,
    var birthDay: Int,
    var birthHour: Int,
    var birthMinute: Int,
    var includeTime: Boolean,

    var birthPlace: String?,
    var timeDiff: Int,

    // 0 - 사용안함, 1 - 사용
    var useSummerTime: Int,
    var useTokyoTime: Int,

    var memo: String?
) {

    fun getBirthOrigin() = Calendar.getInstance().apply {
        set(Calendar.YEAR, birthYear)
        set(Calendar.MONTH, birthMonth - 1)
        set(Calendar.DAY_OF_MONTH, birthDay)

        if (includeTime) {
            set(Calendar.HOUR_OF_DAY, birthHour)
            set(Calendar.MINUTE, birthMinute)
        }
    }

    fun getBirthCalculated(): Calendar {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, birthYear)
            set(Calendar.MONTH, birthMonth - 1)
            set(Calendar.DAY_OF_MONTH, birthDay)
        }

        if (includeTime) {
            calendar.set(Calendar.HOUR_OF_DAY, birthHour)
            calendar.set(Calendar.MINUTE, birthMinute)
            calendar.add(Calendar.MINUTE, timeDiff) // 시차 적용

            if (useSummerTime == 1) calendar.add(Calendar.HOUR_OF_DAY, 1)
            if (useTokyoTime == 1) calendar.add(Calendar.MINUTE, 30)

            // 23:30 이후면 다음날로 침
            if (includeTime && birthHour == 23 && birthMinute >= 30) calendar.add(
                Calendar.HOUR_OF_DAY,
                1
            )
        }

        return calendar
    }

    fun updateFromViewModel(userInputViewModel: UserInputViewModel) {
        this.firstName = userInputViewModel.firstName.value
        this.lastName = userInputViewModel.lastName.value
        this.gender = userInputViewModel.gender.value!!
        this.birthYear = userInputViewModel.year.value!!
        this.birthMonth = userInputViewModel.month.value!!
        this.birthDay = userInputViewModel.day.value!!
        this.includeTime = userInputViewModel.isIncludeTime.value!!

        if (this.includeTime) {
            this.birthHour = userInputViewModel.hour.value!!
            this.birthMinute = userInputViewModel.minute.value!!
        } else {
            this.birthHour = -1
            this.birthMinute = -1
        }

        this.birthPlace = userInputViewModel.birthPlace.value
        this.timeDiff = userInputViewModel.timeDiff.value!!

        this.useSummerTime = if (userInputViewModel.useSummerTime.value!!) 1 else 0
        this.useTokyoTime = if (userInputViewModel.useTokyoTime.value!!) 1 else 0
    }
}