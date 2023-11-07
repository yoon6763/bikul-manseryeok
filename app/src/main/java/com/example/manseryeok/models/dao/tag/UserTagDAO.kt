package com.example.manseryeok.models.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.user.tags.UserTag

@Dao
interface UserTagDAO {
    @Insert
    fun insertUserTag(userTag: UserTag)

    @Delete
    fun deleteUserTag(userTag: UserTag)

    @Query("SELECT * FROM user WHERE userId IN (SELECT userId FROM usertag WHERE tagId = :tagId)")
    fun getUsersByTag(tagId: Long): List<User>

    @Query("SELECT * FROM usertag WHERE userId = :userId")
    fun getTagsByUser(userId: Long): List<UserTag>

    @Query("SELECT * FROM user WHERE userId NOT IN (SELECT userId FROM usertag)")
    fun getUsersWithoutTag(): List<User>
}
