package com.example.manseryeok.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.manseryeok.models.user.Group

@Dao
interface GroupDAO {
    @Insert
    fun insertGroup(group: Group): Long

    @Update
    fun updateGroup(group: Group)

    @Delete
    fun deleteGroup(group: Group)

    @Query("SELECT * FROM `group`")
    fun getAllGroups(): List<Group>
}
