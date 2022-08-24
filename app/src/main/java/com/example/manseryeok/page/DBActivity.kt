package com.example.manseryeok.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.manseryeok.adapter.DBListAdapter
import com.example.manseryeok.databinding.ActivityDbactivityBinding
import com.example.manseryeok.models.DBListItem

class DBActivity : AppCompatActivity() {
    private lateinit var dbListAdapter:DBListAdapter
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

        binding.run {

            dbList.add(DBListItem(0, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(1, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(2, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(3, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(4, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(5, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(6, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(7, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(8, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(9, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(10, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(11, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(12, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))
            dbList.add(DBListItem(13, "전윤호", 0, "2000-00-00", "2000-00-00", "丁戊己庚\n辰巳午未"))

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