package com.bikulwon.manseryeok.utils

object Sinsal {

    enum class Type {
        YEAR, MONTH, DAY, TIME
    }

    // 지지기준
    fun getYearBottom(year: Int, gender: Int, yearGanji: Char, ganji: Char): ArrayList<String> {

        val sinsalList = arrayOf(
            "德\n(복덕)",
            "紫微\n(자미)",
            "太陽\n(태양)",
            "太陰\n(태음)",
            "三台\n(삼태)",
            "八座\n(팔좌)",
            "天害\n(천해)",
            "地害\n(지해)",
            "玉堂\n(옥당)",
            "金匱\n(금궤)",
            "天馬\n(천마)",
            "月空\n(월공)",
            "劫殺\n(겁살)",
            "災殺\n(재살)",
            "天殺\n(천살)",
            "地殺\n(지살)",
            "年殺\n(년살)",
            "月殺\n(월살)",
            "亡身\n(망신)",
            "將星\n(장성)",
            "攀鞍\n(반안)",
            "驛馬\n(역마)",
            "六害\n(육해)",
            "華蓋\n(화개)",
            "孤神殺\n(고신살)", // 남자
            "寡宿殺\n(과숙살)", // 여자
            "帶耗殺\n(대모살)", // 유년운세
            "喪門殺\n(상문살)", // 유년운세
            "弔客殺\n(조객살)",  // 유년운세
            "三災八難\n(삼재팔난)" // 유년운세
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

    fun getMonthBottom(type: Type, monthGanji: Char, ganji: String): ArrayList<String> {
        val labelList = arrayOf(
            "月德貴人\n(월덕귀인)",
            "紅鸞星\n(홍란성)",
            "皇恩大赦\n(황은대사)",
            "天喜神\n(천희신)",
            "天醫星\n(천의성)",
            "急脚殺\n(급각살)",
            "斷橋關殺\n(단교관살)",
            "斧壁殺\n(부벽살)",
            "進神\n(진신)",
            "天赦星\n(천사성)",
            "天轉殺\n(천전살)",
            "地轉殺\n(지전살)",
        )

        val sinsalList = HashSet<Int>()

        when (monthGanji) {
            '子' -> {
                when (ganji[1]) {
                    '壬' -> sinsalList.addAll(listOf(0))
                    '卯' -> sinsalList.addAll(listOf(1))
                    '申' -> sinsalList.addAll(listOf(2))
                    '酉' -> sinsalList.addAll(listOf(3))
                    '亥' -> sinsalList.addAll(listOf(4))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '丑' -> sinsalList.addAll(listOf(5))
                        '辰' -> sinsalList.addAll(listOf(5))
                        '亥' -> sinsalList.addAll(listOf(6))
                        '巳' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "己酉" -> sinsalList.addAll(listOf(8))
                        "甲子" -> sinsalList.addAll(listOf(9))
                        "壬子" -> sinsalList.addAll(listOf(10))
                        "丙子" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '丑' -> {
                when (ganji[1]) {
                    '庚' -> sinsalList.addAll(listOf(0))
                    '寅' -> sinsalList.addAll(listOf(1))
                    '未' -> sinsalList.addAll(listOf(2))
                    '申' -> sinsalList.addAll(listOf(3))
                    '子' -> sinsalList.addAll(listOf(4))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '丑' -> sinsalList.addAll(listOf(5, 7))
                        '辰' -> sinsalList.addAll(listOf(5))
                        '子' -> sinsalList.addAll(listOf(6))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "己酉" -> sinsalList.addAll(listOf(8))
                        "甲子" -> sinsalList.addAll(listOf(9))
                        "壬子" -> sinsalList.addAll(listOf(10))
                        "丙子" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '寅' -> {
                when (ganji[1]) {
                    '丙' -> sinsalList.addAll(listOf(0))
                    '丑' -> sinsalList.addAll(listOf(1, 4))
                    '戌' -> sinsalList.addAll(listOf(2))
                    '未' -> sinsalList.addAll(listOf(3))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '亥' -> sinsalList.addAll(listOf(5))
                        '子' -> sinsalList.addAll(listOf(5))
                        '寅' -> sinsalList.addAll(listOf(6))
                        '酉' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "甲子" -> sinsalList.addAll(listOf(8))
                        "戊寅" -> sinsalList.addAll(listOf(9))
                        "乙卯" -> sinsalList.addAll(listOf(10))
                        "辛卯" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '卯' -> {
                when (ganji[1]) {
                    '甲' -> sinsalList.addAll(listOf(0))
                    '子' -> sinsalList.addAll(listOf(1))
                    '丑' -> sinsalList.addAll(listOf(2))
                    '午' -> sinsalList.addAll(listOf(3))
                    '寅' -> sinsalList.addAll(listOf(4))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '亥' -> sinsalList.addAll(listOf(5))
                        '子' -> sinsalList.addAll(listOf(5))
                        '卯' -> sinsalList.addAll(listOf(6))
                        '巳' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "甲子" -> sinsalList.addAll(listOf(8))
                        "戊寅" -> sinsalList.addAll(listOf(9))
                        "乙卯" -> sinsalList.addAll(listOf(10))
                        "辛卯" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '辰' -> {
                when (ganji[1]) {
                    '壬' -> sinsalList.addAll(listOf(0))
                    '亥' -> sinsalList.addAll(listOf(1))
                    '寅' -> sinsalList.addAll(listOf(2))
                    '巳' -> sinsalList.addAll(listOf(3))
                    '卯' -> sinsalList.addAll(listOf(4))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '亥' -> sinsalList.addAll(listOf(5))
                        '子' -> sinsalList.addAll(listOf(5))
                        '申' -> sinsalList.addAll(listOf(6))
                        '丑' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "甲子" -> sinsalList.addAll(listOf(8))
                        "戊寅" -> sinsalList.addAll(listOf(9))
                        "乙卯" -> sinsalList.addAll(listOf(10))
                        "辛卯" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '巳' -> {
                when (ganji[1]) {
                    '庚' -> sinsalList.addAll(listOf(0, 4))
                    '戌' -> sinsalList.addAll(listOf(1))
                    '巳' -> sinsalList.addAll(listOf(2))
                    '辰' -> sinsalList.addAll(listOf(3))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '卯' -> sinsalList.addAll(listOf(5))
                        '未' -> sinsalList.addAll(listOf(5))
                        '丑' -> sinsalList.addAll(listOf(6))
                        '酉' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "甲午" -> sinsalList.addAll(listOf(8, 9))
                        "丙午" -> sinsalList.addAll(listOf(10))
                        "戊午" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '午' -> {
                when (ganji[1]) {
                    '丙' -> sinsalList.addAll(listOf(0))
                    '酉' -> sinsalList.addAll(listOf(1, 2))
                    '卯' -> sinsalList.addAll(listOf(3))
                    '巳' -> sinsalList.addAll(listOf(4))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '卯' -> sinsalList.addAll(listOf(5))
                        '未' -> sinsalList.addAll(listOf(5))
                        '戌' -> sinsalList.addAll(listOf(6))
                        '巳' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "甲午" -> sinsalList.addAll(listOf(8, 9))
                        "丙午" -> sinsalList.addAll(listOf(10))
                        "戊午" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '未' -> {
                when (ganji[1]) {
                    '甲' -> sinsalList.addAll(listOf(0))
                    '申' -> sinsalList.addAll(listOf(1))
                    '卯' -> sinsalList.addAll(listOf(2))
                    '寅' -> sinsalList.addAll(listOf(3))
                    '午' -> sinsalList.addAll(listOf(4))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '卯' -> sinsalList.addAll(listOf(5))
                        '未' -> sinsalList.addAll(listOf(5))
                        '酉' -> sinsalList.addAll(listOf(6))
                        '丑' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "甲午" -> sinsalList.addAll(listOf(8, 9))
                        "丙午" -> sinsalList.addAll(listOf(10))
                        "戊午" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '申' -> {
                when (ganji[1]) {
                    '壬' -> sinsalList.addAll(listOf(0))
                    '未' -> sinsalList.addAll(listOf(1, 4))
                    '子' -> sinsalList.addAll(listOf(2))
                    '丑' -> sinsalList.addAll(listOf(3))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '寅' -> sinsalList.addAll(listOf(5))
                        '戌' -> sinsalList.addAll(listOf(5))
                        '辰' -> sinsalList.addAll(listOf(6))
                        '酉' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "己卯" -> sinsalList.addAll(listOf(8))
                        "戊申" -> sinsalList.addAll(listOf(9))
                        "辛酉" -> sinsalList.addAll(listOf(10))
                        "癸酉" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '酉' -> {
                when (ganji[1]) {
                    '庚' -> sinsalList.addAll(listOf(0))
                    '午' -> sinsalList.addAll(listOf(1, 2))
                    '子' -> sinsalList.addAll(listOf(3))
                    '申' -> sinsalList.addAll(listOf(4))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '寅' -> sinsalList.addAll(listOf(5))
                        '戌' -> sinsalList.addAll(listOf(5))
                        '巳' -> sinsalList.addAll(listOf(6, 7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "己卯" -> sinsalList.addAll(listOf(8))
                        "戊申" -> sinsalList.addAll(listOf(9))
                        "辛酉" -> sinsalList.addAll(listOf(10))
                        "癸酉" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '戌' -> {
                when (ganji[1]) {
                    '丙' -> sinsalList.addAll(listOf(0))
                    '巳' -> sinsalList.addAll(listOf(1))
                    '亥' -> sinsalList.addAll(listOf(2, 3))
                    '酉' -> sinsalList.addAll(listOf(4))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '寅' -> sinsalList.addAll(listOf(5))
                        '戌' -> sinsalList.addAll(listOf(5))
                        '午' -> sinsalList.addAll(listOf(6))
                        '丑' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "己卯" -> sinsalList.addAll(listOf(8))
                        "戊申" -> sinsalList.addAll(listOf(9))
                        "辛酉" -> sinsalList.addAll(listOf(10))
                        "癸酉" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

            '亥' -> {
                when (ganji[1]) {
                    '甲' -> sinsalList.addAll(listOf(0))
                    '辰' -> sinsalList.addAll(listOf(1, 2))
                    '戌' -> sinsalList.addAll(listOf(3, 4))
                }

                if (type == Type.DAY || type == Type.TIME) {
                    when (ganji[1]) {
                        '丑' -> sinsalList.addAll(listOf(5))
                        '辰' -> sinsalList.addAll(listOf(5))
                        '未' -> sinsalList.addAll(listOf(6))
                        '酉' -> sinsalList.addAll(listOf(7))
                    }
                }

                if (type == Type.DAY) {
                    when (ganji) {
                        "己酉" -> sinsalList.addAll(listOf(8))
                        "甲子" -> sinsalList.addAll(listOf(9))
                        "壬子" -> sinsalList.addAll(listOf(10))
                        "丙子" -> sinsalList.addAll(listOf(11))
                    }
                }
            }

        }

        val res = ArrayList<String>()
        for (i in sinsalList) {
            res.add(labelList[i])
        }

        return res
    }


    fun getDayBottom(time: Char, ganji: String): ArrayList<String> {
        val labelList = arrayOf(
            "劫殺\n(겁살)",
            "災殺\n(재살)",
            "天殺\n(천살)",
            "地殺\n(지살)",
            "年殺\n(년살)",
            "月殺\n(월살)",
            "亡身\n(망신)",
            "將星\n(장성)",
            "攀鞍\n(반안)",
            "驛馬\n(역마)",
            "六害\n(육해)",
            "華蓋\n(화개)",
            "湯火殺\n(탕화살)",
            "天羅蜘網殺\n(천라지망살)",
            "鬼門關殺\n(귀문관살)",
            "鐵鎖開金星\n(철쇄개금성)",
            "三刑殺\n(삼형살)",
            "相形殺\n(상형살)",
            "自形殺\n(자형살)",
            "破殺\n(파살)",
            "害殺\n(해살)",
            "怨嗔殺\n(원진살)",
        )

        val set = HashSet<Int>()

        when (time) {
            '子' -> when (ganji[1]) {
                '巳' -> set.addAll(listOf(0))
                '午' -> set.addAll(listOf(1))
                '未' -> set.addAll(listOf(2, 20, 21))
                '申' -> set.addAll(listOf(3))
                '酉' -> set.addAll(listOf(4, 14, 19))
                '戌' -> set.addAll(listOf(5))
                '亥' -> set.addAll(listOf(6))
                '子' -> set.addAll(listOf(7))
                '丑' -> set.addAll(listOf(8))
                '寅' -> set.addAll(listOf(9))
                '卯' -> set.addAll(listOf(10, 17))
                '辰' -> set.addAll(listOf(11))
            }

            '丑' -> when (ganji[1]) {
                '寅' -> set.addAll(listOf(0))
                '卯' -> set.addAll(listOf(1))
                '辰' -> set.addAll(listOf(2, 19))
                '巳' -> set.addAll(listOf(3))
                '午' -> set.addAll(listOf(4, 12, 14, 16, 19, 20, 21))
                '未' -> set.addAll(listOf(5, 12))
                '申' -> set.addAll(listOf(6))
                '酉' -> set.addAll(listOf(7))
                '戌' -> set.addAll(listOf(8, 12, 16))
                '亥' -> set.addAll(listOf(9))
                '子' -> set.addAll(listOf(10))
                '丑' -> set.addAll(listOf(11))
            }

            '寅' -> when (ganji[1]) {
                '亥' -> set.addAll(listOf(0, 19))
                '子' -> set.addAll(listOf(1))
                '丑' -> set.addAll(listOf(2))
                '寅' -> set.addAll(listOf(3, 12))
                '卯' -> set.addAll(listOf(4))
                '辰' -> set.addAll(listOf(5))
                '巳' -> set.addAll(listOf(6, 12, 16, 20))
                '午' -> set.addAll(listOf(7))
                '未' -> set.addAll(listOf(8, 14))
                '申' -> set.addAll(listOf(9, 12, 16))
                '酉' -> set.addAll(listOf(10, 21))
                '戌' -> set.addAll(listOf(11))
            }

            '卯' -> when (ganji[1]) {
                '申' -> set.addAll(listOf(0, 14, 21))
                '酉' -> set.addAll(listOf(1, 15))
                '戌' -> set.addAll(listOf(2, 15))
                '亥' -> set.addAll(listOf(3))
                '子' -> set.addAll(listOf(4, 17))
                '丑' -> set.addAll(listOf(5))
                '寅' -> set.addAll(listOf(6))
                '卯' -> set.addAll(listOf(7, 15))
                '辰' -> set.addAll(listOf(8, 20))
                '巳' -> set.addAll(listOf(9))
                '午' -> set.addAll(listOf(10, 19))
                '未' -> set.addAll(listOf(11))
            }

            '辰' -> when (ganji[1]) {
                '巳' -> set.addAll(listOf(0, 13))
                '午' -> set.addAll(listOf(1))
                '未' -> set.addAll(listOf(2))
                '申' -> set.addAll(listOf(3))
                '酉' -> set.addAll(listOf(4))
                '戌' -> set.addAll(listOf(5, 13))
                '亥' -> set.addAll(listOf(6, 13, 14, 21))
                '子' -> set.addAll(listOf(7))
                '丑' -> set.addAll(listOf(8, 19))
                '寅' -> set.addAll(listOf(9))
                '卯' -> set.addAll(listOf(10, 20))
                '辰' -> set.addAll(listOf(11, 13, 18))
            }

            '巳' -> when (ganji[1]) {
                '寅' -> set.addAll(listOf(0, 16, 20))
                '卯' -> set.addAll(listOf(1))
                '辰' -> set.addAll(listOf(2, 13))
                '巳' -> set.addAll(listOf(3, 13))
                '午' -> set.addAll(listOf(4))
                '未' -> set.addAll(listOf(5))
                '申' -> set.addAll(listOf(6, 16, 19))
                '酉' -> set.addAll(listOf(7))
                '戌' -> set.addAll(listOf(8, 13, 14, 21))
                '亥' -> set.addAll(listOf(9, 13))
                '子' -> set.addAll(listOf(10))
                '丑' -> set.addAll(listOf(11))
            }

            '午' -> when (ganji[1]) {
                '亥' -> set.addAll(listOf(0))
                '子' -> set.addAll(listOf(1))
                '丑' -> set.addAll(listOf(2, 12, 14, 20, 21))
                '寅' -> set.addAll(listOf(3))
                '卯' -> set.addAll(listOf(4, 19))
                '辰' -> set.addAll(listOf(5, 12))
                '巳' -> set.addAll(listOf(6))
                '午' -> set.addAll(listOf(7, 12, 18))
                '未' -> set.addAll(listOf(8))
                '申' -> set.addAll(listOf(9))
                '酉' -> set.addAll(listOf(10))
                '戌' -> set.addAll(listOf(11))
            }

            '未' -> when (ganji[1]) {
                '申' -> set.addAll(listOf(0))
                '酉' -> set.addAll(listOf(1))
                '戌' -> set.addAll(listOf(2, 16, 19))
                '亥' -> set.addAll(listOf(3))
                '子' -> set.addAll(listOf(4, 20, 21))
                '丑' -> set.addAll(listOf(5, 16))
                '寅' -> set.addAll(listOf(6, 14))
                '卯' -> set.addAll(listOf(7))
                '辰' -> set.addAll(listOf(8))
                '巳' -> set.addAll(listOf(9))
                '午' -> set.addAll(listOf(10))
                '未' -> set.addAll(listOf(11))
            }

            '申' -> when (ganji[1]) {
                '巳' -> set.addAll(listOf(0, 16, 19))
                '午' -> set.addAll(listOf(1))
                '未' -> set.addAll(listOf(2))
                '申' -> set.addAll(listOf(3))
                '酉' -> set.addAll(listOf(4))
                '戌' -> set.addAll(listOf(5))
                '亥' -> set.addAll(listOf(6, 20))
                '子' -> set.addAll(listOf(7))
                '丑' -> set.addAll(listOf(8))
                '寅' -> set.addAll(listOf(9, 16))
                '卯' -> set.addAll(listOf(10, 14, 21))
                '辰' -> set.addAll(listOf(11))
            }

            '酉' -> when (ganji[1]) {
                '寅' -> set.addAll(listOf(0, 21))
                '卯' -> set.addAll(listOf(1, 15))
                '辰' -> set.addAll(listOf(2))
                '巳' -> set.addAll(listOf(3))
                '午' -> set.addAll(listOf(4))
                '未' -> set.addAll(listOf(5))
                '申' -> set.addAll(listOf(6))
                '酉' -> set.addAll(listOf(7, 15, 18))
                '戌' -> set.addAll(listOf(8, 15, 20))
                '亥' -> set.addAll(listOf(9))
                '子' -> set.addAll(listOf(10, 11, 19))
                '丑' -> set.addAll(listOf(11))
            }

            '戌' -> when (ganji[1]) {
                '亥' -> set.addAll(listOf(0, 13))
                '子' -> set.addAll(listOf(1))
                '丑' -> set.addAll(listOf(2, 16))
                '寅' -> set.addAll(listOf(3))
                '卯' -> set.addAll(listOf(4, 15))
                '辰' -> set.addAll(listOf(5, 13))
                '巳' -> set.addAll(listOf(6, 13, 14, 21))
                '午' -> set.addAll(listOf(7))
                '未' -> set.addAll(listOf(8, 16, 19))
                '申' -> set.addAll(listOf(9))
                '酉' -> set.addAll(listOf(10, 15, 20))
                '戌' -> set.addAll(listOf(11, 13, 15))
            }

            '亥' -> when (ganji[1]) {
                '申' -> set.addAll(listOf(0, 20))
                '酉' -> set.addAll(listOf(1))
                '戌' -> set.addAll(listOf(2, 13))
                '亥' -> set.addAll(listOf(3, 13, 18))
                '子' -> set.addAll(listOf(4))
                '丑' -> set.addAll(listOf(5))
                '寅' -> set.addAll(listOf(6, 19))
                '卯' -> set.addAll(listOf(7))
                '辰' -> set.addAll(listOf(8, 13, 14, 21))
                '巳' -> set.addAll(listOf(9, 13))
                '午' -> set.addAll(listOf(10))
                '未' -> set.addAll(listOf(11))
            }
        }

        val res = ArrayList<String>()
        for (i in set) {
            res.add(labelList[i])
        }
        return res
    }
}