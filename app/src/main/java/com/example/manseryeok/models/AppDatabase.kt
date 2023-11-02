package com.example.manseryeok.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.manseryeok.models.dao.GroupDAO
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.dao.UserDAO
import com.example.manseryeok.models.dao.UserGroupDAO
import com.example.manseryeok.models.user.Group
import com.example.manseryeok.models.user.UserGroup

@Database(entities = [User::class, UserGroup::class, Group::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun userGroupDAO(): UserGroupDAO
    abstract fun groupDao(): GroupDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
            }
            return INSTANCE as AppDatabase
        }
    }
}