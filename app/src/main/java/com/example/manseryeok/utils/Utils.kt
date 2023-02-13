package com.example.manseryeok.utils

import android.icu.util.ChineseCalendar
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    const val INTENT_EXTRAS_USER = "INTENT_EXTRAS_USER"

    val TAG = "Utils"

    val dateNumFormat = SimpleDateFormat("yyyyMMdd")
    val dateTimeNumFormat = SimpleDateFormat("yyyyMMddHHmm")
    val dateSlideFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateTimeSlideFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val dateDotFormat = SimpleDateFormat("yyyy.MM.dd")
    val dateKorFormat = SimpleDateFormat("yyyy년 MM월 dd일")
    val dateTimeKorFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm")
    val timeFormat = SimpleDateFormat("a hh:mm")
    val degreeFormat = DecimalFormat("#.00")
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

    fun getEmptyGanji(ganji: String): String {
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

    val pillarLabelArr = arrayOf(
        //         甲 乙 丙 丁 戊 己 庚 辛 壬 癸
        arrayOf("편인", "정인", "편관", "정관", "편재", "정재", "식신", "상관", "비견", "겁재"), // 壬, 亥
        arrayOf("정인", "편인", "정관", "편관", "정재", "편재", "상관", "식신", "겁재", "비견"), // 癸, 子
        arrayOf("비견", "겁재", "편인", "정인", "편관", "정관", "편재", "정재", "식신", "상관"), // 甲, 寅
        arrayOf("겁재", "비견", "정인", "편인", "정관", "편관", "정재", "편재", "상관", "식신"), // 乙, 卯
        arrayOf("식신", "상관", "비견", "겁재", "편인", "정인", "편관", "정관", "편재", "정재"), // 丙, 巳
        arrayOf("상관", "식신", "겁재", "비견", "정인", "편인", "정관", "편관", "정재", "편재"), // 丁, 午
        arrayOf("편재", "정재", "식신", "상관", "비견", "겁재", "편인", "정인", "편관", "정관"), // 戊, 辰, 戌
        arrayOf("정재", "편재", "상관", "식신", "겁재", "비견", "정인", "편인", "정관", "편관"), // 己, 丑, 未
        arrayOf("편관", "정관", "편재", "정재", "식신", "상관", "비견", "겁재", "편인", "정인"), // 庚, 申
        arrayOf("정관", "편관", "정재", "편재", "상관", "식신", "겁재", "비견", "정인", "편인")  // 辛, 酉
    )


    // 육친 속견표
    fun getPillarLabel(me: String, target: String): String {
        val meIdx = tenGan[0].indexOf(me)
        val targetIdx = when (target) {
            "壬", "亥" -> 0
            "癸", "子" -> 1
            "甲", "寅" -> 2
            "乙", "卯" -> 3
            "丙", "巳" -> 4
            "丁", "午" -> 5
            "戊", "辰", "戌" -> 6
            "己", "丑", "未" -> 7
            "庚", "申" -> 8
            "辛", "酉" -> 9
            else -> -1
        }
        return if (meIdx != -1 && targetIdx != -1) {
            pillarLabelArr[targetIdx][meIdx]
        } else {
            "error"
        }
    }

    fun getYearGanji(year: Int): String {
        var result = ""
        result += tenGanForYear[0][year % 10]
        result += twelveGanForYear[0][year % 12]
        return result
    }

    fun getMonthGanji(char: Char): String {
        return when (char) {
            '甲', '己' -> "乙丑 丙寅 丁卯 戊辰 己巳 庚午 辛未 壬申 癸酉 甲戌 乙亥 丙子"
            '乙', '庚' -> "丁丑 戊寅 己卯 庚辰 辛巳 壬午 癸未 甲申 乙酉 丙戌 丁亥 戊子"
            '丙', '辛' -> "己丑 庚寅 辛卯 壬辰 癸巳 甲午 乙未 丙申 丁酉 戊戌 己亥 庚子"
            '丁', '壬' -> "辛丑 壬寅 癸卯 甲辰 乙巳 丙午 丁未 戊申 己酉 庚戌 辛亥 壬子"
            '戊', '癸' -> "癸丑 甲寅 乙卯 丙辰 丁巳 戊午 己未 庚申 辛酉 壬戌 癸亥 甲子"
            else -> ""
        }
    }

    // 오행
    fun getFiveProperty(ganji: String): String {
        return when (ganji) {
            "甲子", "乙丑" -> "海中金\n(해중금)"
            "甲戌", "乙亥" -> "山頭火\n(산두화)"
            "甲申", "乙酉" -> "泉中水\n(천중수)"
            "甲午", "乙未" -> "砂中金\n(사중금)"
            "甲辰", "乙巳" -> "覆燈火\n(복등화)"
            "甲寅", "乙卯" -> "大溪水\n(대계수)"
            "丙寅", "丁卯" -> "爐中火\n(노중화)"
            "丙子", "丁丑" -> "澗下水\n(간하수)"
            "丙戌", "丁亥" -> "屋上土\n(옥상토)"
            "丙申", "丁酉" -> "山下火\n(산하화)"
            "丙午", "丁未" -> "天河水\n(천하수)"
            "丙辰", "丁巳" -> "沙中土\n(사중토)"
            "戊辰", "己巳" -> "大林木\n(대림목)"
            "戊寅", "己卯" -> "城頭土\n(성두토)"
            "戊子", "己丑" -> "霹靂火\n(벽력화)"
            "戊戌", "己亥" -> "平地木\n(평지목)"
            "戊申", "己酉" -> "大驛土\n(대역토)"
            "戊午", "己未" -> "天上火\n(천상화)"
            "庚午", "辛未" -> "路傍土\n(노방토)"
            "庚辰", "辛巳" -> "白蠟金\n(백랍금)"
            "庚寅", "辛卯" -> "松柏木\n(송백목)"
            "庚子", "辛丑" -> "壁上土\n(벽상토)"
            "庚戌", "辛亥" -> "釵釧金\n(채천금)"
            "庚申", "辛酉" -> "石榴木\n(석류목)"
            "壬申", "癸酉" -> "劍鋒金\n(검봉금)"
            "壬午", "癸未" -> "楊柳木\n(양류목)"
            "壬辰", "癸巳" -> "長流水\n(장류수)"
            "壬寅", "癸卯" -> "金箔金\n(금박금)"
            "壬子", "癸丑" -> "桑柘木\n(상자목)"
            "壬戌", "癸亥" -> "大海水\n(대해수)"
            else -> ""
        }
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

    // 지지암장간
    fun getJijiAmJangan(char: Char): String {
        return when (char) {
            '子' -> "壬癸"
            '丑' -> "癸辛己"
            '寅' -> "戊丙甲"
            '卯' -> "甲乙"
            '辰' -> "乙癸戊"
            '巳' -> "戊庚丙"
            '午' -> "丙己丁"
            '未' -> "丁己"
            '申' -> "戊壬庚"
            '酉' -> "庚辛"
            '戌' -> "辛丁戊"
            '亥' -> "戊甲壬"
            else -> ""
        }
    }

    // 양음
    fun getSign(char: Char): Int {
        return if (char in "甲丙戊庚壬") 1 else -1
    }


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

        return if (dayIdx == -1) "" else timeGanji[dayIdx][timeIdx]
    }


    // 십이운성
    fun getTwelveShootingStar(day: Char, twelveGod: Char): String {

        val dayIdx = arrayOf('甲', '乙', '丙', '丁', '戊', '己', '庚', '辛', '壬', '癸').indexOf(day)
        val twelveGodIdx =
            arrayOf('亥', '子', '丑', '寅', '卯', '辰', '巳', '午', '未', '申', '酉', '戌').indexOf(twelveGod)

        return arrayOf(
            arrayOf(
                "生(생)",
                "死(사)",
                "絶(절)",
                "胎(태)",
                "絶(절)",
                "胎(태)",
                "病(병)",
                "浴(욕)",
                "官(관)",
                "旺(왕)",
            ),
            arrayOf(
                "浴(욕)",
                "病(병)",
                "胎(태)",
                "絶(절)",
                "胎(태)",
                "絶(절)",
                "死(사)",
                "生(생)",
                "旺(왕)",
                "官(관)",
            ),
            arrayOf(
                "帶(대)",
                "衰(쇠)",
                "養(양)",
                "墓(묘)",
                "養(양)",
                "墓(묘)",
                "墓(묘)",
                "養(양)",
                "衰(쇠)",
                "帶(대)",
            ),
            arrayOf(
                "官(관)",
                "旺(왕)",
                "生(생)",
                "死(사)",
                "生(생)",
                "死(사)",
                "絶(절)",
                "胎(태)",
                "病(병)",
                "浴(욕)",
            ),
            arrayOf(
                "旺(왕)",
                "官(관)",
                "浴(욕)",
                "病(병)",
                "浴(욕)",
                "病(병)",
                "胎(태)",
                "絶(절)",
                "死(사)",
                "生(생)",
            ),
            arrayOf(
                "衰(쇠)",
                "帶(대)",
                "帶(대)",
                "衰(쇠)",
                "帶(대)",
                "衰(쇠)",
                "養(양)",
                "墓(묘)",
                "墓(묘)",
                "養(양)",
            ),
            arrayOf(
                "病(병)",
                "浴(욕)",
                "官(관)",
                "旺(왕)",
                "官(관)",
                "旺(왕)",
                "生(생)",
                "死(사)",
                "絶(절)",
                "胎(태)",
            ),
            arrayOf(
                "死(사)",
                "生(생)",
                "旺(왕)",
                "官(관)",
                "旺(왕)",
                "官(관)",
                "浴(욕)",
                "病(병)",
                "胎(태)",
                "絶(절)",
            ),
            arrayOf(
                "墓(묘)",
                "養(양)",
                "衰(쇠)",
                "帶(대)",
                "衰(쇠)",
                "帶(대)",
                "帶(대)",
                "衰(쇠)",
                "養(양)",
                "墓(묘)",
            ),
            arrayOf(
                "絶(절)",
                "胎(태)",
                "病(병)",
                "浴(욕)",
                "病(병)",
                "浴(욕)",
                "官(관)",
                "旺(왕)",
                "生(생)",
                "死(사)",
            ),
            arrayOf(
                "胎(태)",
                "絶(절)",
                "死(사)",
                "生(생)",
                "死(사)",
                "生(생)",
                "旺(왕)",
                "官(관)",
                "浴(욕)",
                "病(병)",
            ),
            arrayOf(
                "養(양)",
                "墓(묘)",
                "墓(묘)",
                "養(양)",
                "墓(묘)",
                "養(양)",
                "衰(쇠)",
                "帶(대)",
                "帶(대)",
                "衰(쇠)",
            ),
        )[twelveGodIdx][dayIdx]
    }


    /*** 음력날짜를 양력날짜로 변환* @param 음력날짜 (yyyyMMdd)* @return 양력날짜 (yyyyMMdd) */
    fun convertLunarToSolar(date: String): Long {
        val nDate = date.replace("-", "")
        val cc = ChineseCalendar()
        val cal = Calendar.getInstance()
        cc[ChineseCalendar.EXTENDED_YEAR] = nDate.substring(0, 4).toInt() + 2637
        cc[ChineseCalendar.MONTH] = nDate.substring(4, 6).toInt() - 1
        cc[ChineseCalendar.DAY_OF_MONTH] = nDate.substring(6).toInt()
        cal.timeInMillis = cc.timeInMillis
        return cal.timeInMillis
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

    // 층과 각도가 주어짐
    fun calcBearing(layer: Int, degree: Int): String? {
        return when (layer) {
            // 층수가 0일 경우
            0 -> when (degree) {
                // 각도가 0 이상 60 미만
                in 0 until 60 -> "寅"
                // 각도가 60 이상 120 미만
                in 60 until 120 -> "酉"
                // 각도가 120 이상 180 미만
                in 120 until 180 -> "亥"
                // 각도가 180 이상 240 미만
                in 180 until 240 -> "卯"
                // 각도가 240 이상 300 미만
                in 240 until 300 -> "巳"
                // 그 밖의 범위. 300 이상
                else -> "午"
            }

            1 -> when (degree) {
                in 0 until 10 -> "艮"
                in 10 until 20 -> "" // 빈 칸일 경우
                in 20 until 30 -> "申癸"
                in 30 until 40 -> ""
                in 40 until 50 -> "艮"

                in 340 until 350 -> "壬申"
                else -> "乾"
            }

            2 -> when (degree) {
                in 100 until 200 -> "火"
                in 200 until 300 -> "金"
                // 만약 300 ~ 360(0) ~ 100 이라면
                else -> "水"
            }
            else -> ""
        }
    }
}