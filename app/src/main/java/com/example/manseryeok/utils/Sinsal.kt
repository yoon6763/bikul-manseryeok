package com.example.manseryeok.utils

object Sinsal {


    // 지지기준
    fun getYearBottom(year: Int, gender: Int, yearGanji: Char, ganji: Char): ArrayList<String> {

        val sinsalList = arrayOf(
            "德(복덕)",
            "紫微(자미)",
            "太陽(태양)",
            "太陰(태음)",
            "三台(삼태)",
            "八座(팔좌)",
            "天害(천해)",
            "地害(지해)",
            "玉堂(옥당)",
            "金匱(금궤)",
            "天馬(천마)",
            "月空(월공)",
            "劫殺(겁살)",
            "災殺(재살)",
            "天殺(천살)",
            "地殺(지살)",
            "年殺(년살)",
            "月殺(월살)",
            "亡身(망신)",
            "將星(장성)",
            "攀鞍(반안)",
            "驛馬(역마)",
            "六害(육해)",
            "華蓋(화개)",
            "孤神殺(고신살)", // 남자
            "寡宿殺(과숙살)", // 여자
            "帶耗殺(대모살)", // 유년운세
            "喪門殺(상문살)", // 유년운세
            "弔客殺(조객살)",  // 유년운세
            "三災八難(삼재팔난)" // 유년운세
        )

        val ganjiList = HashSet<Int>()

        when (yearGanji) {
            '子' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(0, 16))
                '亥' -> ganjiList.addAll(listOf(2, 18))
                '卯' -> ganjiList.addAll(listOf(3, 4, 22))
                '戌' -> ganjiList.addAll(listOf(5, 6, 17, 25, 28))
                '未' -> ganjiList.addAll(listOf(1, 7, 14))
                '子' -> ganjiList.addAll(listOf(9, 19))
                '寅' -> ganjiList.addAll(listOf(10, 21, 27))
                '午' -> ganjiList.addAll(listOf(11, 13, 26))
                '巳' -> ganjiList.addAll(listOf(12))
                '申' -> ganjiList.addAll(listOf(15))
                '丑' -> ganjiList.addAll(listOf(20))
                '辰' -> ganjiList.addAll(listOf(23))
            }

