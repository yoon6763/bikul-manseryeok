package com.example.manseryeok.models.user.tags

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val name: String
)