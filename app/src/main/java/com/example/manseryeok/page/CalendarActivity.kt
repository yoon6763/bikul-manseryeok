package com.example.manseryeok.page

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.manseryeok.R
import com.example.manseryeok.adapter.SixtyHorizontalAdapter
import com.example.manseryeok.adapter.SixtyHorizontalSmallAdapter
import com.example.manseryeok.databinding.ActivityCalendarBinding
import com.example.manseryeok.models.SixtyHorizontalItem
import java.security.AccessController.getContext


class CalendarActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCalendarBinding.inflate(layoutInflater) }
    private val tenArray by lazy { resources.getStringArray(R.array.ten_array) }
    private val twelveArray by lazy { resources.getStringArray(R.array.twelve_array) }

    private val luckItems by lazy { ArrayList<SixtyHorizontalItem>() }
    private val yearItems by lazy { ArrayList<SixtyHorizontalItem>() }
    private val monthItems by lazy { ArrayList<SixtyHorizontalItem>() }

    private lateinit var luckAdapter: SixtyHorizontalAdapter
    private lateinit var yearAdapter: SixtyHorizontalSmallAdapter
    private lateinit var monthAdapter: SixtyHorizontalSmallAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCalendar)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        setUpLuckRecyclerView()
        setUpYearPillar()
        setUpMonthPillar()
    }


    private fun setUpLuckRecyclerView() {
        binding.run {
            luckAdapter = SixtyHorizontalAdapter(this@CalendarActivity, luckItems)
            rvLuck.adapter = luckAdapter

            repeat(10) {
                luckItems.add(
                    SixtyHorizontalItem(
                        (it * 2 + 3).toString(),
                        tenArray.random(),
                        twelveArray.random()
                    )
                )
            }
            luckAdapter.notifyDataSetChanged()
        }
    }

    private fun setUpYearPillar() {
        binding.run {
            yearAdapter = SixtyHorizontalSmallAdapter(this@CalendarActivity, yearItems)
            rvYearPillar.adapter = yearAdapter

            repeat(20) {
                yearItems.add(SixtyHorizontalItem((2010 + it).toString(), tenArray.random(), twelveArray.random()))
            }
        }
    }

    private fun setUpMonthPillar() {
        binding.run {
            monthAdapter = SixtyHorizontalSmallAdapter(this@CalendarActivity,  monthItems)
            rvMonthPillar.adapter = monthAdapter

            repeat(20) {
                monthItems.add(SixtyHorizontalItem((it%12 + 1).toString(), tenArray.random(), twelveArray.random()
                ))
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