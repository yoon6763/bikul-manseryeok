package com.bikulwon.manseryeok.models.businessinfo

data class Result(
    val archived: Boolean,
    val cover: Any,
    val created_by: CreatedBy,
    val created_time: String,
    val icon: Any,
    val id: String,
    val in_trash: Boolean,
    val last_edited_by: LastEditedBy,
    val last_edited_time: String,
    val `object`: String,
    val parent: Parent,
    val properties: Properties,
    val public_url: Any,
    val url: String
)