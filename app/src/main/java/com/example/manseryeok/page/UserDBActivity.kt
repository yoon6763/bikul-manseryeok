package com.example.manseryeok.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.manseryeok.R
import com.example.manseryeok.db.UserDBHelper
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.adapter.DBListAdapter
import com.example.manseryeok.databinding.ActivityDbactivityBinding
import com.example.manseryeok.db.ManseryeokSQLHelper
import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.User
import com.example.manseryeok.utils.ParentActivity
import java.util.Calendar

class UserDBActivity : ParentActivity() {
    private lateinit var dbListAdapter: DBListAdapter
    private val binding by lazy { ActivityDbactivityBinding.inflate(layoutInflater) }
    private val userList = ArrayList<User>()
    private val manseryeokList = ArrayList<Manseryeok>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDb)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }


        val myDB = UserDBHelper(this)
        val sqliteData = myDB.allData


        // 유저의 생일 - 1년 부터 + 100년까지의 정보
        val manseryeokSQLHelper = ManseryeokSQLHelper(this)
        manseryeokSQLHelper.createDataBase()
        manseryeokSQLHelper.open()

        while (sqliteData.moveToNext()) {

            // firstName: String?,
            // lastName: String?,
            // gender: Int, // 0 - 남자, 1 - 여자

            // birthYear: Int,
            // birthMonth: Int,
            // birthDay: Int,
            // birthHour: Int,
            // birthMinute: Int,

            // birthPlace: String?,
            // timeDiff: Int,

            // useSummerTime: Int,
            // useTokyoTime: Int

            val id = sqliteData.getLong(0)
            val firstName = sqliteData.getString(1)
            val lastName = sqliteData.getString(2)
            val gender = sqliteData.getInt(3)
            val birthYear = sqliteData.getInt(4)
            val birthMonth = sqliteData.getInt(5)
            val birthDay = sqliteData.getInt(6)
            val birthHour = sqliteData.getInt(7)
            val birthMinute = sqliteData.getInt(8)
            val birthPlace = sqliteData.getString(9)
            val timeDiff = sqliteData.getInt(10)
            val useSummerTime = sqliteData.getInt(11)
            val useTokyoTime = sqliteData.getInt(12)
            val memo = sqliteData.getString(13)

            userList.add(
                User(
                    id,
                    firstName,
                    lastName,
                    if (gender == 0) 0 else 1,
                    birthYear,
                    birthMonth,
                    birthDay,
                    birthHour,
                    birthMinute,
                    birthPlace,
                    timeDiff,
                    useSummerTime,
                    useTokyoTime,
                    memo
                )
            )

            manseryeokList.add(manseryeokSQLHelper.getDayData(birthYear, birthMonth, birthDay))
        }

        binding.run {
            dbListAdapter = DBListAdapter(this@UserDBActivity, userList, manseryeokList)
            dbListAdapter.notifyDataSetChanged()
            rvDbList.adapter = dbListAdapter
        }


        dbListAdapter.onMenuClickListener = object : DBListAdapter.OnMenuClickListener {
            override fun onSearchClick(ID: String, position: Int) {
                val intent = Intent(this@UserDBActivity, CalendarActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER, userList[position])
                startActivity(intent)
            }

            override fun onDeleteClick(ID: String, position: Int) {
                val deleteResult = myDB.deleteData(ID)
                if (deleteResult > 0) {
                    showShortToast(getString(R.string.msg_delete_complete))
                    userList.removeAt(position)
                } else {
                    showShortToast(getString(R.string.msg_delete_fail))
                }
                dbListAdapter.notifyDataSetChanged()
            }
        }
        manseryeokSQLHelper.close()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}