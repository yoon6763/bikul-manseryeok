package com.example.manseryeok.page

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.manseryeok.R
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.adapter.LocationAdapter
import com.example.manseryeok.databinding.ActivityCalendarInputBinding
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.UserInputViewModel
import com.example.manseryeok.models.user.User
import com.example.manseryeok.utils.ParentActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar


class CalendarInputActivity : ParentActivity() {

    enum class NextPageType { NAME, CALENDAR }

    private lateinit var userInputViewModel: UserInputViewModel
    private lateinit var binding: ActivityCalendarInputBinding

    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInputViewModel = ViewModelProvider(this)[UserInputViewModel::class.java]
        binding = DataBindingUtil.setContentView<ActivityCalendarInputBinding?>(this, R.layout.activity_calendar_input).apply {
            lifecycleOwner = this@CalendarInputActivity
            viewModel = userInputViewModel
        }

        toolbarSetting()

        binding.run {
            etInputBirthPlace.setOnClickListener { openBirthPlacePicker() }
            btnNameInputFinish.setOnClickListener { nextPage(NextPageType.NAME) }
            btnCalenderInputFinish.setOnClickListener { nextPage(NextPageType.CALENDAR) }
            btnEditFinish.setOnClickListener { }
        }

        setMode()
    }

    private fun openBirthPlacePicker() {
        val locationPickerFragment = LocationPickerFragment()

        locationPickerFragment.onLocationClickListener = object : LocationAdapter.OnLocationClickListener {
            override fun onLocationClick(location: String, timeDiff: Int) {
                userInputViewModel.birthPlace.value = location
                userInputViewModel.timeDiff.value = timeDiff
                userInputViewModel.birthPlaceLabel.value = "$location (${timeDiff}분)"
                locationPickerFragment.dismiss()
            }
        }

        locationPickerFragment.show(supportFragmentManager, "LocationPicker")
    }

    private fun setMode() {
        when (intent.getStringExtra(Utils.INTENT_EXTRAS_INFO_TYPE)) {
            Utils.InfoType.CREATE.value -> {
                binding.containerEdit.visibility = View.GONE
            }
            Utils.InfoType.EDIT.value -> {
                binding.containerNextPage.visibility = View.GONE
                editSetting()
            }
        }
    }

    private fun editSetting() {
        val userId = intent.getLongExtra(Utils.INTENT_EXTRAS_USER_ID, -1)
        var user: User? = null

        runBlocking {
            launch(IO) {
                user = userDao.getUser(userId)
            }
        }

        showShortToast("수정모드 진입")
        showShortToast(user?.firstName + user?.lastName)

        if (user == null) {
            showShortToast("유저 정보를 불러오는데 실패했습니다")
            finish()
        }

        binding.run {
            etFirstName.setText(user?.firstName)
            etName.setText(user?.lastName)
            etInputBirth.setText(Utils.dateSlideFormat.format(user?.getBirthCalculated()?.timeInMillis))

            if (user?.includeTime!!) {
                etInputBirthTime.setText(Utils.timeFormat.format(user?.getBirthCalculated()?.timeInMillis))
            }
        }
    }

    private fun toolbarSetting() {
        setSupportActionBar(binding.toolbarCalendarInput)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun nextPage(type: NextPageType) = with(binding) {
        if(!viewModel!!.isValid(applicationContext)) return@with
        val user = viewModel!!.toUserEntity()

        runBlocking {
            launch(IO) {
                val savedUserId = userDao.insert(user)
                user.id = savedUserId
            }
        }

        val intent = when (type) {
            NextPageType.NAME -> Intent(this@CalendarInputActivity, NameActivity::class.java)
            NextPageType.CALENDAR -> Intent(this@CalendarInputActivity, CalendarActivity::class.java)
        }

        intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, user.id)
        startActivity(intent)
        finish()
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