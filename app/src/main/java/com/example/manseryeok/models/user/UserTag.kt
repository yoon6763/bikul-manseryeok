package com.example.manseryeok.models.user

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "tagId"])
data class UserTag(
    val userId: Long,
    val tagId: Long
)