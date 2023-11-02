package com.example.manseryeok.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.user.UserGroup

@Dao
interface UserGroupDAO {
    @Insert
    fun insertUserGroup(userGroup: UserGroup)

    @Delete
    fun deleteUserGroup(userGroup: UserGroup)

    @Query("SELECT * FROM user WHERE id IN (SELECT userId FROM usergroup WHERE groupId = :groupId)")
    fun getUsersByGroup(groupId: Long): List<User>

    @Query("SELECT * FROM usergroup WHERE userId = :userId")
    fun getGroupsByUser(userId: Long): List<UserGroup>

    @Query("SELECT * FROM user WHERE id NOT IN (SELECT userId FROM usergroup)")
    fun getUsersWithoutGroup(): List<User>
}
