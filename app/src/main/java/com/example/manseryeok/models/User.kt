package com.example.manseryeok.models

import android.os.Parcel
import android.os.Parcelable

data class User(
    var firstName: String?,
    var lastName: String?,
    var gender: Int, // 0 - 남자, 1 - 여자
    var birth: String?, // yyyyMMddHHmm or yyyyMMdd
    var birthPlace: String?,
    var timeDiff: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeInt(gender)
        parcel.writeString(birth)
        parcel.writeString(birthPlace)
        parcel.writeInt(timeDiff)
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
}