package com.bikulwon.manseryeok.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bikulwon.manseryeok.models.dao.group.GroupDAO
import com.bikulwon.manseryeok.models.user.User
import com.bikulwon.manseryeok.models.dao.UserDAO
import com.bikulwon.manseryeok.models.dao.group.UserGroupDAO
import com.bikulwon.manseryeok.models.dao.tag.TagDAO
import com.bikulwon.manseryeok.models.dao.tag.UserTagDAO
import com.bikulwon.manseryeok.models.user.groups.Group
import com.bikulwon.manseryeok.models.user.groups.UserGroup
import com.bikulwon.manseryeok.models.user.tags.Tag
import com.bikulwon.manseryeok.models.user.tags.UserTag
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

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

        // 데이터베이스 백업
        fun copyDatabaseToExternalStorage(context: Context): File? {
            return try {
                val dbFile = context.getDatabasePath("app_database")
                val externalDir = context.getExternalFilesDir(null) ?: return null
                val backupFile = File(externalDir, "backup_app_database.db")

                dbFile.copyTo(backupFile, overwrite = true)
                backupFile
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        // 데이터베이스 복원
        fun restoreDatabase(context: Context, backupFile: File): Boolean {
            val currentDBPath = context.getDatabasePath("app_database").absolutePath
            val backupFilePath = backupFile.absolutePath

            val src = FileInputStream(backupFilePath).channel
            val dst = FileOutputStream(currentDBPath).channel

            return try {
                // 기존 DB 파일 덮어쓰기
                dst.transferFrom(src, 0, src.size())
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } finally {
                src.close()
                dst.close()
            }
        }

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