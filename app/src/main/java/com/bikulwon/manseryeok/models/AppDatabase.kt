package com.bikulwon.manseryeok.models

import android.content.Context
import android.net.Uri
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
import java.net.URI
import java.net.URL

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
        fun restoreDatabase(context: Context, uri: Uri?) {
            if (uri == null) {
                Toast.makeText(context, "복원 중 에러가 발생하였습니다", Toast.LENGTH_SHORT).show()
                return
            }

            getInstance(context).close()
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream =
                FileOutputStream(File(context.getDatabasePath("app_database").absolutePath))
            inputStream.use { input ->
                outputStream.use { output ->
                    input?.copyTo(output)
                }
            }

            getInstance(context)
            Toast.makeText(context, "데이터 복원이 완료되었습니다", Toast.LENGTH_SHORT).show()
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