package com.bikulwon.manseryeok.models.name

data class NameScoreItem(
    val name: Char,
    val nameScoreChildItems: ArrayList<NameScoreChildItem> = ArrayList(),
)
