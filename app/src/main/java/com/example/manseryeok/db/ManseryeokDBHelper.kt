package com.example.manseryeok.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import java.io.*
import java.sql.SQLException

class ManseryeokDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    private var mDataBase: SQLiteDatabase? = null
    private val mContext: Context
    @Throws(IOException::class)

    fun createDataBase() {
        //데이터베이스가 없으면 asset 폴더에서 복사해온다.
        val mDataBaseExist = checkDataBase()
        if (!mDataBaseExist) {
            this.readableDatabase
            close()
            try {
                //Copy the database from assets
                copyDataBase()
                Log.e(TAG, "createDatabase database created")
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db!!.disableWriteAheadLogging()
    }

    ///data/data/your package/databases/Da Name <-이 경로에서 데이터베이스가 존재하는지 확인한다
    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists()
    }

    //assets 폴더에서 데이터베이스를 복사한다.
    @Throws(IOException::class)
    private fun copyDataBase() {
        val mInput: InputStream = mContext.assets.open(DB_NAME)
        val outFileName = DB_PATH + DB_NAME
        val mOutput: OutputStream = FileOutputStream(outFileName)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) {
            mOutput.write(mBuffer, 0, mLength)
        }
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    //데이터베이스를 열어서 쿼리를 쓸수있게만든다.
    @Throws(SQLException::class)
    fun openDataBase(): Boolean {
        val mPath = DB_PATH + DB_NAME
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY)
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null
    }

    @Synchronized
    override fun close() {
        if (mDataBase != null) mDataBase!!.close()
        super.close()
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {}
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}

    companion object {
        private const val TAG = "DataBaseHelper" //Logcat에 출력할 태그이름

        // 디바이스 장치에서 데이터베이스의 경로
        // assets 폴더에 있는 경우 "", 그 외 경로기입
        private var DB_PATH = ""

        // assets 폴더에 있는 DB명 또는 별도의 데이터베이스 파일이름
        private const val DB_NAME = "Manseryeok.db"
    }

    init {
        DB_PATH =
            if (Build.VERSION.SDK_INT >= 17) context.applicationInfo.dataDir + "/databases/"
            else "/data/data/" + context.packageName + "/databases/"

        mContext = context
    }
}