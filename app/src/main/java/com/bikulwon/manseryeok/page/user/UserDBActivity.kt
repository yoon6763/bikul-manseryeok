package com.bikulwon.manseryeok.page.user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.core.content.FileProvider
import com.bikulwon.manseryeok.R
import com.bikulwon.manseryeok.adapter.decorator.RecyclerViewDecorator
import com.bikulwon.manseryeok.adapter.userlist.item.UserRVItem
import com.bikulwon.manseryeok.adapter.userlist.group.GroupListAdapter
import com.bikulwon.manseryeok.adapter.userlist.OnUserMenuClickListener
import com.bikulwon.manseryeok.adapter.userlist.item.GroupRVItem
import com.bikulwon.manseryeok.utils.Utils
import com.bikulwon.manseryeok.databinding.ActivityDbactivityBinding
import com.bikulwon.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.bikulwon.manseryeok.models.AppDatabase
import com.bikulwon.manseryeok.page.calendarname.CalendarActivity
import com.bikulwon.manseryeok.page.calendarname.CalendarInputActivity
import com.bikulwon.manseryeok.page.calendarname.NameActivity
import com.bikulwon.manseryeok.service.calendar.CalendarService
import com.bikulwon.manseryeok.utils.Extras
import com.bikulwon.manseryeok.utils.ParentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

class UserDBActivity : ParentActivity(), DBBottomSheetDialogFragment.DBSheetDialogListener {
    private val binding by lazy { ActivityDbactivityBinding.inflate(layoutInflater) }
    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }
    private val userGroupDAO by lazy { AppDatabase.getInstance(applicationContext).userGroupDAO() }
    private val groupGroupDao by lazy { AppDatabase.getInstance(applicationContext).groupDao() }
    private val userTagDAO by lazy { AppDatabase.getInstance(applicationContext).userTagDAO() }
    private val groupRvItems = ArrayList<GroupRVItem>()
    private lateinit var groupListAdapter: GroupListAdapter
    private lateinit var calendarService: CalendarService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        calendarService = CalendarService(this, LocalDateTime.now(), false)

        toolbarSetting()
    }

    private fun toolbarSetting() {
        setSupportActionBar(binding.toolbarDb)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        binding.icSearchButton.setOnClickListener {
            startActivity(Intent(this@UserDBActivity, UserSearchActivity::class.java))
        }

        binding.icBackupButton.setOnClickListener {
            val bottomSheetDialogFragment = DBBottomSheetDialogFragment()
            bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
            bottomSheetDialogFragment.listener = this
        }
    }

    override fun onStart() {
        super.onStart()
        groupRvItems.clear()

        // 유저의 생일 - 1년 부터 + 100년까지의 정보
        val manseryeokSQLHelper = ManseryeokSQLHelper(this)
        manseryeokSQLHelper.createDataBase()
        manseryeokSQLHelper.open()

        runBlocking {
            launch(IO) {
                val allGroups = groupGroupDao.getAllGroups()

                allGroups.forEach { group ->

                    val groupRVItem = GroupRVItem(group.name, ArrayList<UserRVItem>())

                    val usersInThisGroup = userGroupDAO.getUsersByGroup(group.groupId)

                    usersInThisGroup.forEach { user ->
                        val manseryeok = manseryeokSQLHelper.getDayData(
                            user.birthYear,
                            user.birthMonth,
                            user.birthDay
                        )
                        val tags = userTagDAO.getTagsByUser(user.userId)

                        val userRVItem = UserRVItem(user, manseryeok, tags)

                        groupRVItem.userRVItemList.add(userRVItem)
                    }

                    groupRvItems.add(groupRVItem)
                }


                val notGroupRVItem = GroupRVItem("미분류", ArrayList<UserRVItem>())
                val notGroupUsers = userGroupDAO.getUsersWithoutGroup()

                notGroupUsers.forEach { user ->
                    val manseryeok = manseryeokSQLHelper.getDayData(
                        user.birthYear,
                        user.birthMonth,
                        user.birthDay
                    )
                    val tags = userTagDAO.getTagsByUser(user.userId)

                    val userRVItem = UserRVItem(user, manseryeok, tags)

                    notGroupRVItem.userRVItemList.add(userRVItem)
                }

                groupRvItems.add(notGroupRVItem)
            }
        }

        binding.run {
            groupListAdapter =
                GroupListAdapter(this@UserDBActivity, groupRvItems.toList(), calendarService)
            rvDbList.addItemDecoration(RecyclerViewDecorator(30, Color.parseColor("#d9d9d9")))
            groupListAdapter.notifyDataSetChanged()
            rvDbList.adapter = groupListAdapter
        }

        groupListAdapter.setUserMenuClickListener(object : OnUserMenuClickListener {
            override fun onManseryeokView(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, CalendarActivity::class.java)
                intent.putExtra(Extras.INTENT_EXTRAS_USER_ID, id)
                startActivity(intent)
            }

            override fun onNameView(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, NameActivity::class.java)
                intent.putExtra(Extras.INTENT_EXTRAS_USER_ID, id)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Long, position: Int) {
                runBlocking {
                    launch(IO) {
                        val user = userDao.getUser(id)
                        userDao.delete(user)
                    }
                }
                onStart()
            }

            override fun onGroupClick(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, GroupActivity::class.java)
                intent.putExtra(Extras.INTENT_EXTRAS_USER_ID, id)
                startActivity(intent)
            }

            override fun onEditClick(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, CalendarInputActivity::class.java).apply {
                    putExtra(Extras.INTENT_EXTRAS_USER_ID, id)
                    putExtra(Extras.INTENT_EXTRAS_INFO_TYPE, Utils.InfoType.EDIT.value)
                }
                startActivity(intent)
            }
        })


        manseryeokSQLHelper.close()
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

    override fun onBackupDataPressed() {
        AlertDialog.Builder(this, R.style.AlertDialogCustom)
            .setTitle("데이터 복원")
            .setMessage("안드로이드 정책 상 로컬 파일 시스템이 아닌 이메일, 카카오톡, 클라우드 등에 백업을 진행합니다.")
            .setPositiveButton("확인") { _, _ ->
                val backupFile = AppDatabase.copyDatabaseToExternalStorage(this)

                if (backupFile == null) {
                    Toast.makeText(this, "데이터 백업에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val uri = FileProvider.getUriForFile(
                    this,
                    "com.bikulwon.manseryeok.fileprovider",
                    backupFile
                )

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/octet-stream"
                    putExtra(Intent.EXTRA_STREAM, uri)
                }

                startActivity(Intent.createChooser(intent, "데이터 백업"))
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    override fun onLoadDataPressed() {
        AlertDialog.Builder(this, R.style.AlertDialogCustom)
            .setTitle("데이터 복원")
            .setMessage("데이터를 복원하려면 백업했던 DB 파일을 선택하세요.\n(확장자: .db)\n\n * 주의 *\n복원 시 기존 데이터는 삭제됩니다.")
            .setPositiveButton("확인") { _, _ ->
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "application/octet-stream"
                }
                startActivityForResult(
                    Intent.createChooser(intent, "데이터 소스를 선택하세요"),
                    REQUEST_CODE_SELECT_FILE
                )
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SELECT_FILE && resultCode == RESULT_OK) {
            val uri = data?.data

            AppDatabase.restoreDatabase(this, uri)
        }
    }


    companion object {
        private const val REQUEST_CODE_SELECT_FILE = 100
    }
}