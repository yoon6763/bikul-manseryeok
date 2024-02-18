package com.bikulwon.manseryeok.adapter.userlist.item

import com.bikulwon.manseryeok.models.Manseryeok
import com.bikulwon.manseryeok.models.user.User
import com.bikulwon.manseryeok.models.user.tags.Tag

data class UserRVItem(
    val user: User,
    val manseryeok: Manseryeok,
    val tags: List<Tag>
)