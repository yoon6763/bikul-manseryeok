package com.example.manseryeok.userDB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper


class UserDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table $TABLE_NAME(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$FIRST_NAME TEXT, " +
                    "$LAST_NAME TEXT, " +
                    "$GENDER INTEGER," +
                    "$BIRTH TEXT," +
                    "$BIRTH_PLACE TEXT," +
                    "$TIME_DIFF INTEGER," +
                    "$YEAR_PILLAR TEXT," +
                    "$MONTH_PILLAR TEXT," +
                    "$DAY_PILLAR TEXT, " +
                    "$TIME_PILLAR TEXT " +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // 데이터베이스 추가하기 insert
    fun insertData(
        firstName: String,
        lastName: String,
        gender: Int,
        birth: String,
        birthPlace: String,
        timeDiff: Int,
        yearPillar: String,
        monthPillar: String,
        dayPillar: String,
        timePillar: String
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(FIRST_NAME, firstName)
        contentValues.put(LAST_NAME, lastName)
        contentValues.put(GENDER, gender)
        contentValues.put(BIRTH, birth)
        contentValues.put(BIRTH_PLACE, birthPlace)
        contentValues.put(TIME_DIFF, timeDiff)
        contentValues.put(YEAR_PILLAR, yearPillar)
        contentValues.put(MONTH_PILLAR, monthPillar)
        contentValues.put(DAY_PILLAR, dayPillar)
        contentValues.put(TIME_PILLAR, timePillar)
        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
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
        yearPillar: String,
        monthPillar: String,
        dayPillar: String,
        timePillar: String
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(FIRST_NAME, firstName)
        contentValues.put(LAST_NAME, lastName)
        contentValues.put(GENDER, gender)
        contentValues.put(BIRTH, birth)
        contentValues.put(BIRTH_PLACE, birthPlace)
        contentValues.put(TIME_DIFF, timeDIff)
        contentValues.put(YEAR_PILLAR, yearPillar)
        contentValues.put(MONTH_PILLAR, monthPillar)
        contentValues.put(DAY_PILLAR, dayPillar)
        contentValues.put(TIME_PILLAR, timePillar)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    companion object {
        const val DATABASE_NAME = "UserDatabase.db" // 데이터베이스 명
        const val TABLE_NAME = "user_table" // 테이블 명

        // 테이블 항목
        const val INDEX = "ID"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val GENDER = "gender"
        const val BIRTH = "birth"
        const val BIRTH_PLACE = "birth_place"
        const val TIME_DIFF = "time_diff"
        const val YEAR_PILLAR = "year_pillar"
        const val MONTH_PILLAR = "month_pillar"
        const val DAY_PILLAR = "day_pillar"
        const val TIME_PILLAR = "time_pillar"
    }
}