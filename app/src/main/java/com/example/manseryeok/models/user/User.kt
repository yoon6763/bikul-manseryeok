package com.example.manseryeok.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
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

    var useSummerTime: Int,
    var useTokyoTime: Int,

    var memo: String?
) {
    fun getBirthOrigin()=Calendar.getInstance().apply {
        set(Calendar.YEAR, birthYear)
        set(Calendar.MONTH, birthMonth - 1)
        set(Calendar.DAY_OF_MONTH, birthDay)

        if(includeTime) {
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
            calendar.add(Calendar.MINUTE, timeDiff)

            if (useSummerTime == 1) calendar.add(Calendar.HOUR_OF_DAY, 1)
            if (useTokyoTime == 1) calendar.add(Calendar.MINUTE, 30)

            // 23:30 이후면 다음날로 침
            if (includeTime && birthHour == 23 && birthMinute >= 30) calendar.add(Calendar.HOUR_OF_DAY, 1)
        }

        return calendar
    }
}