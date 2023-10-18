package com.example.manseryeok.models.user

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
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

    var birthPlace: String?,
    var timeDiff: Int,

    var useSummerTime: Int,
    var useTokyoTime: Int,

    var memo: String?,
    var tag: String?

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeInt(gender)
        parcel.writeInt(birthYear)
        parcel.writeInt(birthMonth)
        parcel.writeInt(birthDay)
        parcel.writeInt(birthHour)
        parcel.writeInt(birthMinute)
        parcel.writeString(birthPlace)
        parcel.writeInt(timeDiff)
        parcel.writeInt(useSummerTime)
        parcel.writeInt(useTokyoTime)
        parcel.writeString(memo)
        parcel.writeString(tag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

    fun getBirthCalendar(): Calendar {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, birthYear)
            set(Calendar.MONTH, birthMonth - 1)
            set(Calendar.DAY_OF_MONTH, birthDay)
        }

        if (birthHour != -1) {
            calendar.set(Calendar.HOUR_OF_DAY, birthHour)
            calendar.set(Calendar.MINUTE, birthMinute)
            calendar.add(Calendar.MINUTE, timeDiff)
        }
        if (useSummerTime == 1) calendar.add(Calendar.HOUR_OF_DAY, 1)
        if (useTokyoTime == 1) calendar.add(Calendar.MINUTE, 30)

        return calendar
    }

    fun getBirthLocalDate(): LocalDateTime {
        val localDateTime =
            LocalDateTime.of(birthYear, birthMonth, birthDay, birthHour, birthMinute)
        return localDateTime
    }
}