package com.example.manseryeok.page.calendarname

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.example.manseryeok.adapter.NameScoreAdapter
import com.example.manseryeok.databinding.ActivityNameBinding
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.name.NameScoreItem
import com.example.manseryeok.models.user.User
import com.example.manseryeok.page.calendarname.popup.NumberPickerDialog
import com.example.manseryeok.service.name.NameService
import com.example.manseryeok.utils.Extras
import com.example.manseryeok.utils.ParentActivity
import com.example.manseryeok.utils.Utils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class NameActivity : ParentActivity() {
    private val TAG = "NameActivity"

    private lateinit var userModel: User // 유저의 정보 DTO

    private val binding by lazy { ActivityNameBinding.inflate(layoutInflater) }
    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }

    private var searchDate = LocalDate.now()

    private val nameItems = ArrayList<NameScoreItem>()
    private val nameAdapter by lazy { NameScoreAdapter(this@NameActivity, nameItems) }

    private var name = ""

    private lateinit var nameService: NameService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        commonSetting()
        loadUserModel()

        nameService = NameService(this, name, userModel)

        binding.run {
            rvNameScore.adapter = nameAdapter

            setUpYearMonthRadioButton()

            btnGotoManseryeok.setOnClickListener {
                val intent = Intent(this@NameActivity, CalendarActivity::class.java)
                intent.putExtra(Extras.INTENT_EXTRAS_USER_ID, userModel.userId)
                startActivity(intent)
                finish()
            }

            setUpMemo()
            btnShare.setOnClickListener { shareNameResult() }
        }

        setYearAndMonthPicker()
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


        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, content.toString())
        startActivity(Intent.createChooser(shareIntent, "이름풀이 결과 공유하기"))
    }

    private fun setUpYearMonthRadioButton() = with(binding) {
        rgBirthType.setOnCheckedChangeListener { _, _ ->
            setUpGanji()
        }

        rgBirthType.check(rbYear.id)
    }


    private fun setYearAndMonthPicker() = with(binding) {
        etYear.setOnClickListener {
            val yearPickerDialog = NumberPickerDialog(this@NameActivity)
            yearPickerDialog.maxValue = 2100
            yearPickerDialog.minValue = 1900
            yearPickerDialog.initialValue = searchDate.year
            yearPickerDialog.onConfirmListener = object : NumberPickerDialog.OnConfirmListener {
                override fun onConfirm(number: Int) {
                    searchDate = searchDate.withYear(number)
                    setUpGanji()
                    etYear.setText(number.toString())
                }
            }

            yearPickerDialog.show()
        }

        etMonth.setOnClickListener {
            val monthPickerDialog = NumberPickerDialog(this@NameActivity)
            monthPickerDialog.maxValue = 12
            monthPickerDialog.minValue = 1
            monthPickerDialog.initialValue = searchDate.monthValue
            monthPickerDialog.onConfirmListener = object : NumberPickerDialog.OnConfirmListener {
                override fun onConfirm(number: Int) {
                    searchDate = searchDate.withMonth(number)
                    setUpGanji()

                    if (number < 10) {
                        etMonth.setText("0$number")
                        return
                    }
                    etMonth.setText(number.toString())
                }
            }

            monthPickerDialog.show()
        }
    }

    private fun loadUserModel() {
        runBlocking {
            launch(IO) {
                val userDao = AppDatabase.getInstance(applicationContext).userDao()
                val userId = intent.getLongExtra(Extras.INTENT_EXTRAS_USER_ID, -1L)
                userModel = userDao.getUser(userId)
                name = userModel.firstName + userModel.lastName

                searchDate =
                    LocalDate.of(userModel.birthYear, userModel.birthMonth, userModel.birthDay)

                binding.etYear.setText(userModel.birthYear.toString())
                var monthLabel = userModel.birthMonth.toString()
                if (userModel.birthMonth < 10) {
                    monthLabel = "0$monthLabel"
                }
                binding.etMonth.setText(monthLabel)
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

    private fun setUpGanji() = with(binding) {
        val type = when (rgBirthType.checkedRadioButtonId) {
            rbYear.id -> NameService.YEAR
            rbMonth.id -> NameService.MONTH
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