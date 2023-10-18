package com.example.manseryeok.adapter.userlist

import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.user.User

data class GroupItem(
    val groupName: String,
    val users: ArrayList<User>,
    val manseryeokList: ArrayList<Manseryeok>
)