package com.example.manseryeok.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.manseryeok.models.user.GroupTag

@Dao
interface GroupTagDAO {
    @Insert
    fun insertTag(tag: GroupTag): Long

    @Update
    fun updateTag(groupTag: GroupTag)

    @Delete
    fun deleteTag(tag: GroupTag)

    @Query("SELECT * FROM grouptag")
    fun getAllTags(): List<GroupTag>
}
