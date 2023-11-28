package com.example.manseryeok.service.name

data class NameCharComponent(
    val name: Char, // 이름  ex) 김
    val initialSound: Char, // 초성 ex) ㄱ
    val middleSound: Char, // 중성 ex) ㅣ
    val finalSound: Char? = null, // 종성 ex) ㅁ
) {
    val score by lazy {
        var score = 0
        score += NameCharConstants.scoreHashMap[initialSound]!!
        score += NameCharConstants.scoreHashMap[middleSound]!!
        if (finalSound != null) {
            score += NameCharConstants.scoreHashMap[finalSound]!!
        }
        score
    }
}