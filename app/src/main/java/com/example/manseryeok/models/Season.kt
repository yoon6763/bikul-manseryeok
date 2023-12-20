package com.example.manseryeok.models

// 한로, 입춘, 우수, 동지 등
data class Season(
    val name: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
)