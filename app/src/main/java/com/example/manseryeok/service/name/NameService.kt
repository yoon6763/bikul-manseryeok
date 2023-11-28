package com.example.manseryeok.service.name

import android.content.Context
import android.widget.Toast
import com.example.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.name.NameScoreChildItem
import com.example.manseryeok.models.name.NameScoreItem
import com.example.manseryeok.utils.Utils

class NameService(val context: Context, var name: String) {

    companion object {
        const val EVEN = 0
        const val ODD = 1

        const val YEAR = 0
        const val MONTH = 1
    }

    /*₩
        한글 유니코드 규칙 - (초성 * 21 + 중성) * 28 + 종성 + 0xAC00
        전 -> ㅈ : 12, ㅓ : 4 ㄴ : 5
        (12 * 21 + 4) * 28 + 5 + 0xAC00
    */

    private val nameComponents = ArrayList<NameCharComponent>()

    init {
        for (nameChar in name) {
            if (nameChar.code !in NameCharConstants.HANGLE_UNICODE_START..NameCharConstants.HANGLE_UNICODE_END) {
                Toast.makeText(context, "한글만 입력해주세요.", Toast.LENGTH_SHORT).show()
                break
            }
            addNameToComponents(nameChar)
        }
    }

    private fun addNameToComponents(nameChar: Char) {
        // 김
        val uniVal = nameChar - NameCharConstants.HANGLE_UNICODE_START

        val initialSound = NameCharConstants.initialSound[(uniVal.code / 28 / 21)] // ㄱ
        val middleSound = NameCharConstants.middleSound[(uniVal.code / 28) % 21] // ㅣ
        val finalSound = NameCharConstants.finalSound[uniVal.code % 28] // ㅁ

        nameComponents.add(NameCharComponent(nameChar, initialSound, middleSound, finalSound))
    }


    fun calcYearGanji(year: Int, month: Int, day: Int): List<NameScoreItem> {
        val manseryeok = importCalendar(year, month, day)
        val nameScoreItems = ArrayList<NameScoreItem>()
        val yearGanji = manseryeok.cd_hyganjee!!

        nameComponents.forEach { nameCharComponent ->
            val parity = nameCharComponent.score % 2 // 짝수 or 홀수

            val nameScoreItem = NameScoreItem(nameCharComponent.name)

            val initialGanji = getNameGanji(nameCharComponent.initialSound, parity)
            val initialGanjiYearTopLabel = Utils.getPillarLabel(initialGanji.toString(), yearGanji[0].toString())
            val initialGanjiYearBottomLabel = Utils.getPillarLabel(initialGanji.toString(), yearGanji[1].toString())

            nameScoreItem.nameScoreChildItems.add(NameScoreChildItem(initialGanji.toString(), initialGanjiYearTopLabel, initialGanjiYearBottomLabel,))

            if(nameCharComponent.finalSound != null &&
                nameCharComponent.finalSound != ' ') {
                val finalGanji = getNameGanji(nameCharComponent.finalSound, parity)
                val finialGanjiYearTopLabel = Utils.getPillarLabel(finalGanji.toString(), yearGanji[0].toString())
                val finialGanjiYearBottomLabel = Utils.getPillarLabel(finalGanji.toString(), yearGanji[1].toString())

                nameScoreItem.nameScoreChildItems.add(
                    NameScoreChildItem(
                        finalGanji.toString(),
                        finialGanjiYearTopLabel,
                        finialGanjiYearBottomLabel,
                    )
                )
            }

            nameScoreItems.add(nameScoreItem)
        }

        return nameScoreItems
    }

    fun calcMonthGanji(year: Int, month: Int, day: Int): List<NameScoreItem> {
        val manseryeok = importCalendar(year, month, day)
        val nameScoreItems = ArrayList<NameScoreItem>()
        val monthGanji = manseryeok.cd_hmganjee!!

        nameComponents.forEach { nameCharComponent ->
            val parity = nameCharComponent.score % 2 // 짝수 or 홀수

            val nameScoreItem = NameScoreItem(nameCharComponent.name)

            val initialGanji = getNameGanji(nameCharComponent.initialSound, parity)
            val initialGanjiYearTopLabel = Utils.getPillarLabel(initialGanji.toString(), monthGanji[0].toString())
            val initialGanjiYearBottomLabel = Utils.getPillarLabel(initialGanji.toString(), monthGanji[1].toString())

            nameScoreItem.nameScoreChildItems.add(NameScoreChildItem(initialGanji.toString(), initialGanjiYearTopLabel, initialGanjiYearBottomLabel,))

            if(nameCharComponent.finalSound != null &&
                nameCharComponent.finalSound != ' ') {
                val finalGanji = getNameGanji(nameCharComponent.finalSound, parity)
                val finialGanjiYearTopLabel = Utils.getPillarLabel(finalGanji.toString(), monthGanji[0].toString())
                val finialGanjiYearBottomLabel = Utils.getPillarLabel(finalGanji.toString(), monthGanji[1].toString())

                nameScoreItem.nameScoreChildItems.add(
                    NameScoreChildItem(
                        finalGanji.toString(),
                        finialGanjiYearTopLabel,
                        finialGanjiYearBottomLabel,
                    )
                )
            }

            nameScoreItems.add(nameScoreItem)
        }

        return nameScoreItems
    }


    private fun importCalendar(year: Int, month: Int, day: Int): Manseryeok {
        val dbHelper = ManseryeokSQLHelper(context)
        dbHelper.createDataBase()
        dbHelper.open()

        val userCalendar = dbHelper.getDayData(year, month, day)

        dbHelper.close()
        return userCalendar
    }

    private fun getProperty(sound: Char): Int {
        return when (sound) {
            'ㄱ', 'ㅋ' -> 0 // 목
            'ㄴ', 'ㄷ', 'ㄹ', 'ㅌ' -> 1 // 화
            'ㅇ', 'ㅎ' -> 2 // 토
            'ㅅ', 'ㅈ', 'ㅊ' -> 3 // 금
            'ㅁ', 'ㅂ', 'ㅍ' -> 4 // 수
            else -> -1
        }
    }

    private fun getNameGanji(sound: Char, parity: Int): Char = when (getProperty(sound)) {
        0 -> if (parity == ODD) '甲' else '乙'
        1 -> if (parity == ODD) '丙' else '丁'
        2 -> if (parity == ODD) '戊' else '己'
        3 -> if (parity == ODD) '庚' else '辛'
        4 -> if (parity == ODD) '壬' else '癸'
        else -> ' '
    }

}