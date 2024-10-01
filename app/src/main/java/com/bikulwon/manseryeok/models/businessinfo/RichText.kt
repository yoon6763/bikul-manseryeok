package com.bikulwon.manseryeok.models.businessinfo

data class RichText(
    val annotations: Annotations,
    val href: Any,
    val plain_text: String,
    val text: Text,
    val type: String
)