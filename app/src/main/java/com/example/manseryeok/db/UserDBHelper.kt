package com.example.manseryeok.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import com.example.manseryeok.models.User


class UserDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table $TABLE_NAME(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$FIRST_NAME TEXT, " +
                    "$LAST_NAME TEXT, " +
                    "$GENDER INTEGER," +
                    "$BIRTH_YEAR INTEGER," +
                    "$BIRTH_MONTH INTEGER," +
                    "$BIRTH_DAY INTEGER," +
                    "$BIRTH_HOUR INTEGER," +
                    "$BIRTH_MINUTE INTEGER," +
                    "$BIRTH_PLACE TEXT," +
                    "$TIME_DIFF INTEGER," +
                    "$USE_SUMMER_TIME INTEGER," +
                    "$USE_TOKYO_TIME INTEGER," +
                    "$MEMO TEXT" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // 데이터베이스 추가하기 insert
    fun insertData(user: User): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

//      firstName: String?,
//      lastName: String?,
//      gender: Int, // 0 - 남자, 1 - 여자

//      birthYear: Int,
//      birthMonth: Int,
//      birthDay: Int,
//      birthHour: Int,
//      birthMinute: Int,

//      birthPlace: String?,
//      timeDiff: Int,

//      useSummerTime: Boolean = false,
//      useTokyoTime: Boolean = false

        contentValues.put(FIRST_NAME, user.firstName)
        contentValues.put(LAST_NAME, user.lastName)
        contentValues.put(GENDER, if (user.gender == 0) 0 else 1)
        contentValues.put(BIRTH_YEAR, user.birthYear)
        contentValues.put(BIRTH_MONTH, user.birthMonth)
        contentValues.put(BIRTH_DAY, user.birthDay)
        contentValues.put(BIRTH_HOUR, user.birthHour) // 모를 경우 -1
        contentValues.put(BIRTH_MINUTE, user.birthMinute) // 모를 경우 -1

        contentValues.put(BIRTH_PLACE, user.birthPlace)
        contentValues.put(TIME_DIFF, user.timeDiff)

        contentValues.put(USE_SUMMER_TIME, user.useSummerTime)
        contentValues.put(USE_TOKYO_TIME, user.useTokyoTime)

        contentValues.put(MEMO, "")

        // 삽입한 행의 id값을 반환한다
        return db.insert(TABLE_NAME, null, contentValues)
    }

    //데이터베이스 항목 읽어오기 Read
    val allData: Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("select * from $TABLE_NAME", null)
        }

    // 데이터베이스 삭제하기
    fun deleteData(id: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "ID = ? ", arrayOf(id))
    }

    //데이터베이스 수정하기
    fun updateData(
        id: String,
        firstName: String,
        lastName: String,
        gender: Int,
        birth: String,
        birthPlace: String,
        timeDIff: Int,
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        return true
    }

    fun updateMemo(id: Long, memo: String):Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MEMO, memo)
        return db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id.toString())) != -1
    }

    companion object {
        const val DATABASE_NAME = "UserDatabase.db" // 데이터베이스 명
        const val TABLE_NAME = "user_table" // 테이블 명


//      firstName: String?,
//      lastName: String?,
//      gender: Int, // 0 - 남자, 1 - 여자
//
//      birthYear: Int,
//      birthMonth: Int,
//      birthDay: Int,
//      birthHour: Int,
//      birthMinute: Int,
//
//      birthPlace: String?,
//      timeDiff: Int,
//
//      useSummerTime: Boolean = false,
//      useTokyoTime: Boolean = false

        // 테이블 항목
        const val INDEX = "ID"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val GENDER = "gender"

        const val BIRTH_YEAR = "birth_year"
        const val BIRTH_MONTH = "birth_month"
        const val BIRTH_DAY = "birth_day"
        const val BIRTH_HOUR = "birth_hour"
        const val BIRTH_MINUTE = "birth_minute"

        const val BIRTH_PLACE = "birth_place"
        const val TIME_DIFF = "time_diff"

        const val USE_SUMMER_TIME = "use_summer_time"
        const val USE_TOKYO_TIME = "use_tokyo_time"

        const val MEMO = "memo"
    }
}