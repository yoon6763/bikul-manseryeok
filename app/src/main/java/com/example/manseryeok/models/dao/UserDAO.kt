package com.example.manseryeok.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.manseryeok.models.user.User

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

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Long): User

    @Query("SELECT * FROM User WHERE firstName || ' ' || lastName LIKE '%' || :searchQuery || '%'")
    fun searchUserByName(searchQuery: String):List<User>

}