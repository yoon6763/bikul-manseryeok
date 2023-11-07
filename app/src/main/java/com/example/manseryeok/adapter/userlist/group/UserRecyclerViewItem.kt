package com.example.manseryeok.adapter.userlist.group

import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.user.User

data class UserRecyclerViewItem(
    val groupName: String,
    val users: ArrayList<User>,
    val manseryeokList: ArrayList<Manseryeok>
)