package com.bikulwon.manseryeok.models

import android.content.Context
import android.util.Log
import android.widget.Toast
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

            INSTANCE?.close()

            val result = try {
                val dbFile = context.getDatabasePath("app_database")
                val externalDir = context.getExternalFilesDir(null) ?: return null
                val backupFile = File(externalDir, "backup_app_database.db")

                dbFile.copyTo(backupFile, overwrite = true)
                backupFile
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                INSTANCE = getInstance(context)
            }

            return result
        }

        // 데이터베이스 복원
        fun restoreDatabase(context: Context, backupFile: File): Boolean {
            INSTANCE?.close()

            val result = try {
                val dbFile = context.getDatabasePath("app_database")
                backupFile.copyTo(dbFile, overwrite = true)
                true
            } catch (e: Exception) {
                Toast.makeText(context, "데이터 복원에 실패했습니다.", Toast.LENGTH_SHORT).show()
                // 실패 사유
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                Log.e("AppDatabase", "restoreDatabase: ${e.message}")
                e.printStackTrace()
                false
            } finally {
                INSTANCE = getInstance(context)
            }

            return result
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