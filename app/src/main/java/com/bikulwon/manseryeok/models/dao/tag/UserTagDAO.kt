package com.bikulwon.manseryeok.models.dao.tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bikulwon.manseryeok.models.user.User
import com.bikulwon.manseryeok.models.user.tags.Tag
import com.bikulwon.manseryeok.models.user.tags.UserTag

@Dao
interface UserTagDAO {
    @Insert
    fun insertUserTag(userTag: UserTag)

    @Delete
    fun deleteUserTag(userTag: UserTag)

    @Query("SELECT * FROM user WHERE userId IN (SELECT userId FROM usertag WHERE tagId = :tagId)")
    fun getUsersByTag(tagId: Long): List<User>

    @Query("SELECT * FROM tag WHERE tagId IN (SELECT tagId FROM usertag WHERE userId = :userId)")
    fun getTagsByUser(userId: Long): List<Tag>

    @Query("SELECT * FROM user WHERE userId NOT IN (SELECT userId FROM usertag)")
    fun getUsersWithoutTag(): List<User>

    @Query("SELECT * FROM user WHERE userId IN (SELECT userId FROM usertag WHERE tagId IN (:tagIds))")
    fun getUsersByTags(tagIds: List<Long>): List<User>
}
