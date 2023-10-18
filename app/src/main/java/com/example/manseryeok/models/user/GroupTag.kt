package com.example.manseryeok.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupTag(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val name: String
)