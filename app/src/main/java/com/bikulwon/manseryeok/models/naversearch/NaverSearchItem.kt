package com.bikulwon.manseryeok.models.naversearch

data class NaverSearchItem(
    val address: String,
    val category: String,
    val description: String,
    val link: String,
    val mapx: String,
    val mapy: String,
    val roadAddress: String,
    val telephone: String,
    var title: String
)