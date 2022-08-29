package com.example.manseryeok.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.manseryeok.UserDB.DatabaseHelper
import com.example.manseryeok.adapter.DBListAdapter
import com.example.manseryeok.databinding.ActivityDbactivityBinding
import com.example.manseryeok.models.DBListItem

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

            var pillar = "${yearPillar[0]}${monthPillar[0]}${dayPillar[0]}${if (timePillar == null || timePillar == "NULL") "" else timePillar[0]}" +
                    "\n"+"${yearPillar[1]}${monthPillar[0]}${dayPillar[0]}${if(timePillar == null || timePillar == "NULL") "" else timePillar[1]}"

            dbList.add(DBListItem(id,firstName, lastName, birth, if(gender == "남") 0 else 1, pillar))
        }

//        val isInserted = myDB.insertData("전", "윤호", "202208082020", "남", "子丑","子丑","子丑","子丑")
//        val isInserted2 = myDB.insertData("테", "스트", "202208082020", "여","子丑","子丑","子丑","子丑")
//        Toast.makeText(applicationContext,"${if (isInserted) "케이스 1 성공" else "케이스 1 실패" }",Toast.LENGTH_SHORT).show()
//        Toast.makeText(applicationContext,"${if (isInserted2) "케이스 2 성공" else "케이스 2 실패" }",Toast.LENGTH_SHORT).show()

        binding.run {
            dbListAdapter = DBListAdapter(this@DBActivity, dbList)
            dbListAdapter.notifyDataSetChanged()
            rvDbList.adapter = dbListAdapter
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