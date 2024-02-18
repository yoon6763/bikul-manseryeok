package com.bikulwon.manseryeok.models.dao.group

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bikulwon.manseryeok.models.user.User
import com.bikulwon.manseryeok.models.user.groups.UserGroup
import com.bikulwon.manseryeok.models.user.join.UserWithTags

@Dao
interface UserGroupDAO {
    @Insert
    fun insertUserGroup(userGroup: UserGroup)

    @Delete
    fun deleteUserGroup(userGroup: UserGroup)

    @Query("SELECT * FROM user WHERE userId IN (SELECT userId FROM usergroup WHERE groupId = :groupId)")
    fun getUsersByGroup(groupId: Long): List<User>

    @Query("SELECT * FROM user WHERE userId IN (SELECT userId FROM usergroup WHERE groupId = :groupId)")
    fun getUserWithTagsByGroup(groupId: Long): List<UserWithTags>

    @Query("SELECT * FROM usergroup WHERE userId = :userId")
    fun getGroupsByUser(userId: Long): List<UserGroup>

    @Query("SELECT * FROM user WHERE userId NOT IN (SELECT userId FROM usergroup)")
    fun getUsersWithoutGroup(): List<User>
}
