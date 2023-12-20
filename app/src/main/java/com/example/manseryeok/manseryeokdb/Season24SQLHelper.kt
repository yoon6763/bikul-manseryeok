package com.example.manseryeok.manseryeokdb

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.Season
import java.io.IOException
import java.time.LocalDateTime


class Season24SQLHelper(val context: Context) {
    private val TAG = "Season24SQLHelper"
    private val TABLE_NAME = "SEASON"
    private var dbHelper: Season24DBHelper? = null
    private var db: SQLiteDatabase? = null

    init {
        dbHelper = Season24DBHelper(context)
    }

    fun createDataBase(): Season24SQLHelper {
        try {
            dbHelper?.createDataBase()
        } catch (e: IOException) {
            Log.d(TAG, e.message!!)
        }
        return this
    }

    fun open(): Season24SQLHelper {
        try {
            dbHelper?.openDataBase()
            dbHelper?.close()
            db = dbHelper?.readableDatabase
        } catch (e: IOException) {
            Log.d(TAG, e.message!!)
        }
        return this
    }

    fun close() {
        dbHelper?.close()
    }

    fun sqlTest() {
        Log.d(TAG, "sqlTest: point1")
        try {
            val sql = "SELECT * FROM $TABLE_NAME"
            Log.d(TAG, "sqlTest: ${db!!.isOpen}")

            val cur: Cursor = db!!.rawQuery(sql, null)
            Log.d(TAG, "sqlTest: sdfdfgdsfghsfdgh")
            Log.d(TAG, "sqlTest: ${cur.count}")

            while (cur.moveToNext()) {
                Log.d(TAG, "sqlTest: ${cur.getString(0)} ${cur.getString(1)} ${cur.getString(2)}")
            }

        } catch (mSQLException: SQLException) {
            Log.e(TAG, "getTestData >>$mSQLException")
            throw mSQLException
        }
    }

    // 입춘을 넘겼는가
    fun isReachSpring(date: LocalDateTime): Boolean {
        try {
            // 2월의 첫 번째 시즌이 입춘
            val sql =
                "SELECT * FROM $TABLE_NAME WHERE YEAR = ${date.year} AND MONTH = 2 ORDER BY DAY ASC LIMIT 1;"

            val cur: Cursor = db!!.rawQuery(sql, null)
            if (cur != null) {
                while (cur.moveToNext()) {
                    val year = cur.getInt(1)
                    val month = cur.getInt(2)
                    val day = cur.getInt(3)
                    val hour = cur.getInt(4)
                    val minute = cur.getInt(5)

                    val springDate = LocalDateTime.of(year, month, day, hour, minute,0)
                    return date.isAfter(springDate)
                }
            }
        } catch (mSQLException: SQLException) {
            Log.e(TAG, "getTestData >>$mSQLException")
            throw mSQLException
        }
        return false
    }

    // 해당 월을 넘겼는가
    fun isReachMonth(date: LocalDateTime): Boolean {
        try {
            val sql =
                "SELECT * FROM $TABLE_NAME WHERE YEAR = ${date.year} AND MONTH = ${date.monthValue} ORDER BY DAY ASC LIMIT 1;"

            val cur: Cursor = db!!.rawQuery(sql, null)
            if (cur != null) {
                while (cur.moveToNext()) {
                    val year = cur.getInt(1)
                    val month = cur.getInt(2)
                    val day = cur.getInt(3)
                    val hour = cur.getInt(4)
                    val minute = cur.getInt(5)

                    val monthDate = LocalDateTime.of(year, month, day, hour, minute,0)
                    return date.isAfter(monthDate)
                }
            }
        } catch (mSQLException: SQLException) {
            Log.e(TAG, "getTestData >>$mSQLException")
            throw mSQLException
        }
        return false
    }


