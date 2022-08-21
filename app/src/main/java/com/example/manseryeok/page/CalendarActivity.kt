package com.example.manseryeok.page

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.manseryeok.R
import com.example.manseryeok.Utils.Utils
import com.example.manseryeok.adapter.ManseryeokSQLAdapter
import com.example.manseryeok.adapter.SixtyHorizontalAdapter
import com.example.manseryeok.adapter.SixtyHorizontalSmallAdapter
import com.example.manseryeok.databinding.ActivityCalendarBinding
import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.SixtyHorizontalItem
import com.example.manseryeok.models.User
import java.util.*
import kotlin.collections.ArrayList


class CalendarActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCalendarBinding.inflate(layoutInflater) }
    private val tenArray by lazy { resources.getStringArray(R.array.ten_array) }
    private val twelveArray by lazy { resources.getStringArray(R.array.twelve_array) }

    private val luckItems by lazy { ArrayList<SixtyHorizontalItem>() }
    private val yearItems by lazy { ArrayList<SixtyHorizontalItem>() }
    private val monthItems by lazy { ArrayList<SixtyHorizontalItem>() }

    private var yearPillar = ""
    private var monthPillar = ""
    private var dayPillar = ""
    private var timePillar = ""

    private lateinit var luckAdapter: SixtyHorizontalAdapter
    private lateinit var yearAdapter: SixtyHorizontalSmallAdapter
    private lateinit var monthAdapter: SixtyHorizontalSmallAdapter
    private lateinit var userModel: User // 유저의 정보 DTO
    private lateinit var userBirth: Calendar // 유저의 생일 Calendar
    private lateinit var userCalendar:List<Manseryeok> // 유저의 생일 ~ + 100년의 만세력 정보
    private lateinit var userBirthCalender:Manseryeok // 유저의 생일 당일의 만세력 정보

    private val TAG = "CalendarActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCalendar)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        userModel = intent.getParcelableExtra<User>(Utils.INTENT_EXTRAS_USER)!!

        setUpUserInfo()
        initLoadDB()
        setUpPillar() // 기둥 세우기
        setUpLuckRecyclerView() // 대운 리사이클러뷰
        setUpYearAndMonthPillar() // 년주 리사이클러뷰
        setRecyclerViewClickEvent() // 년주 리사이클러뷰 클릭 이벤트 처리
    }

    // SQLite 불러오기
    private fun initLoadDB() {
        val mDBHelper = ManseryeokSQLAdapter(applicationContext)
        mDBHelper.createDataBase()
        mDBHelper.open()

        userCalendar = mDBHelper.getTableData(userBirth[Calendar.YEAR], userBirth[Calendar.YEAR] + 100)!!
        Log.d(TAG, "initLoadDB: "+Utils.dateFormat.format(userBirth.timeInMillis))

        mDBHelper.close()
    }

    // 기둥 세우기
    private fun setUpPillar() {
        binding.run {
            userBirthCalender = userCalendar.find {
                it.cd_sy == userBirth[Calendar.YEAR] &&
                it.cd_sm == userBirth[Calendar.MONTH] + 1 &&
                it.cd_sd == userBirth[Calendar.DAY_OF_MONTH]
            }!!

            // 음력 생일
            val moonCalendar = Calendar.getInstance().apply {
                this[Calendar.YEAR] = userBirthCalender.cd_ly!!
                this[Calendar.MONTH] = userBirthCalender.cd_lm!!
                this[Calendar.DAY_OF_MONTH] = userBirthCalender.cd_ld!!
            }

            tvCalMoon.text = "${Utils.dateKorFormat.format(moonCalendar.timeInMillis)}"

            // 正 생일
            tvCal5.text = "${Utils.dateKorFormat.format(userBirth.timeInMillis)}"


            yearPillar = userBirthCalender.cd_hyganjee!!
            monthPillar = userBirthCalender.cd_hmganjee!!
            dayPillar = userBirthCalender.cd_hdganjee!!

            // 년주
            tvPillarYearTop.text = yearPillar[0].toString()
            tvPillarYearBottom.text = yearPillar[1].toString()

            // 월주
            tvPillarMonthTop.text = monthPillar[0].toString()
            tvPillarMonthBottom.text = monthPillar[1].toString()

            // 일주
            tvPillarDayTop.text = dayPillar[0].toString()
            tvPillarDayBottom.text = dayPillar[1].toString()

            // 시주
            if(userModel.isIncludedTime) {
                timePillar = Utils.getTimeGanji(dayPillar[0].toString(), userBirth[Calendar.HOUR_OF_DAY])
                tvPillarTimeTop.text = timePillar[0].toString()
                tvPillarTimeBottom.text = timePillar[1].toString()

                tvPillarTimeTop.visibility = View.VISIBLE
                tvPillarTimeBottom.visibility = View.VISIBLE
            } else {
                tvPillarTimeTop.visibility = View.INVISIBLE
                tvPillarTimeBottom.visibility = View.INVISIBLE
            }
        }
    }

    private fun setUpUserInfo() {
        binding.run {
            tvCalSun.text = Utils.dateKorFormat.format(Utils.dateFormat.parse(userModel.birth))
            userBirth = Calendar.getInstance().apply { timeInMillis = Utils.dateTimeFormat.parse(userModel.birth).time }
        }
    }

    // 대운 리사이클러 뷰 세팅
    private fun setUpLuckRecyclerView() {
        binding.run {
            luckAdapter = SixtyHorizontalAdapter(this@CalendarActivity, luckItems)
            rvLuck.adapter = luckAdapter

            repeat(10) {
                luckItems.add(
                    SixtyHorizontalItem(
                        it * 2 + 3,
                        tenArray.random(),
                        twelveArray.random()
                    )
                )
            }
            luckAdapter.notifyDataSetChanged()
        }
    }

    // 년주 월주 리사이클러뷰 세팅
    private fun setUpYearAndMonthPillar() {
        binding.run {
            yearAdapter = SixtyHorizontalSmallAdapter(this@CalendarActivity, yearItems)
            rvYearPillar.adapter = yearAdapter
            monthAdapter = SixtyHorizontalSmallAdapter(this@CalendarActivity, monthItems)
            rvMonthPillar.adapter = monthAdapter

            for(i in 0 .. 100) {
                val yGanji = Utils.getYearGanji(userBirth[Calendar.YEAR] + i)
                yearItems.add(SixtyHorizontalItem(userBirth[Calendar.YEAR] + i, yGanji[0].toString(), yGanji[1].toString()))
            }
        }
    }


    private fun setRecyclerViewClickEvent() {
        binding.run {
            yearAdapter.useItemClickEvent = true
            yearAdapter.onItemClickListener = object : SixtyHorizontalSmallAdapter.OnItemClickListener {
                override fun onItemClick(year: Int) {
                    yearAdapter.notifyDataSetChanged()

                    setUpMonthPillar(year)
                }
            }

            val today = Calendar.getInstance()
            var initialPos = today[Calendar.YEAR] - userBirth[Calendar.YEAR]
            if(initialPos !in 0 .. 100) initialPos = 0

            yearAdapter.selectedItemPos = initialPos
            val scrollViewFocusPos = if(initialPos - 1 < 0) 0 else initialPos - 1
            rvYearPillar.scrollToPosition(scrollViewFocusPos)

            yearAdapter.notifyDataSetChanged()


            var initialYear = today[Calendar.YEAR]
            if(initialYear !in userBirth[Calendar.YEAR] .. userBirth[Calendar.YEAR] + 100) initialYear = userBirth[Calendar.YEAR]

            setUpMonthPillar(initialYear)
        }
    }

    // 월주 리사이클러뷰 세팅
    private fun setUpMonthPillar(year:Int) {
        if(year > 2100) {
            binding.run {
                tvMonthPillarMessage.visibility = View.VISIBLE
                rvMonthPillar.visibility = View.GONE
                return
            }
        } else {
            binding.run {
                tvMonthPillarMessage.visibility = View.GONE
                rvMonthPillar.visibility = View.VISIBLE
            }
        }

        binding.run {
            monthItems.clear()

            for(i in 1 .. 12) {
                val monthCal = userCalendar.findLast { it.cd_sy!! == year && it.cd_sm!! == i}
                if(monthCal != null) monthItems.add(SixtyHorizontalItem(i, monthCal.cd_hmganjee!![0].toString(), monthCal.cd_hmganjee!![1].toString()))
            }

            monthAdapter.notifyDataSetChanged()
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