package com.example.manseryeok.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.manseryeok.R
import com.example.manseryeok.userDB.UserDBHelper
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.adapter.DBListAdapter
import com.example.manseryeok.databinding.ActivityDbactivityBinding
import com.example.manseryeok.models.DBListItem
import com.example.manseryeok.models.User

class UserDBActivity : AppCompatActivity() {
    private lateinit var dbListAdapter: DBListAdapter
    private val binding by lazy { ActivityDbactivityBinding.inflate(layoutInflater) }
    private val dbList = ArrayList<DBListItem>()

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

        while(sqliteData.moveToNext()) {
//            firstName: String
//            lastName: String
//            gender: Int
//            birth: String
//            birthPlace: String
//            timeDiff: Int
//            yearPillar: String
//            monthPillar: String
//            dayPillar: String
//            timePillar: String

            val id = sqliteData.getString(0)
            val firstName = sqliteData.getString(1)
            val lastName = sqliteData.getString(2)
            val gender = sqliteData.getInt(3)
            val birth = sqliteData.getString(4)
            val birthPlace = sqliteData.getString(5)
            val timeDiff = sqliteData.getInt(6)
            val yearPillar = sqliteData.getString(7)
            val monthPillar = sqliteData.getString(8)
            val dayPillar = sqliteData.getString(9)
            val timePillar = sqliteData.getString(10)



            val pillar = "${if (timePillar == null || timePillar == "NULL" || timePillar == "") "" else timePillar[0]}${dayPillar[0]}${monthPillar[0]}${yearPillar[0]}" +
                    "\n"+"${if(timePillar == null || timePillar == "NULL" || timePillar == "") "" else timePillar[1]}${dayPillar[1]}${monthPillar[1]}${yearPillar[1]}"

            dbList.add(DBListItem(
                id,
                firstName,
                lastName,
                birth,
                birthPlace,
                timeDiff,
                if(gender == 0) 0 else 1, pillar))
        }

        binding.run {
            dbListAdapter = DBListAdapter(this@UserDBActivity, dbList)
            dbListAdapter.notifyDataSetChanged()
            rvDbList.adapter = dbListAdapter
        }


        dbListAdapter.onMenuClickListener = object : DBListAdapter.OnMenuClickListener {
            override fun onSearchClick(ID: String, position: Int) {
                val intent = Intent(this@UserDBActivity, CalendarActivity::class.java)

//              firstName: String?,
//              lastName: String?,
//              gender: Int, // 0 - 남자, 1 - 여자
//              birth: String?, // yyyyMMddHHmm or yyyyMMdd
//              birthPlace: String?,
//              timeDiff: Int
                val dbModel = dbList[position]

                val userModel =
                    User(
                        dbModel.firstName,
                        dbModel.lastName,
                        dbModel.gender,
                        dbModel.birth,
                        dbModel.birthPlace,
                        dbModel.timeDiff)

                intent.putExtra(Utils.INTENT_EXTRAS_USER, userModel)
                startActivity(intent)
            }

            override fun onDeleteClick(ID: String, position: Int) {
                val deleteResult = myDB.deleteData(ID)
                if(deleteResult > 0) {
                    Toast.makeText(applicationContext,getString(R.string.msg_delete_complete),Toast.LENGTH_SHORT).show()
                    dbList.removeAt(position)
                } else {
                    Toast.makeText(applicationContext,getString(R.string.msg_delete_fail),Toast.LENGTH_SHORT).show()
                }
                dbListAdapter.notifyDataSetChanged()
            }
        }
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