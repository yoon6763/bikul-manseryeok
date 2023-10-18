package com.example.manseryeok.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.manseryeok.models.dao.GroupTagDAO
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.dao.UserDAO
import com.example.manseryeok.models.dao.UserTagDAO
import com.example.manseryeok.models.user.GroupTag
import com.example.manseryeok.models.user.UserTag

@Database(entities = [User::class, UserTag::class, GroupTag::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun userTagDao(): UserTagDAO
    abstract fun groupTagDao(): GroupTagDAO

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