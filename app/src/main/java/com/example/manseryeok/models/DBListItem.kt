package com.example.manseryeok.models

data class DBListItem(
    val id: String,
    val firstName: String,
    val lastName: String,
    val birth: String,
    val birthPlace: String,
    val timeDiff: Int,
    val gender: Int,
    val ganji: String
)