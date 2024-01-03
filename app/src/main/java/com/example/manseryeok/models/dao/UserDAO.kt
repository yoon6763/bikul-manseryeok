package com.example.manseryeok.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.user.join.UserWithGroupAndTag

@Dao
interface UserDAO {

    @Insert
    fun insert(user: User): Long

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user")
    fun getAllUser(): List<User>

    @Query("SELECT * FROM user WHERE userId = :id")
    fun getUser(id: Long): User

    // name에 searchQuery가 포함되어 있는 user를 검색
    @Query("SELECT * FROM user WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchUserByName(searchQuery: String): List<User>

    @Query("SELECT * FROM user")
    fun getAllUserGroupTag(): List<UserWithGroupAndTag>

    @Query("SELECT * FROM user WHERE userId = :id")
    fun getUserGroupTag(id: Long): UserWithGroupAndTag

}