            '丑' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(6, 9, 19))
                '亥' -> ganjiList.addAll(listOf(5, 10, 21, 28))
                '卯' -> ganjiList.addAll(listOf(13, 27))
                '戌' -> ganjiList.addAll(listOf(0, 20, 25))
                '未' -> ganjiList.addAll(listOf(11, 17, 26))
                '子' -> ganjiList.addAll(listOf(22))
                '寅' -> ganjiList.addAll(listOf(2, 12, 24))
                '午' -> ganjiList.addAll(listOf(16))
                '巳' -> ganjiList.addAll(listOf(15))
                '申' -> ganjiList.addAll(listOf(1, 7, 18))
                '丑' -> ganjiList.addAll(listOf(23))
                '辰' -> ganjiList.addAll(listOf(3, 4, 14))
            }

            '寅' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(1, 7, 22))
                '亥' -> ganjiList.addAll(listOf(0, 12))
                '卯' -> ganjiList.addAll(listOf(2, 16))
                '戌' -> ganjiList.addAll(listOf(23))
                '未' -> ganjiList.addAll(listOf(20))
                '子' -> ganjiList.addAll(listOf(5, 13, 28))
                '寅' -> ganjiList.addAll(listOf(15))
                '午' -> ganjiList.addAll(listOf(9, 19))
                '巳' -> ganjiList.addAll(listOf(3, 4, 18, 24))
                '申' -> ganjiList.addAll(listOf(6, 10, 11, 21, 26))
                '丑' -> ganjiList.addAll(listOf(14, 25))
                '辰' -> ganjiList.addAll(listOf(17, 27))
            }

            '卯' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(11, 13, 26))
                '亥' -> ganjiList.addAll(listOf(1, 15))
                '卯' -> ganjiList.addAll(listOf(9, 19))
                '戌' -> ganjiList.addAll(listOf(7, 14))
                '未' -> ganjiList.addAll(listOf(6, 23))
                '子' -> ganjiList.addAll(listOf(0, 16))
                '寅' -> ganjiList.addAll(listOf(18))
                '午' -> ganjiList.addAll(listOf(3, 4, 22))
                '巳' -> ganjiList.addAll(listOf(10, 21, 24, 27))
                '申' -> ganjiList.addAll(listOf(12))
                '丑' -> ganjiList.addAll(listOf(5, 17, 25, 28))
                '辰' -> ganjiList.addAll(listOf(2, 24))
            }

            '辰' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(16))
                '亥' -> ganjiList.addAll(listOf(7, 18))
                '卯' -> ganjiList.addAll(listOf(22))
                '戌' -> ganjiList.addAll(listOf(10, 11, 17, 26))
                '未' -> ganjiList.addAll(listOf(3, 4, 14))
                '子' -> ganjiList.addAll(listOf(1, 9, 19))
                '寅' -> ganjiList.addAll(listOf(5, 21, 28))
                '午' -> ganjiList.addAll(listOf(6, 13, 27))
                '巳' -> ganjiList.addAll(listOf(2, 12, 24))
                '申' -> ganjiList.addAll(listOf(15))
                '丑' -> ganjiList.addAll(listOf(0, 20, 25))
                '辰' -> ganjiList.addAll(listOf(23))
            }

            '巳' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(9, 19))
                '亥' -> ganjiList.addAll(listOf(10, 11, 21, 26))
                '卯' -> ganjiList.addAll(listOf(5, 13, 28))
                '戌' -> ganjiList.addAll(listOf(20))
                '未' -> ganjiList.addAll(listOf(17, 27))
                '子' -> ganjiList.addAll(listOf(7, 8, 22))
                '寅' -> ganjiList.addAll(listOf(0, 12))
                '午' -> ganjiList.addAll(listOf(2, 16))
                '巳' -> ganjiList.addAll(listOf(6, 15))
                '申' -> ganjiList.addAll(listOf(3, 4, 18, 24))
                '丑' -> ganjiList.addAll(listOf(1, 23))
                '辰' -> ganjiList.addAll(listOf(14, 25))
            }

            '午' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(3, 4, 22))
                '亥' -> ganjiList.addAll(listOf(12))
                '卯' -> ganjiList.addAll(listOf(0, 16))
                '戌' -> ganjiList.addAll(listOf(23))
                '未' -> ganjiList.addAll(listOf(2, 20))
                '子' -> ganjiList.addAll(listOf(11, 13, 26))
                '寅' -> ganjiList.addAll(listOf(1, 15))
                '午' -> ganjiList.addAll(listOf(9, 19))
                '巳' -> ganjiList.addAll(listOf(18))
                '申' -> ganjiList.addAll(listOf(10, 21, 24, 27))
                '丑' -> ganjiList.addAll(listOf(7, 14))
                '辰' -> ganjiList.addAll(listOf(5, 6, 17, 25, 28))
            }

            '未' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(13, 27))
                '亥' -> ganjiList.addAll(listOf(15))
                '卯' -> ganjiList.addAll(listOf(1, 6, 9, 19))
                '戌' -> ganjiList.addAll(listOf(3, 4, 14))
                '未' -> ganjiList.addAll(listOf(23))
                '子' -> ganjiList.addAll(listOf(16))
                '寅' -> ganjiList.addAll(listOf(7, 18))
                '午' -> ganjiList.addAll(listOf(8, 22))
                '巳' -> ganjiList.addAll(listOf(5, 10, 21, 28))
                '申' -> ganjiList.addAll(listOf(2, 12, 24))
                '丑' -> ganjiList.addAll(listOf(11, 17, 26))
                '辰' -> ganjiList.addAll(listOf(0, 20, 25))
            }

            '申' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(2, 16))
                '亥' -> ganjiList.addAll(listOf(3, 4, 18, 24))
                '卯' -> ganjiList.addAll(listOf(7, 22))
                '戌' -> ganjiList.addAll(listOf(17, 27))
                '未' -> ganjiList.addAll(listOf(14, 25))
                '子' -> ganjiList.addAll(listOf(9, 19))
                '寅' -> ganjiList.addAll(listOf(6, 10, 11, 21))
                '午' -> ganjiList.addAll(listOf(5, 13, 28))
                '巳' -> ganjiList.addAll(listOf(0, 12))
                '申' -> ganjiList.addAll(listOf(15))
                '丑' -> ganjiList.addAll(listOf(20))
                '辰' -> ganjiList.addAll(listOf(1, 23))
            }

            '酉' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(9, 19))
                '亥' -> ganjiList.addAll(listOf(10, 21, 24, 27))
                '卯' -> ganjiList.addAll(listOf(11, 13, 26))
                '戌' -> ganjiList.addAll(listOf(2, 20))
                '未' -> ganjiList.addAll(listOf(5, 17, 25, 28))
                '子' -> ganjiList.addAll(listOf(3, 4, 22))
                '寅' -> ganjiList.addAll(listOf(8, 12))
                '午' -> ganjiList.addAll(listOf(0, 16))
                '巳' -> ganjiList.addAll(listOf(1, 15))
                '申' -> ganjiList.addAll(listOf(18))
                '丑' -> ganjiList.addAll(listOf(6, 23))
                '辰' -> ganjiList.addAll(listOf(7, 14))
            }

            '戌' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(22))
                '亥' -> ganjiList.addAll(listOf(2, 12, 24))
                '卯' -> ganjiList.addAll(listOf(16))
                '戌' -> ganjiList.addAll(listOf(23))
                '未' -> ganjiList.addAll(listOf(0, 20, 25))
                '子' -> ganjiList.addAll(listOf(6, 13, 27))
                '寅' -> ganjiList.addAll(listOf(15))
                '午' -> ganjiList.addAll(listOf(1, 9, 19))
                '巳' -> ganjiList.addAll(listOf(7, 18))
                '申' -> ganjiList.addAll(listOf(5, 10, 21, 28))
                '丑' -> ganjiList.addAll(listOf(3, 4, 14))
                '辰' -> ganjiList.addAll(listOf(11, 17, 26))
            }

            '亥' -> when (ganji) {
                '酉' -> ganjiList.addAll(listOf(5, 13, 28))
                '亥' -> ganjiList.addAll(listOf(6, 15))
                '卯' -> ganjiList.addAll(listOf(9, 19))
                '戌' -> ganjiList.addAll(listOf(14, 25))
                '未' -> ganjiList.addAll(listOf(1, 23))
                '子' -> ganjiList.addAll(listOf(2, 16))
                '寅' -> ganjiList.addAll(listOf(3, 4, 18, 24))
                '午' -> ganjiList.addAll(listOf(7, 22))
                '巳' -> ganjiList.addAll(listOf(10, 11, 21, 26))
                '申' -> ganjiList.addAll(listOf(0, 12))
                '丑' -> ganjiList.addAll(listOf(17, 27))
                '辰' -> ganjiList.addAll(listOf(20))
            }
        }

        if (gender == 0) ganjiList.remove(24) else ganjiList.remove(25)

        val res = ArrayList<String>()
        for (i in ganjiList) {
            res.add(sinsalList[i])
        }

        return res
    }
}