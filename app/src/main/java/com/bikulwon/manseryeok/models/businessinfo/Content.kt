package com.bikulwon.manseryeok.models.businessinfo

data class Content(
    val id: String,
    val rich_text: List<RichText>,
    val type: String
)