package com.example.manseryeok.models.naversearch

data class NaverSearchResult(
    val display: Int,
    val items: List<NaverSearchItem>,
    val lastBuildDate: String,
    val start: Int,
    val total: Int
)