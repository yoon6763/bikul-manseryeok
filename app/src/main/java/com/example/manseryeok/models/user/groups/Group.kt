package com.example.manseryeok.models.user.groups

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val name: String
)