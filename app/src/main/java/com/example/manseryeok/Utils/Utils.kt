package com.example.manseryeok.Utils

import android.icu.util.ChineseCalendar
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    const val INTENT_EXTRAS_USER = "INTENT_EXTRAS_USER"

    val TAG = "Utils"

    val dateNumFormat = SimpleDateFormat("yyyyMMdd")
    val dateTimeNumFormat = SimpleDateFormat("yyyyMMddHHmm")
    val dateSlideFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateTimeSlideFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val dateKorFormat = SimpleDateFormat("yyyy년 MM월 dd일")
    val timeFormat = SimpleDateFormat("a hh:mm")
    //const val DB_FILE_NAME = "Manseryeok.db"


    val tenGan = arrayOf(
        arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"),
        arrayOf("갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"),
        arrayOf("목", "목", "화", "화", "토", "토", "금", "금", "수", "수")
    )


    val twelveGan = arrayOf(
        arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"),
        arrayOf("자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"),
        arrayOf("수", "토", "목", "목", "토", "화", "화", "토", "금", "금", "토", "수")
    )

    val tenGanForYear = arrayOf(arrayOf("庚", "辛", "壬", "癸", "甲", "乙", "丙", "丁", "戊", "己"))


    val twelveGanForYear =
        arrayOf(arrayOf("申", "酉", "戌", "亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未"))

//    십간
//    甲 乙 丙 丁 戊 己 庚 辛 壬 癸
//    갑 을 병 정 무 기 경 신 임 계
//    목 목 화 화 토 토 금 금 수 수

//    십이지
//    子 丑 寅 卯 辰 巳 午 未 申 酉 戌 亥
//    자 축 인 묘 진 사 오 미 신 유 술 해
//    수 토 목 목 토 화 화 토 금 금 토 수

//    丙丁巳午 화
//    壬癸子亥 수
//    甲乙寅卯 목
//    庚辛申酉 금
//    戊己丑辰未戌 토

    fun getProperty(c: Char): Int {
        // 화 수 목 금 토
        //  0 1 2 3 4
        return when (c) {
            '丙', '丁', '巳', '午' -> 0 // 화
            '壬', '癸', '子', '亥' -> 1 // 수
            '甲', '乙', '寅', '卯' -> 2 // 목
            '庚', '辛', '申', '酉' -> 3 // 금
            '戊', '己', '丑', '辰', '未', '戌' -> 4 // 토
            else -> 5
        }
    }

    fun getYearGanji(year: Int): String {
        var result = ""
        result += tenGanForYear[0][year % 10]
        result += twelveGanForYear[0][year % 12]
        return result
    }

    val timeGanji = arrayOf(
//        시(時)  23~01 01~03 03~05 05~07 07~09 09~11 11~13 13~15 15~17 17~19 19~21 21~23
//    갑(甲), 기(己)일
        arrayOf("甲子", "乙丑", "丙寅", "丁卯", "戊辰", "己巳", "庚午", "辛未", "壬申", "癸酉", "甲戌", "乙亥"),
//    을(乙), 경(庚)일
        arrayOf("丙子", "丁丑", "戊寅", "己卯", "庚辰", "辛巳", "壬午", "癸未", "甲申", "乙酉", "丙戌", "丁亥"),
//    병(丙), 신(辛)일
        arrayOf("戊子", "己丑", "庚寅", "辛卯", "壬辰", "癸巳", "甲午", "乙未", "丙申", "丁酉", "戊戌", "己亥"),
//    정(丁), 임(壬)일
        arrayOf("庚子", "辛丑", "壬寅", "癸卯", "甲辰", "乙巳", "丙午", "丁未", "戊申", "己酉", "庚戌", "辛亥"),
//    무(戊), 계(癸)일
        arrayOf("壬子", "癸丑", "甲寅", "乙卯", "丙辰", "丁巳", "戊午", "己未", "庚申", "辛酉", "壬戌", "癸亥")
    )


    fun getTimeGanji(day: String, hour: Int): String {
        val dayIdx = when (day) {
            "甲", "己" -> 0
            "乙", "庚" -> 1
            "丙", "辛" -> 2
            "丁", "壬" -> 3
            "戊", "癸" -> 4
            else -> -1
        }

        val timeIdx = when (hour) {
            in 1 until 3 -> 1
            in 3 until 5 -> 2
            in 5 until 7 -> 3
            in 7 until 9 -> 4
            in 9 until 11 -> 5
            in 11 until 13 -> 6
            in 13 until 15 -> 7
            in 15 until 17 -> 8
            in 17 until 19 -> 9
            in 19 until 21 -> 10
            in 21 until 23 -> 11
            else -> 0
        }
        Log.d(
            TAG,
            "getTimeGanji:   Time = $hour    ${dayIdx}   $timeIdx     ${timeGanji[dayIdx][timeIdx]}"
        )

        return if (dayIdx == -1) "" else timeGanji[dayIdx][timeIdx]
    }

    /*** 음력날짜를 양력날짜로 변환* @param 음력날짜 (yyyyMMdd)* @return 양력날짜 (yyyyMMdd) */
    fun convertLunarToSolar(date: String): String {
        val nDate = date.replace("-", "")
        val cc = ChineseCalendar()
        val cal = Calendar.getInstance()
        cc[ChineseCalendar.EXTENDED_YEAR] = nDate.substring(0, 4).toInt() + 2637
        cc[ChineseCalendar.MONTH] = nDate.substring(4, 6).toInt() - 1
        cc[ChineseCalendar.DAY_OF_MONTH] = nDate.substring(6).toInt()
        cal.timeInMillis = cc.timeInMillis
        return dateTimeSlideFormat.format(cal.time)
    }

    /*** 양력날짜를 음력날짜로 변환* @param 양력날짜 (yyyyMMdd)* @return 음력날짜 (yyyyMMdd) */
    fun convertSolarToLunar(date: String): Long {
        val nDate = date.replace("-", "")
        val cc = ChineseCalendar()
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = nDate.substring(0, 4).toInt()
        cal[Calendar.MONTH] = nDate.substring(4, 6).toInt() - 1
        cal[Calendar.DAY_OF_MONTH] = nDate.substring(6).toInt()
        cc.timeInMillis = cal.timeInMillis
        val y = cc[ChineseCalendar.EXTENDED_YEAR] - 2637
        val m = cc[ChineseCalendar.MONTH] + 1
        val d = cc[ChineseCalendar.DAY_OF_MONTH]
        val ret = StringBuffer()
        ret.append(String.format("%04d", y)).append("-")
        ret.append(String.format("%02d", m)).append("-")
        ret.append(String.format("%02d", d))
        return dateSlideFormat.parse(ret.toString())!!.time
    }

}