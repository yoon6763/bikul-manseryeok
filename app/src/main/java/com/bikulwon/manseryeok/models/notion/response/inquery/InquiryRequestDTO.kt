package com.bikulwon.manseryeok.models.notion.response.inquery

data class InquiryRequestDTO(
    val children: List<Children>,
    val parent: Parent,
    val properties: Properties
)