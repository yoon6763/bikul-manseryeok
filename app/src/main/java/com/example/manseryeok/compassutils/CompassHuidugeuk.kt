package com.example.manseryeok.compassutils

object CompassHuidugeuk {

    // 회두극 방향
    fun getDirection(year: Int): String {
        return when (year % 9) {
            0 -> "東"
            1 -> "南西"
            2 -> "北"
            3 -> "南東"
            4 -> "무관함"
            5 -> "北西"
            6 -> "西"
            7 -> "北東"
            8 -> "南"
            else -> "오류"
        }
    }

    fun getGenderNumber(year:Int, gender:Boolean) {

    }

}