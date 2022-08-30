package com.example.manseryeok.page

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.manseryeok.R
import com.example.manseryeok.UserDB.DatabaseHelper
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
    private lateinit var userCalendar:List<Manseryeok> // 유저의 생일 -1년 ~ + 100년의 만세력 정보
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

        binding.run {
            btnCalendarSave.setOnClickListener {
                 saveResult()
            }
        }

        userModel = intent.getParcelableExtra<User>(Utils.INTENT_EXTRAS_USER)!!

        setUpUserInfo()
        initLoadDB()
        setUpPillar() // 기둥 세우기
        setUpSeasonBirth() // 절기 설정
        setUpProperty() // 오행 설정
        setUpLuckRecyclerView() // 대운 리사이클러뷰
        setUpYearAndMonthPillar() // 년주 리사이클러뷰
        setRecyclerViewClickEvent() // 년주 리사이클러뷰 클릭 이벤트 처리
    }

    private fun saveResult() {
        val myDB = DatabaseHelper(this)
//        firstName: String,
//        lastName: String,
//        birth: String,   202201012459, 20220101
//        gender: String,
//        yearPillar: String,
//        monthPillar: String,
//        dayPillar: String,
//        timePillar: String

        val insertDataResult = myDB.insertData(userModel.firstName!!,
            userModel.lastName!!,
            userModel.birth!!,
            if(userModel.gender == 0) "남" else "여",
            yearPillar,
            monthPillar,
            dayPillar,
            timePillar)

        myDB.close()

        if(insertDataResult) {
            Toast.makeText(applicationContext,getString(R.string.msg_save_complete),Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext,getString(R.string.msg_save_fail),Toast.LENGTH_SHORT).show()
        }
    }

    // SQLite 불러오기
    private fun initLoadDB() {
        val mDBHelper = ManseryeokSQLAdapter(applicationContext)
        mDBHelper.createDataBase()
        mDBHelper.open()

        val tempCal = Calendar.getInstance().apply {
            this[Calendar.YEAR] = userBirth[Calendar.YEAR]
            add(Calendar.MONTH, -2)
        }

        userCalendar = mDBHelper.getTableData(tempCal[Calendar.YEAR] - 1, tempCal[Calendar.YEAR] + 100)!!
        Log.d(TAG, "initLoadDB: ${tempCal[Calendar.YEAR]} ~ ${tempCal[Calendar.YEAR]+100}")
        Log.d(TAG, "initLoadDB: "+Utils.dateSlideFormat.format(userBirth.timeInMillis))

        mDBHelper.close()
    }

    // 기둥 세우기
    private fun setUpPillar() {
        binding.run {
            Log.d(TAG, "setUpUserInfo: ${Utils.dateTimeSlideFormat.format(userBirth.timeInMillis)}")

            userBirthCalender = userCalendar.find {
                it.cd_sy == userBirth[Calendar.YEAR] &&
                it.cd_sm == userBirth[Calendar.MONTH] + 1 &&
                it.cd_sd == userBirth[Calendar.DAY_OF_MONTH]
            }!!


            //tvCalMoon.text = "${Utils.dateKorFormat.format(moonCalendar.timeInMillis)}"

            // 正 생일
            tvCal5.text = "${Utils.dateKorFormat.format(userBirth.timeInMillis)}"


            yearPillar = userBirthCalender.cd_hyganjee!!
            monthPillar = userBirthCalender.cd_hmganjee!!
            dayPillar = userBirthCalender.cd_hdganjee!!


            if(userBirthCalender.cd_hterms != "NULL" && userModel.isIncludedTime) {
                val seasonTimeString = userBirthCalender.cd_terms_time.toString()

                val seasonCalendar = Calendar.getInstance().apply {
                    this[Calendar.YEAR] = seasonTimeString.substring(0, 4).toInt()
                    this[Calendar.MONTH] = seasonTimeString.substring(4, 6).toInt() - 1
                    this[Calendar.DAY_OF_MONTH] = seasonTimeString.substring(6, 8).toInt()
                    this[Calendar.HOUR_OF_DAY] = seasonTimeString.substring(8, 10).toInt()
                    this[Calendar.MINUTE] = seasonTimeString.substring(10, 12).toInt()
                }

                if(userBirth.timeInMillis < seasonCalendar.timeInMillis) {
                    val targetDay = userCalendar.indexOf(userBirthCalender)
                    yearPillar = userCalendar[targetDay - 1].cd_hyganjee!!
                    monthPillar = userCalendar[targetDay - 1].cd_hmganjee!!
                }
            }

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

    private fun setUpSeasonBirth() {
        var nearSeasonTop = userCalendar.indexOf(userBirthCalender)
        var nearSeasonBottom = nearSeasonTop

        var seasonK = "" // 대설
        var seasonH = "" // 大雪
        val seasonCalendar = Calendar.getInstance() // 199812071646 - year 0 ~ 3, month 4 ~ 5, day 6 ~ 7, hour 8 ~ 9, min 10 ~ 11

        if(nearSeasonBottom != -1) {
            while (true) {
                if(userCalendar[nearSeasonTop].cd_terms_time != null && userCalendar[nearSeasonTop].cd_terms_time != 0L) {
                    val termsTimeString = userCalendar[nearSeasonTop].cd_terms_time.toString()
                    seasonCalendar.run {
                        this[Calendar.YEAR] = termsTimeString.substring(0, 4).toInt()
                        this[Calendar.MONTH] = termsTimeString.substring(4, 6).toInt() - 1
                        this[Calendar.DAY_OF_MONTH] = termsTimeString.substring(6, 8).toInt()
                        this[Calendar.HOUR_OF_DAY] = termsTimeString.substring(8, 10).toInt()
                        this[Calendar.MINUTE] = termsTimeString.substring(10, 12).toInt()
                    }
                    seasonK = userCalendar[nearSeasonTop].cd_kterms.toString()
                    seasonH = userCalendar[nearSeasonTop].cd_hterms.toString()
                    break
                }

                if(nearSeasonBottom > 0 && userCalendar[nearSeasonBottom].cd_terms_time != null && userCalendar[nearSeasonBottom].cd_terms_time != 0L) {
                    val termsTimeString = userCalendar[nearSeasonBottom].cd_terms_time.toString()
                    seasonCalendar.run {
                        this[Calendar.YEAR] = termsTimeString.substring(0, 3).toInt()
                        this[Calendar.MONTH] = termsTimeString.substring(4, 5).toInt() - 1
                        this[Calendar.DAY_OF_MONTH] = termsTimeString.substring(6, 7).toInt()
                        this[Calendar.HOUR_OF_DAY] = termsTimeString.substring(8, 9).toInt()
                        this[Calendar.MINUTE] = termsTimeString.substring(10, 11).toInt()
                    }
                    seasonK = userCalendar[nearSeasonBottom].cd_kterms.toString()
                    seasonH = userCalendar[nearSeasonBottom].cd_hterms.toString()
                    break
                }

                nearSeasonTop++
                nearSeasonBottom--
            }

            binding.run {
                tvCalTermsTitle.text = seasonH
                tvCalTerms.text = Utils.dateKorFormat.format(seasonCalendar.timeInMillis)
            }
        }
    }

    private fun setUpProperty() {
        // 화 수 목 금 토 else
        val properties = Array(6) { 0 }

        properties[Utils.getProperty(yearPillar[0])]++
        properties[Utils.getProperty(yearPillar[1])]++
        properties[Utils.getProperty(monthPillar[0])]++
        properties[Utils.getProperty(monthPillar[1])]++
        properties[Utils.getProperty(dayPillar[0])]++
        properties[Utils.getProperty(dayPillar[1])]++
        properties[Utils.getProperty(timePillar[0])]++
        properties[Utils.getProperty(timePillar[1])]++

        binding.run {
            tvFire.text = "火(${properties[0]})"
            tvWater.text = "水(${properties[1]})"
            tvTree.text = "木(${properties[2]})"
            tvGold.text = "金(${properties[3]})"
            tvSand.text = "土(${properties[4]})"
        }
    }

    private fun setUpUserInfo() {
        binding.run {
            tvCalSun.text = Utils.dateKorFormat.format(Utils.dateSlideFormat.parse(userModel.birth))
            userBirth = Calendar.getInstance().apply { timeInMillis = Utils.dateTimeSlideFormat.parse(userModel.birth).time }
            tvCalMoon.text =
                Utils.dateKorFormat.format(Utils.convertSolarToLunar(Utils.dateSlideFormat.format(userBirth.timeInMillis)))
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