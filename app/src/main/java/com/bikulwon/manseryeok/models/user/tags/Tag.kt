package com.bikulwon.manseryeok.models.user.tags

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true)
    var tagId: Long = 0,
    val name: String
)