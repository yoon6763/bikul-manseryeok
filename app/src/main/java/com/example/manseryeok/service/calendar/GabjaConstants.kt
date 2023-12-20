package com.example.manseryeok.service.calendar

object GabjaConstants {

    val SIBGAN_HANGUL = arrayOf("갑", "을", "병", "정", "무", "기", "경", "신", "임", "계")
    val SIBGAN_HANJA = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    val SIBIJI_HANGUL = arrayOf("자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해")
    val SIBIJI_HANJA = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")

    fun getHangulToHanja(hangul: Char): Char {
        val hangulSibganIndex = SIBIJI_HANGUL.indexOf(hangul.toString())
        if (hangulSibganIndex != -1) return SIBIJI_HANJA[hangulSibganIndex][0]
        val hangulSibijiIndex = SIBGAN_HANGUL.indexOf(hangul.toString())
        if (hangulSibijiIndex != -1) return SIBGAN_HANJA[hangulSibijiIndex][0]
        return ' '
    }

    fun getHanjaToHangul(hanja: Char): Char {
        val hanjaSibganIndex = SIBIJI_HANJA.indexOf(hanja.toString())
        if (hanjaSibganIndex != -1) return SIBIJI_HANGUL[hanjaSibganIndex][0]
        val hanjaSibijiIndex = SIBGAN_HANJA.indexOf(hanja.toString())
        if (hanjaSibijiIndex != -1) return SIBGAN_HANGUL[hanjaSibijiIndex][0]
        return ' '
    }

    fun getHangulToHanja(hangul: String): String {
        var result = ""
        for (hangulChar in hangul) {
            result += getHangulToHanja(hangulChar)
        }
        return result
    }

    fun getHanjaToHangul(hanja: String): String {
        var result = ""
        for (hanjaChar in hanja) {
            result += getHanjaToHangul(hanjaChar)
        }
        return result
    }

}