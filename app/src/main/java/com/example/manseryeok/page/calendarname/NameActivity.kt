package com.example.manseryeok.page.calendarname

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.manseryeok.adapter.NameScoreAdapter
import com.example.manseryeok.databinding.ActivityNameBinding
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.name.NameScoreItem
import com.example.manseryeok.models.user.User
import com.example.manseryeok.service.calendar.CalendarService
import com.example.manseryeok.service.name.NameService
import com.example.manseryeok.utils.Extras
import com.example.manseryeok.utils.ParentActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime

class NameActivity : ParentActivity() {
    private val TAG = "NameActivity"

    private lateinit var userModel: User // 유저의 정보 DTO

    private val binding by lazy { ActivityNameBinding.inflate(layoutInflater) }
    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }

    private var searchDate = LocalDate.now()

    private val nameItems = ArrayList<NameScoreItem>()
    private val nameAdapter by lazy { NameScoreAdapter(this@NameActivity, nameItems) }

    private var name = ""
    private lateinit var userBirth: LocalDateTime
    private lateinit var nameService: NameService

    private lateinit var userCalendarService: CalendarService
    private lateinit var luckyCalendarService: CalendarService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        commonSetting()
        loadUserModel()
        nameService = NameService(this, name, userModel)
        userBirth = userModel.getBirthCalculatedLocalDateTime()
        luckyCalendarService = CalendarService(this@NameActivity, LocalDateTime.now(), false)

        binding.run {
            rvNameScore.adapter = nameAdapter

            setUpRadioButtonClickEvent()

            btnGotoManseryeok.setOnClickListener {
                val intent = Intent(this@NameActivity, CalendarActivity::class.java)
                intent.putExtra(Extras.INTENT_EXTRAS_USER_ID, userModel.userId)
                startActivity(intent)
                finish()
            }

            setUpMemo()
            btnShare.setOnClickListener { shareNameResult() }
        }

        setUpNumberPickerEvent()
        updatePage()
    }

    private fun updatePage() {
        updateBirthLabel()
        updateGanji()
    }

    private fun updateBirthLabel() = with(binding) {
        val dateContent = StringBuilder()
        dateContent.append("${searchDate.year}년  ")
        var monthLabel = searchDate.monthValue.toString()
        if (searchDate.monthValue < 10) {
            monthLabel = "0$monthLabel"
        }

        dateContent.append("${monthLabel}월  ")

        var dayLabel = searchDate.dayOfMonth.toString()
        if (searchDate.dayOfMonth < 10) {
            dayLabel = "0$dayLabel"
        }

        dateContent.append("${dayLabel}일")

        etCalender.setText(dateContent.toString())
    }

    private fun shareNameResult() {
        val content = StringBuilder()
        content.appendLine("${name}님의 이름풀이 결과입니다.\n")
        content.appendLine("기준년월: ${searchDate.year}년 ${searchDate.monthValue}월\n")

        val yearGanji =
            nameService.calcYearGanji(searchDate.year, searchDate.monthValue, searchDate.dayOfMonth)
        val monthGanji = nameService.calcMonthGanji(
            searchDate.year,
            searchDate.monthValue,
            searchDate.dayOfMonth
        )

        val dayGanji = nameService.calcDayGanji(
            searchDate.year,
            searchDate.monthValue,
            searchDate.dayOfMonth
        )

        content.appendLine("년 간지 정보\n")
        yearGanji.forEach {
            content.appendLine("${it.name}")

            it.nameScoreChildItems.forEach { childItem ->
                content.appendLine("${childItem.ganjiTop} ${childItem.nameHan} ${childItem.ganjiBottom}")
            }

            content.appendLine()
        }

        content.appendLine("\n\n\n월 간지 정보\n")
        monthGanji.forEach {
            content.appendLine("${it.name}")

            it.nameScoreChildItems.forEach { childItem ->
                content.appendLine("${childItem.ganjiTop} ${childItem.nameHan} ${childItem.ganjiBottom}")
            }

            content.appendLine()
        }

        content.appendLine("\n\n\n일 간지 정보\n")
        dayGanji.forEach {
            content.appendLine("${it.name}")

            it.nameScoreChildItems.forEach { childItem ->
                content.appendLine("${childItem.ganjiTop} ${childItem.nameHan} ${childItem.ganjiBottom}")
            }

            content.appendLine()
        }

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, content.toString())
        startActivity(Intent.createChooser(shareIntent, "이름풀이 결과 공유하기"))
    }

    private fun setUpRadioButtonClickEvent() = with(binding) {
        rgBirthType.setOnCheckedChangeListener { _, _ ->
            updatePage()
        }

        rgBirthType.check(rbYear.id)
    }

    private fun setUpNumberPickerEvent() = with(binding) {
        etCalender.setOnClickListener {

            val dialog = DatePickerDialog(
                this@NameActivity, DatePickerDialog.THEME_HOLO_LIGHT,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    searchDate = LocalDate.of(year, month + 1, day)
                    updatePage()
                }, searchDate.year, searchDate.monthValue - 1, searchDate.dayOfMonth
            )

            dialog.show()
        }
    }

    private fun loadUserModel() {
        runBlocking {
            launch(IO) {
                val userDao = AppDatabase.getInstance(applicationContext).userDao()
                val userId = intent.getLongExtra(Extras.INTENT_EXTRAS_USER_ID, -1L)
                userModel = userDao.getUser(userId)
                name = userModel.firstName + userModel.lastName

                userBirth = userModel.getBirthCalculatedLocalDateTime()
                userCalendarService =
                    CalendarService(this@NameActivity, userBirth, userModel.includeTime)
            }
        }
    }

    private fun commonSetting() {
        setSupportActionBar(binding.toolbarName)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setUpMemo() = with(binding) {
        etMemo.setText(userModel.memo)
        etMemo.addTextChangedListener {
            userModel.memo = it.toString()
            runBlocking {
                launch(IO) {
                    userDao.update(userModel)
                }
            }
        }
    }

    private fun updateGanji() = with(binding) {
        val type = when (rgBirthType.checkedRadioButtonId) {
            rbYear.id -> NameService.YEAR
            rbMonth.id -> NameService.MONTH
            rbDay.id -> NameService.DAY
            else -> {
                Toast.makeText(applicationContext, "잘못된 타입입니다.", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val nameScoreItems = when (type) {
            NameService.YEAR -> nameService.calcYearGanji(
                searchDate.year,
                searchDate.monthValue,
                searchDate.dayOfMonth
            )

            NameService.MONTH -> nameService.calcMonthGanji(
                searchDate.year,
                searchDate.monthValue,
                searchDate.dayOfMonth
            )

            NameService.DAY -> nameService.calcDayGanji(
                searchDate.year,
                searchDate.monthValue,
                searchDate.dayOfMonth
            )

            else -> {
                Toast.makeText(applicationContext, "잘못된 타입입니다.", Toast.LENGTH_SHORT).show()
                return
            }
        }

        nameItems.clear()
        nameItems.addAll(nameScoreItems)

        nameAdapter.notifyDataSetChanged()

        val ganji = nameService.getGanjiLabel(
            searchDate.year,
            searchDate.monthValue,
            searchDate.dayOfMonth,
            type
        )
        tvGanjiTop.text = ganji[0].toString()
        tvGanjiBottom.text = ganji[1].toString()
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