package com.example.manseryeok.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.manseryeok.R
import com.example.manseryeok.UserDB.DatabaseHelper
import com.example.manseryeok.Utils.Utils
import com.example.manseryeok.adapter.DBListAdapter
import com.example.manseryeok.databinding.ActivityDbactivityBinding
import com.example.manseryeok.models.DBListItem
import com.example.manseryeok.models.User

class DBActivity : AppCompatActivity() {
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


        val myDB = DatabaseHelper(this)
        val sqliteData = myDB.allData

        while(sqliteData.moveToNext()) {
            // firstName, lastName, birth, gender, yearPillar, monthPillar, dayPillar, timePillar
            val id = sqliteData.getString(0)
            val firstName = sqliteData.getString(1)
            val lastName = sqliteData.getString(2)
            val birth = sqliteData.getString(3)
            val gender = sqliteData.getString(4)
            val yearPillar = sqliteData.getString(5)
            val monthPillar = sqliteData.getString(6)
            val dayPillar = sqliteData.getString(7)
            val timePillar = sqliteData.getString(8)

            val pillar = "${if (timePillar == null || timePillar == "NULL" || timePillar == "") "" else timePillar[0]}${dayPillar[0]}${monthPillar[0]}${yearPillar[0]}" +
                    "\n"+"${if(timePillar == null || timePillar == "NULL" || timePillar == "") "" else timePillar[1]}${dayPillar[1]}${monthPillar[1]}${yearPillar[1]}"

            dbList.add(DBListItem(id,firstName, lastName, birth, if(gender == "남") "남" else "여", pillar))

        }

        binding.run {
            dbListAdapter = DBListAdapter(this@DBActivity, dbList)
            dbListAdapter.notifyDataSetChanged()
            rvDbList.adapter = dbListAdapter
        }


        dbListAdapter.onMenuClickListener = object : DBListAdapter.OnMenuClickListener {
            override fun onSearchClick(ID: String, position: Int) {
                val intent = Intent(this@DBActivity, CalendarActivity::class.java)

//                firstName: String?,
//                lastName: String?,
//                gender: Int, // 0 - 남자, 1 - 여자
//                isIncludedTime: Boolean,
//                birth: String?, // yyyy-MM-dd HH:mm
//                birthPlace: String?
                val dbModel = dbList[position]

                val userModel =
                    User(
                        dbModel.firstName,
                        dbModel.lastName,
                        if(dbModel.gender == "남") 0 else 1,
                        if(dbModel.birth.length == 8) false else true,
                        dbModel.birth,
                        "서울")

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