package com.example.manseryeok.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.manseryeok.Utils.Utils
import com.example.manseryeok.databinding.ActivityNameBinding
import com.example.manseryeok.models.User

class NameActivity : AppCompatActivity() {
    private val binding by lazy { ActivityNameBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

/*
        한글 유니코드 규칙 - (초성 * 21 + 중성) * 28 + 종성 + 0xAC00
        전 -> ㅈ : 12, ㅓ : 4 ㄴ : 5
        (12 * 21 + 4) * 28 + 5 + 0xAC00
     */

        // 초성
        val initialSound = arrayOf('ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')

        // 중성
        val middleSound = arrayOf('ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ')

        // 종성, 종성이 없는 경우도 있기에 "" 값 포함
        val finalSound = arrayOf(' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')

        val scoreHashMap = HashMap<Char, Int>()
        scoreHashMap[' '] = 0
        scoreHashMap['ㄱ'] = 2
        scoreHashMap['ㄲ'] = 4
        scoreHashMap['ㄴ'] = 2
        scoreHashMap['ㄷ'] = 3
        scoreHashMap['ㄸ'] = 6
        scoreHashMap['ㄹ'] = 5
        scoreHashMap['ㅁ'] = 4
        scoreHashMap['ㅂ'] = 4
        scoreHashMap['ㅃ'] = 8
        scoreHashMap['ㅅ'] = 2
        scoreHashMap['ㅆ'] = 4
        scoreHashMap['ㅇ'] = 1
        scoreHashMap['ㅈ'] = 3
        scoreHashMap['ㅉ'] = 6
        scoreHashMap['ㅊ'] = 4
        scoreHashMap['ㅋ'] = 3
        scoreHashMap['ㅌ'] = 4
        scoreHashMap['ㅍ'] = 4
        scoreHashMap['ㅎ'] = 3
        scoreHashMap['ㄳ'] = 4
        scoreHashMap['ㄵ'] = 5
        scoreHashMap['ㄶ'] = 5
        scoreHashMap['ㄺ'] = 7
        scoreHashMap['ㄻ'] = 9
        scoreHashMap['ㄼ'] = 9
        scoreHashMap['ㄽ'] = 7
        scoreHashMap['ㄾ'] = 9
        scoreHashMap['ㄿ'] = 9
        scoreHashMap['ㅀ'] = 8
        scoreHashMap['ㅄ'] = 6
        scoreHashMap['ㅏ'] = 2
        scoreHashMap['ㅐ'] = 3
        scoreHashMap['ㅑ'] = 3
        scoreHashMap['ㅒ'] = 4
        scoreHashMap['ㅓ'] = 2
        scoreHashMap['ㅔ'] = 3
        scoreHashMap['ㅕ'] = 3
        scoreHashMap['ㅖ'] = 4
        scoreHashMap['ㅗ'] = 2
        scoreHashMap['ㅘ'] = 4
        scoreHashMap['ㅙ'] = 5
        scoreHashMap['ㅚ'] = 3
        scoreHashMap['ㅛ'] = 3
        scoreHashMap['ㅜ'] = 2
        scoreHashMap['ㅝ'] = 4
        scoreHashMap['ㅞ'] = 5
        scoreHashMap['ㅟ'] = 3
        scoreHashMap['ㅠ'] = 3
        scoreHashMap['ㅡ'] = 1
        scoreHashMap['ㅢ'] = 2
        scoreHashMap['ㅣ'] = 1


        val userModel = intent.getParcelableExtra<User>(Utils.INTENT_EXTRAS_USER)!!

        val firstName = userModel.firstName
        val lastName = userModel.lastName

        val name = firstName + lastName

        for(element in name) {
            if (element.code !in 0xAC00..0xD79F) {
                println("한글이 아닙니다.")
                continue
            }

            val uniVal = element - 0xAC00

            val initialVal = initialSound[(uniVal.code / 28 / 21)]
            val middleVal = middleSound[(uniVal.code / 28 % 21)]
            val finalVal = finalSound[(uniVal.code % 28)]

            println("$initialVal : ${scoreHashMap[initialVal]}\t $middleVal : ${scoreHashMap[middleVal]}\t $finalVal : ${scoreHashMap[finalVal]}")
        }
    }
}