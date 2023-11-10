package com.example.manseryeok.page

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manseryeok.R
import com.example.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.example.manseryeok.adapter.SixtyHorizontalAdapter
import com.example.manseryeok.adapter.SixtyHorizontalSmallAdapter
import com.example.manseryeok.databinding.ActivityCalendarBinding
import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.SixtyHorizontalItem
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.utils.ParentActivity
import com.example.manseryeok.utils.Sinsal
import com.example.manseryeok.utils.Utils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class CalendarActivity : ParentActivity() {
    private val binding by lazy { ActivityCalendarBinding.inflate(layoutInflater) }
    private val tenArray by lazy { resources.getStringArray(R.array.ten_array) }
    private val twelveArray by lazy { resources.getStringArray(R.array.twelve_array) }

    private val luckItems by lazy { ArrayList<SixtyHorizontalItem>() }
    private val yearItems by lazy { ArrayList<SixtyHorizontalItem>() }
    private val monthItems by lazy { ArrayList<SixtyHorizontalItem>() }

    private var yearPillar = "" // 년주
    private var monthPillar = "" // 월주
    private var dayPillar = "" // 일주
    private var timePillar = "" // 시주

    private var isTimeInclude = false

    private lateinit var luckAdapter: SixtyHorizontalAdapter
    private lateinit var yearAdapter: SixtyHorizontalSmallAdapter
    private lateinit var monthAdapter: SixtyHorizontalSmallAdapter
    private lateinit var userModel: User // 유저의 정보 DTO
    private lateinit var userBirth: Calendar // 유저의 생일 Calendar
    private lateinit var userCalendar: List<Manseryeok> // 유저의 생일 -1년 ~ + 100년의 만세력 정보
    private lateinit var userBirthCalender: Manseryeok // 유저의 생일 당일의 만세력 정보

    private val TAG = "CalendarActivity"

    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }
    private var currentTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        commonSetting()
        showProgress(this@CalendarActivity, "잠시만 기다려주세요")

        loadUserModel()

        // 프로그래스바를 띄우기 위해 1초 딜레이
        Handler().postDelayed({
            currentTime = System.currentTimeMillis()

            initLoadDB()

            setUserBirth()
            setUpPillar() // 기둥 세우기
            setUpPillarLabel() // 라벨
            setUpEmpty() // 공망 설정
            setUpSeasonBirth() // 절기 설정
            setUpProperty() // 오행 - 화수목금토 개수 설정
            setUpPlusMinusFiveProperty() // 음양오행 - 해중금, 복등화 등등
            setUpJiJiAmjangan() // 지지 암장간
            setUpShootingStar() // 십이운성
            setUpSinsal() // 신살
            setUpYearAndMonthPillar() // 년주 리사이클러뷰
            setRecyclerViewClickEvent() // 년주 리사이클러뷰 클릭 이벤트 처리
            setUpLuckRecyclerView() // 대운 리사이클러뷰
            setUpMemo()

            currentTime = System.currentTimeMillis()

            hideProgress()

            binding.run {
                btnGotoName.setOnClickListener {
                    val intent = Intent(this@CalendarActivity, NameActivity::class.java)
                    intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, userModel.userId)
                    startActivity(intent)
                    finish()
                }

                btnCalendarShare.setOnClickListener { shareResult() }
            }
        }, 1000)
    }

    private fun loadUserModel() {
        val userId = intent.getLongExtra(Utils.INTENT_EXTRAS_USER_ID, -1L)

        runBlocking {
            launch(IO) {
                val userDao = AppDatabase.getInstance(applicationContext).userDao()
                userModel = userDao.getUser(userId)
            }
        }
    }

    private fun commonSetting() {
        setSupportActionBar(binding.toolbarCalendar)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }
    }


    private fun setUpMemo() {
        binding.etMemo.setText(userModel.memo)

        binding.etMemo.addTextChangedListener {
            userModel.memo = it.toString()
            runBlocking {
                launch(IO) {
                    userDao.update(userModel)
                }
            }
        }
    }

    private fun setUpSinsal() {
        // year: Int, gender: Int, yearGanji: Char, ganji: Char
        val year = HashSet<String>()
        val month = HashSet<String>()
        val day = HashSet<String>()
        val time = HashSet<String>()

        // 생년 기준
        month.addAll(
            Sinsal.getYearBottom(
                userBirth[Calendar.YEAR],
                userModel.gender,
                yearPillar[1],
                monthPillar[1]
            )
        )
        day.addAll(
            Sinsal.getYearBottom(
                userBirth[Calendar.YEAR],
                userModel.gender,
                yearPillar[1],
                dayPillar[1]
            )
        )
        time.addAll(
            Sinsal.getYearBottom(
                userBirth[Calendar.YEAR],
                userModel.gender,
                yearPillar[1],
                timePillar[1]
            )
        )

        // 생월 기준
        // fun getMonthBottom(type: Type, monthGanji: Char, ganji: String)
        month.addAll(Sinsal.getMonthBottom(Sinsal.Type.YEAR, monthPillar[1], yearPillar))
        day.addAll(Sinsal.getMonthBottom(Sinsal.Type.DAY, monthPillar[1], dayPillar))
        time.addAll(Sinsal.getMonthBottom(Sinsal.Type.TIME, monthPillar[1], timePillar))

        // 생일 기준
        year.addAll(Sinsal.getDayBottom(dayPillar[1], yearPillar))
        month.addAll(Sinsal.getDayBottom(dayPillar[1], monthPillar))
        time.addAll(Sinsal.getDayBottom(dayPillar[1], timePillar))

        binding.run {
            tvSinsalYear.text = year.joinToString("\n\n")
            tvSinsalMonth.text = month.joinToString("\n\n")
            tvSinsalDay.text = day.joinToString("\n\n")
            tvSinsalTime.text = time.joinToString("\n\n")
        }
    }

    private fun shareResult() {
        /*
        만세력 결과 - 전윤호(25세)

        (양) 1998년 12월 04일
        (음) 1998년 10월 16일 23:00
        (正) 1998년 12월 04일 22:30

        시 일 월 년
        丁 乙 癸 戊
        亥 酉 亥 寅

        71 61 51 41 31 21 11 1.0
        辛 庚 己 戊 丁 丙 乙 甲
        未 午 巳 辰 卯 寅 丑 子
         */
        var sendContent = "${userModel.firstName}${userModel.lastName}\n" +
                "${if (isTimeInclude) Utils.dateTimeKorFormat.format(userBirth.timeInMillis) else Utils.dateKorFormat.format(userBirth.timeInMillis)}\n" + "\n"

        if (isTimeInclude) {
            sendContent += "시 일 월 년\n"
            sendContent += "${timePillar[0]} ${dayPillar[0]} ${monthPillar[0]} ${yearPillar[0]}\n"
            sendContent += "${timePillar[1]} ${dayPillar[1]} ${monthPillar[1]} ${yearPillar[1]}\n"
        } else {
            sendContent += "일 월 년\n"
            sendContent += "${dayPillar[0]} ${monthPillar[0]} ${yearPillar[0]}\n"
            sendContent += "${dayPillar[1]} ${monthPillar[1]} ${yearPillar[1]}\n"
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, sendContent)
        }
        startActivity(Intent.createChooser(intent, "Share"))
    }

    // 기둥 세우기
    private fun setUpPillar() {
        binding.run {

            userBirthCalender = userCalendar.find {
                it.cd_sy == userBirth[Calendar.YEAR] &&
                        it.cd_sm == userBirth[Calendar.MONTH] + 1 &&
                        it.cd_sd == userBirth[Calendar.DAY_OF_MONTH]
            }!!

            // 正 생일
            tvCal5.text = "${Utils.dateKorFormat.format(userBirth.timeInMillis)}"

            yearPillar = userBirthCalender.cd_hyganjee!!
            monthPillar = userBirthCalender.cd_hmganjee!!
            dayPillar = userBirthCalender.cd_hdganjee!!
            timePillar = Utils.getTimeGanji(dayPillar[0], userBirth[Calendar.HOUR_OF_DAY])


            if (userBirthCalender.cd_hterms != "NULL" && isTimeInclude) {
                val seasonTimeString = userBirthCalender.cd_terms_time.toString()

                val seasonCalendar = Calendar.getInstance().apply {
                    this[Calendar.YEAR] = seasonTimeString.substring(0, 4).toInt()
                    this[Calendar.MONTH] = seasonTimeString.substring(4, 6).toInt() - 1
                    this[Calendar.DAY_OF_MONTH] = seasonTimeString.substring(6, 8).toInt()
                    this[Calendar.HOUR_OF_DAY] = seasonTimeString.substring(8, 10).toInt()
                    this[Calendar.MINUTE] = seasonTimeString.substring(10, 12).toInt()
                }

                if (userBirth.timeInMillis < seasonCalendar.timeInMillis) {
                    val targetDay = userCalendar.indexOf(userBirthCalender)
                    yearPillar = userCalendar[targetDay - 1].cd_hyganjee!!
                    monthPillar = userCalendar[targetDay - 1].cd_hmganjee!!
                }
            }

            // 년주
            tvPillarYearTop.text = yearPillar[0].toString()
            tvPillarYearBottom.text = yearPillar[1].toString()
            when (Utils.tenGan[0].indexOf(yearPillar[0].toString())) {
                0, 1 -> tvPillarYearTop.setBackgroundResource(R.drawable.box_mint) // 목
                2, 3 -> tvPillarYearTop.setBackgroundResource(R.drawable.box_red) // 화
                4, 5 -> tvPillarYearTop.setBackgroundResource(R.drawable.box_yellow) // 토
                6, 7 -> tvPillarYearTop.setBackgroundResource(R.drawable.box_light_gray) // 금
                8, 9 -> tvPillarYearTop.setBackgroundResource(R.drawable.box_sky) // 수
            }

            when (Utils.twelveGan[0].indexOf(yearPillar[1].toString())) {
                0, 11 -> tvPillarYearBottom.setBackgroundResource(R.drawable.box_sky) // 수
                1, 4, 7, 10 -> tvPillarYearBottom.setBackgroundResource(R.drawable.box_yellow) // 토
                2, 3 -> tvPillarYearBottom.setBackgroundResource(R.drawable.box_mint) // 목
                5, 6 -> tvPillarYearBottom.setBackgroundResource(R.drawable.box_red) // 화
                8, 9 -> tvPillarYearBottom.setBackgroundResource(R.drawable.box_light_gray) // 금
            }

            // 월주
            tvPillarMonthTop.text = monthPillar[0].toString()
            tvPillarMonthBottom.text = monthPillar[1].toString()

            when (Utils.tenGan[0].indexOf(monthPillar[0].toString())) {
                0, 1 -> tvPillarMonthTop.setBackgroundResource(R.drawable.box_mint) // 목
                2, 3 -> tvPillarMonthTop.setBackgroundResource(R.drawable.box_red) // 화
                4, 5 -> tvPillarMonthTop.setBackgroundResource(R.drawable.box_yellow) // 토
                6, 7 -> tvPillarMonthTop.setBackgroundResource(R.drawable.box_light_gray) // 금
                8, 9 -> tvPillarMonthTop.setBackgroundResource(R.drawable.box_sky) // 수
            }

            when (Utils.twelveGan[0].indexOf(monthPillar[1].toString())) {
                0, 11 -> tvPillarMonthBottom.setBackgroundResource(R.drawable.box_sky) // 수
                1, 4, 7, 10 -> tvPillarMonthBottom.setBackgroundResource(R.drawable.box_yellow) // 토
                2, 3 -> tvPillarMonthBottom.setBackgroundResource(R.drawable.box_mint) // 목
                5, 6 -> tvPillarMonthBottom.setBackgroundResource(R.drawable.box_red) // 화
                8, 9 -> tvPillarMonthBottom.setBackgroundResource(R.drawable.box_light_gray) // 금
            }

            // 일주
            tvPillarDayTop.text = dayPillar[0].toString()
            tvPillarDayBottom.text = dayPillar[1].toString()

            when (Utils.tenGan[0].indexOf(dayPillar[0].toString())) {
                0, 1 -> tvPillarDayTop.setBackgroundResource(R.drawable.box_mint) // 목
                2, 3 -> tvPillarDayTop.setBackgroundResource(R.drawable.box_red) // 화
                4, 5 -> tvPillarDayTop.setBackgroundResource(R.drawable.box_yellow) // 토
                6, 7 -> tvPillarDayTop.setBackgroundResource(R.drawable.box_light_gray) // 금
                8, 9 -> tvPillarDayTop.setBackgroundResource(R.drawable.box_sky) // 수
            }

            when (Utils.twelveGan[0].indexOf(dayPillar[1].toString())) {
                0, 11 -> tvPillarDayBottom.setBackgroundResource(R.drawable.box_sky) // 수
                1, 4, 7, 10 -> tvPillarDayBottom.setBackgroundResource(R.drawable.box_yellow) // 토
                2, 3 -> tvPillarDayBottom.setBackgroundResource(R.drawable.box_mint) // 목
                5, 6 -> tvPillarDayBottom.setBackgroundResource(R.drawable.box_red) // 화
                8, 9 -> tvPillarDayBottom.setBackgroundResource(R.drawable.box_light_gray) // 금
            }

            // 시주
            if (isTimeInclude) {
                // 시간 포함
                timePillar = Utils.getTimeGanji(dayPillar[0], userBirth[Calendar.HOUR_OF_DAY])
                tvPillarTimeTop.text = timePillar[0].toString()
                tvPillarTimeBottom.text = timePillar[1].toString()

                tvPillarTimeTop.visibility = View.VISIBLE
                tvPillarTimeBottom.visibility = View.VISIBLE


                when (Utils.tenGan[0].indexOf(timePillar[0].toString())) {
                    0, 1 -> tvPillarTimeTop.setBackgroundResource(R.drawable.box_mint) // 목
                    2, 3 -> tvPillarTimeTop.setBackgroundResource(R.drawable.box_red) // 화
                    4, 5 -> tvPillarTimeTop.setBackgroundResource(R.drawable.box_yellow) // 토
                    6, 7 -> tvPillarTimeTop.setBackgroundResource(R.drawable.box_light_gray) // 금
                    8, 9 -> tvPillarTimeTop.setBackgroundResource(R.drawable.box_sky) // 수
                }

                when (Utils.twelveGan[0].indexOf(timePillar[1].toString())) {
                    0, 11 -> tvPillarTimeBottom.setBackgroundResource(R.drawable.box_sky) // 수
                    1, 4, 7, 10 -> tvPillarTimeBottom.setBackgroundResource(R.drawable.box_yellow) // 토
                    2, 3 -> tvPillarTimeBottom.setBackgroundResource(R.drawable.box_mint) // 목
                    5, 6 -> tvPillarTimeBottom.setBackgroundResource(R.drawable.box_red) // 화
                    8, 9 -> tvPillarTimeBottom.setBackgroundResource(R.drawable.box_light_gray) // 금
                }
            } else {
                tvPillarTimeTop.visibility = View.INVISIBLE
                tvPillarTimeBottom.visibility = View.INVISIBLE
            }
        }
    }

    private fun setUpPillarLabel() {
        binding.run {
            val me = dayPillar[0].toString()
            tvPillarYearTopLabel.text = Utils.getPillarLabel(me, yearPillar[0].toString())
            tvPillarYearBottomLabel.text = Utils.getPillarLabel(me, yearPillar[1].toString())
            tvPillarMonthTopLabel.text = Utils.getPillarLabel(me, monthPillar[0].toString())
            tvPillarMonthBottomLabel.text = Utils.getPillarLabel(me, monthPillar[1].toString())
            tvPillarDayTopLabel.text = Utils.getPillarLabel(me, dayPillar[0].toString())
            tvPillarDayBottomLabel.text = Utils.getPillarLabel(me, dayPillar[1].toString())

            if (isTimeInclude) {
                tvPillarTimeTopLabel.text = Utils.getPillarLabel(me, timePillar[0].toString())
                tvPillarTimeBottomLabel.text = Utils.getPillarLabel(me, timePillar[1].toString())
            }
        }
    }

    private fun setUpEmpty() {
        val yearEmpty = Utils.getEmptyGanji(yearPillar)
        val dayEmpty = Utils.getEmptyGanji(dayPillar)

        binding.tvEmpty.text = "공망 : $yearEmpty (년), $dayEmpty (일)"
    }

    private fun setUpSeasonBirth() {
        var nearSeasonTop = userCalendar.indexOf(userBirthCalender)
        var nearSeasonBottom = nearSeasonTop

        var seasonK = "" // 대설
        var seasonH = "" // 大雪
        val seasonCalendar =
            Calendar.getInstance() // 199812071646 - year 0 ~ 3, month 4 ~ 5, day 6 ~ 7, hour 8 ~ 9, min 10 ~ 11

        if (nearSeasonBottom != -1) {
            while (true) {
                if (userCalendar[nearSeasonTop].cd_terms_time != null && userCalendar[nearSeasonTop].cd_terms_time != 0L) {
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

                if (nearSeasonBottom > 0 && userCalendar[nearSeasonBottom].cd_terms_time != null && userCalendar[nearSeasonBottom].cd_terms_time != 0L) {
                    val termsTimeString = userCalendar[nearSeasonBottom].cd_terms_time.toString()
                    seasonCalendar.run {
                        this[Calendar.YEAR] = termsTimeString.substring(0, 4).toInt()
                        this[Calendar.MONTH] = termsTimeString.substring(4, 6).toInt() - 1
                        this[Calendar.DAY_OF_MONTH] = termsTimeString.substring(6, 8).toInt()
                        this[Calendar.HOUR_OF_DAY] = termsTimeString.substring(8, 10).toInt()
                        this[Calendar.MINUTE] = termsTimeString.substring(10, 12).toInt()
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

        if (isTimeInclude) {
            properties[Utils.getProperty(timePillar[0])]++
            properties[Utils.getProperty(timePillar[1])]++
        }

        binding.run {
            tvFire.text = "火(${properties[0]})"
            tvWater.text = "水(${properties[1]})"
            tvTree.text = "木(${properties[2]})"
            tvGold.text = "金(${properties[3]})"
            tvSand.text = "土(${properties[4]})"
        }
    }

    private fun setUpPlusMinusFiveProperty() {
        binding.run {
            tvFivePropertyYear.text = Utils.getFiveProperty(yearPillar)
            tvFivePropertyMonth.text = Utils.getFiveProperty(monthPillar)
            tvFivePropertyDay.text = Utils.getFiveProperty(dayPillar)
            if (isTimeInclude) {
                tvFivePropertyTime.text = Utils.getFiveProperty(timePillar)
            } else {
                tvFivePropertyTime.text = ""
            }
        }
    }

    private fun setUpJiJiAmjangan() {
        binding.run {
            if (isTimeInclude) tvTimeJiji.text = Utils.getJijiAmJangan(timePillar[1])
            else tvTimeJiji.text = "-"
            tvDayJiji.text = Utils.getJijiAmJangan(dayPillar[1])
            tvMonthJiji.text = Utils.getJijiAmJangan(monthPillar[1])
            tvYearJiji.text = Utils.getJijiAmJangan(yearPillar[1])
        }
    }

    private fun setUpShootingStar() {
        binding.run {
            tvShootingStarYear.text = Utils.getTwelveShootingStar(dayPillar[0], yearPillar[1])
            tvShootingStarMonth.text = Utils.getTwelveShootingStar(dayPillar[0], monthPillar[1])
            tvShootingStarDay.text = Utils.getTwelveShootingStar(dayPillar[0], dayPillar[1])

            if (isTimeInclude) {
                tvShootingStarTime.text = Utils.getTwelveShootingStar(dayPillar[0], timePillar[1])
            } else {
                tvShootingStarTime.text = ""
            }
        }
    }

    private fun setUserBirth() {
        binding.run {
            tvCalSun.text = Utils.dateKorFormat.format(userModel.getBirthOrigin().timeInMillis)
            tvCalMoon.text = Utils.dateKorFormat.format(Utils.convertSolarToLunar(userBirth))
        }
    }

    private fun initLoadDB() {
        userBirth = userModel.getBirthCalculated()
        isTimeInclude = userModel.includeTime

        val mDBHelper = ManseryeokSQLHelper(applicationContext)
        mDBHelper.createDataBase()
        mDBHelper.open()

        // 유저의 생일 - 1년 부터 + 100년까지의 정보
        userCalendar = mDBHelper.getTableData(userBirth[Calendar.YEAR] - 1, userBirth[Calendar.YEAR] + 100)!!

        mDBHelper.close()
    }

    // 대운 리사이클러 뷰 세팅
    private fun setUpLuckRecyclerView() {
        var topPtr = tenArray.indexOf(monthPillar[0].toString())
        var bottomPtr = twelveArray.indexOf(monthPillar[1].toString())

        var direction = if (userModel.gender == 0) 1 else -1 // 남자 순행, 여자 역행, 양 순행, 음 역행
        direction *= Utils.getSign(yearPillar[0])

        var firstAge = 0

        if (direction == 1) {
            var cnt = 0
            var ptr = userCalendar.indexOf(userBirthCalender)
            while (true) {
                if (userCalendar[ptr].cd_terms_time != null && userCalendar[ptr].cd_terms_time != 0L) break
                ptr++
                cnt++
            }
            firstAge = (cnt / 3).toInt()
        } else {
            var cnt = 0
            var ptr = userCalendar.indexOf(userBirthCalender)
            while (true) {
                if (userCalendar[ptr].cd_terms_time != null && userCalendar[ptr].cd_terms_time != 0L) {
                    break
                }
                ptr--
                cnt++
            }
            firstAge = (cnt / 3).toInt()
        }

        binding.run {
            luckAdapter = SixtyHorizontalAdapter(this@CalendarActivity, luckItems)
            rvLuck.adapter = luckAdapter

            luckAdapter.onItemClickListener = object : SixtyHorizontalAdapter.OnItemClickListener {
                override fun onItemClick(age: Int, pos: Int) {
//                    if(year == 0) return
//                    rvYearPillar.findViewHolderForLayoutPosition(yearItems.find { it.label == year }!!.label)?.itemView?.performClick()
//                    luckAdapter.notifyDataSetChanged()
                    luckAdapter.notifyDataSetChanged()
                    val yPos = pos * 10 + firstAge - 1
                    if (yPos >= 0) {
                        yearAdapter.itemClicked(yPos)
                        (rvYearPillar.layoutManager!! as LinearLayoutManager).scrollToPositionWithOffset(
                            yPos,
                            -5
                        )
                    }
                }
            }

            repeat(12) {
                val age = it * 10 + firstAge
                val year = userBirth[Calendar.YEAR] + age - 1

                if (direction == 1) {
                    topPtr++
                    bottomPtr++

                    if (topPtr >= 10) topPtr = 0
                    if (bottomPtr >= 12) bottomPtr = 0
                } else {
                    topPtr--
                    bottomPtr--

                    if (topPtr < 0) topPtr = 9
                    if (bottomPtr < 0) bottomPtr = 11
                }

                luckItems.add(SixtyHorizontalItem(age, tenArray[topPtr], twelveArray[bottomPtr]))

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

            for (i in 0..150) {
                val yGanji = Utils.getYearGanji(userBirth[Calendar.YEAR] + i)
                yearItems.add(
                    SixtyHorizontalItem(
                        userBirth[Calendar.YEAR] + i,
                        yGanji[0].toString(),
                        yGanji[1].toString()
                    )
                )
            }
        }
    }


    private fun setRecyclerViewClickEvent() {
        binding.run {
            yearAdapter.useItemClickEvent = true
            yearAdapter.onItemClickListener =
                object : SixtyHorizontalSmallAdapter.OnItemClickListener {
                    override fun onItemClick(year: Int) {
                        yearAdapter.notifyDataSetChanged()

                        setUpMonthPillar(Utils.getYearGanji(year)[0])
                    }
                }

            val today = Calendar.getInstance()
            var initialPos = today[Calendar.YEAR] - userBirth[Calendar.YEAR]
            if (initialPos !in 0..100) initialPos = 0

            yearAdapter.selectedItemPos = initialPos
            val scrollViewFocusPos = if (initialPos - 1 < 0) 0 else initialPos - 1
            rvYearPillar.scrollToPosition(scrollViewFocusPos)

            yearAdapter.notifyDataSetChanged()


            var initialYear = today[Calendar.YEAR]
            if (initialYear !in userBirth[Calendar.YEAR]..userBirth[Calendar.YEAR] + 100)
                initialYear = userBirth[Calendar.YEAR]

            setUpMonthPillar(yearPillar[0])
        }
    }

    // 월주 리사이클러뷰 세팅
    private fun setUpMonthPillar(year: Char) {
        binding.run {
            monthItems.clear()
            val yearGanji = Utils.getMonthGanjiList(year).split(" ")

            for (i in 0 until 12) {
                monthItems.add(
                    SixtyHorizontalItem(
                        i + 1,
                        yearGanji[i][0].toString(),
                        yearGanji[i][1].toString()
                    )
                )
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