package com.example.manseryeok.service.calendar

import android.content.Context
import android.util.Log
import com.example.manseryeok.manseryeokdb.Season24SQLHelper
import java.time.LocalDate
import java.time.LocalDateTime

// 시간은 있을 수도 있고 없을 수도 있음. 이 경우 23:59로 설정
class CalendarService(
    private val context: Context,
    var userBirth: LocalDateTime,
    var isTimeInclude: Boolean
) {
    private val TAG = "CalendarService"

    private lateinit var season24SQLHelper: Season24SQLHelper

    lateinit var yearHangulGanji: String
    lateinit var monthHangulGanji: String
    lateinit var dayHangulGanji: String
    lateinit var timeHangulGanji: String

    lateinit var yearHanjaGanji: String
    lateinit var monthHanjaGanji: String
    lateinit var dayHanjaGanji: String
    lateinit var timeHanjaGanji: String

    fun calcGanji() {
        season24SQLHelper = Season24SQLHelper(context)
        season24SQLHelper.createDataBase()
        season24SQLHelper.open()

        yearHanjaGanji = calcYearGanji()
        monthHanjaGanji = calcMonthGanji()
        dayHanjaGanji = calcDayGanji()
        timeHanjaGanji = calcTimeGanji()

        yearHangulGanji = GabjaConstants.getHanjaToHangul(yearHanjaGanji)
        monthHangulGanji = GabjaConstants.getHangulToHanja(monthHanjaGanji)
        dayHangulGanji = GabjaConstants.getHangulToHanja(dayHanjaGanji)
        timeHangulGanji = GabjaConstants.getHangulToHanja(timeHanjaGanji)

        season24SQLHelper.close()

        Log.d(TAG, "yearHanjaGanji: ${userBirth.year}  $yearHanjaGanji")
        Log.d(TAG, "monthHanjaGanji: ${userBirth.monthValue}  $monthHanjaGanji")
        Log.d(TAG, "dayHanjaGanji: ${userBirth.dayOfMonth}  $dayHanjaGanji")
        Log.d(TAG, "timeHanjaGanji: ${userBirth.hour}  $timeHanjaGanji")
    }

    private fun calcYearGanji(): String {
        val sibganForYear = arrayOf(
            "庚", "辛", "壬", "癸", "甲",
            "乙", "丙", "丁", "戊", "己"
        ) // 연도 계산 용 십간
        val sibijiForYear = arrayOf(
            "申", "酉", "戌", "亥", "子", "丑",
            "寅", "卯", "辰", "巳", "午", "未"
        ) // 연도 계산 용 십이지

        var targetYear = userBirth.year
        if (!season24SQLHelper.isReachSpring(userBirth)) targetYear--

        val sibgan = sibganForYear[targetYear % 10]
        val sibiji = sibijiForYear[targetYear % 12]

        return sibgan + sibiji
    }

    private fun calcMonthGanji(): String {
        // 년 간지의 첫 글자를 이용하여 월 간지를 구한다

        val monthGanjiList = when (yearHanjaGanji[0]) {
            '甲', '己' -> "丙寅 丁卯 戊辰 己巳 庚午 辛未 壬申 癸酉 甲戌 乙亥 丙子 丁丑"
            '乙', '庚' -> "戊寅 己卯 庚辰 辛巳 壬午 癸未 甲申 乙酉 丙戌 丁亥 戊子 己丑"
            '丙', '辛' -> "庚寅 辛卯 壬辰 癸巳 甲午 乙未 丙申 丁酉 戊戌 己亥 庚子 辛丑"
            '丁', '壬' -> "壬寅 癸卯 甲辰 乙巳 丙午 丁未 戊申 己酉 庚戌 辛亥 壬子 癸丑"
            '戊', '癸' -> "甲寅 乙卯 丙辰 丁巳 戊午 己未 庚申 辛酉 壬戌 癸亥 甲子 乙丑"
            else -> throw Exception("Invalid yearSibgan")
        }

        var monthIndex = userBirth.monthValue - 1
        Log.d(TAG, "calcMonthGanji: monthIndex: $monthIndex")
        if (!season24SQLHelper.isReachMonth(userBirth)) monthIndex--
        Log.d(TAG, "calcMonthGanji: ${season24SQLHelper.isReachMonth(userBirth)}")
        Log.d(TAG, "calcMonthGanji: monthIndex: $monthIndex")
        if (monthIndex == -1) monthIndex = 11
        monthIndex--
        if (monthIndex == -1) monthIndex = 11

        return monthGanjiList.split(" ")[monthIndex]
    }

    private fun calcDayGanji(): String {
        var tempDay = LocalDate.of(1900, 1, 1)

        var daySibganIndex = 0
        var daySibijiIndex = 10

        while (isNotEqualDay(tempDay, userBirth.toLocalDate())) {
            tempDay = tempDay.plusDays(1)
            daySibganIndex++
            daySibijiIndex++
            daySibganIndex %= 10
            daySibijiIndex %= 12
        }

        // 23:30 이후면 다음날로 침
        if (isTimeInclude && userBirth.hour >= 23 && userBirth.minute >= 30) {
            daySibganIndex++
            daySibijiIndex++
            daySibganIndex %= 10
            daySibijiIndex %= 12
        }

        return GabjaConstants.SIBGAN_HANJA[daySibganIndex] + GabjaConstants.SIBIJI_HANJA[daySibijiIndex]
    }

    private fun calcTimeGanji(): String {
        val ganjiForTime = arrayOf(
            //    시(時)  23~01 01~03 03~05 05~07 07~09 09~11 11~13 13~15 15~17 17~19 19~21 21~23
            //    갑(甲), 기(己)일
            arrayOf(
                "甲子", "乙丑", "丙寅", "丁卯", "戊辰", "己巳",
                "庚午", "辛未", "壬申", "癸酉", "甲戌", "乙亥"
            ),
            //    을(乙), 경(庚)일
            arrayOf(
                "丙子", "丁丑", "戊寅", "己卯", "庚辰", "辛巳",
                "壬午", "癸未", "甲申", "乙酉", "丙戌", "丁亥"
            ),
            //    병(丙), 신(辛)일
            arrayOf(
                "戊子", "己丑", "庚寅", "辛卯", "壬辰", "癸巳",
                "甲午", "乙未", "丙申", "丁酉", "戊戌", "己亥"
            ),
            //    정(丁), 임(壬)일
            arrayOf(
                "庚子", "辛丑", "壬寅", "癸卯", "甲辰", "乙巳",
                "丙午", "丁未", "戊申", "己酉", "庚戌", "辛亥"
            ),
            //    무(戊), 계(癸)일
            arrayOf(
                "壬子", "癸丑", "甲寅", "乙卯", "丙辰", "丁巳",
                "戊午", "己未", "庚申", "辛酉", "壬戌", "癸亥"
            )
        )

        val dayIdx = when (dayHanjaGanji[0]) {
            '甲', '己' -> 0
            '乙', '庚' -> 1
            '丙', '辛' -> 2
            '丁', '壬' -> 3
            '戊', '癸' -> 4
            else -> -1
        }

        val timeIdx = when (userBirth.hour) {
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

        return ganjiForTime[dayIdx][timeIdx]
    }

    private fun isEqualDay(day1: LocalDate, day2: LocalDate): Boolean {
        return day1.year == day2.year &&
                day1.month == day2.month &&
                day1.dayOfMonth == day2.dayOfMonth
    }

    private fun isNotEqualDay(day1: LocalDate, day2: LocalDate): Boolean {
        return !isEqualDay(day1, day2)
    }

    // 공망
    fun calcGongmang(ganji: String): String {
        return when (ganji) {
            "甲子", "乙丑", "丙寅", "丁卯", "戊辰", "己巳", "庚午", "辛未", "壬申", "癸酉" -> "戌亥"
            "甲戌", "乙亥", "丙子", "丁丑", "戊寅", "己卯", "庚辰", "辛巳", "壬午", "癸未" -> "申酉"
            "甲申", "乙酉", "丙戌", "丁亥", "戊子", "己丑", "庚寅", "辛卯", "壬辰", "癸巳" -> "午未"
            "甲午", "乙未", "丙申", "丁酉", "戊戌", "己亥", "庚子", "辛丑", "壬寅", "癸卯" -> "辰巳"
            "甲辰", "乙巳", "丙午", "丁未", "戊申", "己酉", "庚戌", "辛亥", "壬子", "癸丑" -> "寅卯"
            "甲寅", "乙卯", "丙辰", "丁巳", "戊午", "己未", "庚申", "辛酉", "壬戌", "癸亥" -> "子丑"
            else -> ""
        }
    }
}