    fun getDayData(year: Int, month: Int, day: Int): Manseryeok {
        try {
            // Table 이름
            val sql =
                "SELECT * FROM $TABLE_NAME WHERE cd_sy = $year AND cd_sm = $month AND cd_sd = $day;"

            // 모델 넣을 리스트 생성
            val manList = ArrayList<Manseryeok>()

            // TODO : 모델 선언
            var manseryeokModel: Manseryeok? = null
            val mCur: Cursor = db!!.rawQuery(sql, null)
            if (mCur != null) {
                // 칼럼의 마지막까지
                while (mCur.moveToNext()) {

                    // TODO : 커스텀 모델 생성
                    manseryeokModel = Manseryeok(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    )

                    manseryeokModel.cd_no = mCur.getInt(0)
                    manseryeokModel.cd_sgi = mCur.getInt(1)
                    manseryeokModel.cd_sy = mCur.getInt(2)
                    manseryeokModel.cd_sm = mCur.getInt(3)
                    manseryeokModel.cd_sd = mCur.getInt(4)
                    manseryeokModel.cd_ly = mCur.getInt(5)
                    manseryeokModel.cd_lm = mCur.getInt(6)
                    manseryeokModel.cd_ld = mCur.getInt(7)
                    manseryeokModel.cd_hyganjee = mCur.getString(8)
                    manseryeokModel.cd_kyganjee = mCur.getString(9)
                    manseryeokModel.cd_hmganjee = mCur.getString(10)
                    manseryeokModel.cd_kmganjee = mCur.getString(11)
                    manseryeokModel.cd_hdganjee = mCur.getString(12)
                    manseryeokModel.cd_kdganjee = mCur.getString(13)
                    manseryeokModel.cd_hweek = mCur.getString(14)
                    manseryeokModel.cd_kweek = mCur.getString(15)
                    manseryeokModel.cd_stars = mCur.getString(16)
                    manseryeokModel.cd_moon_state = mCur.getString(17)
                    manseryeokModel.cd_moon_time = mCur.getInt(18)
                    manseryeokModel.cd_leap_month = mCur.getInt(19)
                    manseryeokModel.cd_month_size = mCur.getInt(20)
                    manseryeokModel.cd_hterms = mCur.getString(21)
                    manseryeokModel.cd_kterms = mCur.getString(22)
                    manseryeokModel.cd_terms_time = mCur.getLong(23)
                    manseryeokModel.cd_keventday = mCur.getString(24)
                    manseryeokModel.cd_ddi = mCur.getString(25)
                    manseryeokModel.cd_sol_plan = mCur.getString(26)
                    manseryeokModel.cd_lun_plan = mCur.getString(27)
                    manseryeokModel.holiday = mCur.getInt(28)

                    // 리스트에 넣기
                    manList.add(manseryeokModel)
                }
            }
            return manList.toList()[0]
        } catch (mSQLException: SQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString())
            throw mSQLException
        }
    }

    fun getTableData(startYear: Int, endYear: Int): List<Manseryeok>? {
        try {
            // Table 이름
            val sql = "SELECT * FROM $TABLE_NAME WHERE cd_sy BETWEEN $startYear AND $endYear;"

            // 모델 넣을 리스트 생성
            val manList = ArrayList<Manseryeok>()

            // TODO : 모델 선언
            var manseryeokModel: Manseryeok? = null
            val mCur: Cursor = db!!.rawQuery(sql, null)
            if (mCur != null) {
                // 칼럼의 마지막까지
                while (mCur.moveToNext()) {

                    // TODO : 커스텀 모델 생성
                    manseryeokModel = Manseryeok(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    )

                    manseryeokModel.cd_no = mCur.getInt(0)
                    manseryeokModel.cd_sgi = mCur.getInt(1)
                    manseryeokModel.cd_sy = mCur.getInt(2)
                    manseryeokModel.cd_sm = mCur.getInt(3)
                    manseryeokModel.cd_sd = mCur.getInt(4)
                    manseryeokModel.cd_ly = mCur.getInt(5)
                    manseryeokModel.cd_lm = mCur.getInt(6)
                    manseryeokModel.cd_ld = mCur.getInt(7)
                    manseryeokModel.cd_hyganjee = mCur.getString(8)
                    manseryeokModel.cd_kyganjee = mCur.getString(9)
                    manseryeokModel.cd_hmganjee = mCur.getString(10)
                    manseryeokModel.cd_kmganjee = mCur.getString(11)
                    manseryeokModel.cd_hdganjee = mCur.getString(12)
                    manseryeokModel.cd_kdganjee = mCur.getString(13)
                    manseryeokModel.cd_hweek = mCur.getString(14)
                    manseryeokModel.cd_kweek = mCur.getString(15)
                    manseryeokModel.cd_stars = mCur.getString(16)
                    manseryeokModel.cd_moon_state = mCur.getString(17)
                    manseryeokModel.cd_moon_time = mCur.getInt(18)
                    manseryeokModel.cd_leap_month = mCur.getInt(19)
                    manseryeokModel.cd_month_size = mCur.getInt(20)
                    manseryeokModel.cd_hterms = mCur.getString(21)
                    manseryeokModel.cd_kterms = mCur.getString(22)
                    manseryeokModel.cd_terms_time = mCur.getLong(23)
                    manseryeokModel.cd_keventday = mCur.getString(24)
                    manseryeokModel.cd_ddi = mCur.getString(25)
                    manseryeokModel.cd_sol_plan = mCur.getString(26)
                    manseryeokModel.cd_lun_plan = mCur.getString(27)
                    manseryeokModel.holiday = mCur.getInt(28)

                    // 리스트에 넣기
                    manList.add(manseryeokModel)
                }
            }
            return manList.toList()
        } catch (mSQLException: SQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString())
            throw mSQLException
        }
    }
}
