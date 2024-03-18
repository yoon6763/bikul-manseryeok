package com.bikulwon.manseryeok.models.user.tags

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "tagId"])
data class UserTag(
    val userId: Long,
    val tagId: Long
)