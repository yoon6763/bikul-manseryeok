package com.example.manseryeok.adapter.userlist.item

import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.user.tags.Tag

data class UserRVItem(
    val user: User,
    val manseryeok: Manseryeok,
    val tags: List<Tag>
)