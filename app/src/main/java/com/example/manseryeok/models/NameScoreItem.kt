package com.example.manseryeok.models

data class NameScoreItem(
    val name: String,

    val nameHanInitial: String,
    val ganjiYearTopInitial: String,
    val ganjiYearBottomInitial: String,
    val ganjiMonthTopInitial: String,
    val ganjiMonthBottomInitial: String,

    val nameKorFinal: String? = null,
    val nameHanFinal: String? = null,
    val ganjiYearTopFinal: String? = null,
    val ganjiYearBottomFinal: String? = null,
    val ganjiMonthTopFinal: String? = null,
    val ganjiMonthBottomFinal: String? = null,
)
