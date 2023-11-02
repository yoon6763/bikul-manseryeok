package com.example.manseryeok.models.user.groups

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "groupId"])
data class UserGroup(
    val userId: Long,
    val groupId: Long
)