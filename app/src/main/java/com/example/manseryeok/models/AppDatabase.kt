package com.example.manseryeok.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.manseryeok.models.dao.group.GroupDAO
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.dao.UserDAO
import com.example.manseryeok.models.dao.group.UserGroupDAO
import com.example.manseryeok.models.dao.tag.TagDAO
import com.example.manseryeok.models.dao.tag.UserTagDAO
import com.example.manseryeok.models.user.groups.Group
import com.example.manseryeok.models.user.groups.UserGroup
import com.example.manseryeok.models.user.tags.Tag
import com.example.manseryeok.models.user.tags.UserTag

@Database(
    entities = [User::class, UserGroup::class, Group::class, Tag::class, UserTag::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO

    abstract fun userGroupDAO(): UserGroupDAO
    abstract fun groupDao(): GroupDAO

    abstract fun tagDao(): TagDAO
    abstract fun userTagDAO(): UserTagDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "app_database"
                ).build()
            }
            return INSTANCE as AppDatabase
        }
    }
}