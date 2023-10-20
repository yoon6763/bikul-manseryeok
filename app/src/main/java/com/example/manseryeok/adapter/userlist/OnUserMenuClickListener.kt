package com.example.manseryeok.adapter.userlist

interface OnUserMenuClickListener {
    fun onManseryeokView(id: Long, position: Int)
    fun onNameView(id: Long, position: Int)
    fun onDeleteClick(id: Long, position: Int)
    fun onGroupClick(id: Long, position: Int)
    fun onEditClick(id: Long, position: Int)
}