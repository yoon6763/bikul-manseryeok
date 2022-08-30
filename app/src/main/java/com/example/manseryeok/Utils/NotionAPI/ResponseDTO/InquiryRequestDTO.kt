package com.example.manseryeok.Utils.NotionAPI.ResponseDTO

data class InquiryRequestDTO(
    val children: List<Children>,
    val parent: Parent,
    val properties: Properties
)