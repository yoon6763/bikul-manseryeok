package com.example.manseryeok.service.name

object NameCharConstants {

     const val HANGLE_UNICODE_START = 0xAC00
     const val HANGLE_UNICODE_END = 0xD79F

    // 초성
     val initialSound = arrayOf(
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
        'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
        'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
        'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )


    // 중성
     val middleSound = arrayOf(
        'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ',
        'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
        'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ',
        'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ',
        'ㅣ'
    )

    // 종성, 종성이 없는 경우도 있기에 "" 값 포함
    val finalSound = arrayOf(
        ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ',
        'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ',
        'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ',
        'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ',
        'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ',
        'ㅌ', 'ㅍ', 'ㅎ'
    )

    // 획수
    val scoreHashMap = hashMapOf(
        ' ' to 0, ' ' to 0, 'ㄱ' to 1, 'ㄲ' to 2, 'ㄴ' to 1,
        'ㄷ' to 2, 'ㄸ' to 4, 'ㄹ' to 3, 'ㅁ' to 3, 'ㅂ' to 4,
        'ㅃ' to 8, 'ㅅ' to 2, 'ㅆ' to 4, 'ㅇ' to 1, 'ㅈ' to 3,
        'ㅉ' to 6, 'ㅊ' to 4, 'ㅋ' to 2, 'ㅌ' to 3, 'ㅍ' to 4,
        'ㅎ' to 3, 'ㄳ' to 3, 'ㄵ' to 4, 'ㄶ' to 4, 'ㄺ' to 4,
        'ㄻ' to 7, 'ㄼ' to 7, 'ㄽ' to 5, 'ㄾ' to 6, 'ㄿ' to 7,
        'ㅀ' to 6, 'ㅄ' to 6, 'ㅏ' to 2, 'ㅐ' to 3, 'ㅑ' to 3,
        'ㅒ' to 4, 'ㅓ' to 2, 'ㅔ' to 3, 'ㅕ' to 3, 'ㅖ' to 4,
        'ㅗ' to 2, 'ㅘ' to 4, 'ㅙ' to 5, 'ㅚ' to 3, 'ㅛ' to 3,
        'ㅜ' to 2, 'ㅝ' to 4, 'ㅞ' to 5, 'ㅟ' to 3, 'ㅠ' to 3,
        'ㅡ' to 1,    'ㅢ' to 2, 'ㅣ' to 1,
    )
}