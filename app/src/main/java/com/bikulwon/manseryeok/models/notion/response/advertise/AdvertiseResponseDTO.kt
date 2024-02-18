package com.bikulwon.manseryeok.models.notion.response.advertise

data class AdvertiseResponseDTO(
    val developer_survey: String,
    val has_more: Boolean,
    val next_cursor: Any,
    val `object`: String,
    val page_or_database: PageOrDatabase,
    val request_id: String,
    val results: List<Result>,
    val type: String
)