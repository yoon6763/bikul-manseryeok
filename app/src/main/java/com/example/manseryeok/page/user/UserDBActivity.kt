package com.example.manseryeok.page.user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import com.example.manseryeok.adapter.decorator.RecyclerViewDecorator
import com.example.manseryeok.adapter.userlist.item.UserRVItem
import com.example.manseryeok.adapter.userlist.group.GroupListAdapter
import com.example.manseryeok.adapter.userlist.OnUserMenuClickListener
import com.example.manseryeok.adapter.userlist.item.GroupRVItem
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.databinding.ActivityDbactivityBinding
import com.example.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.dao.tag.UserTagDAO
import com.example.manseryeok.models.user.User
import com.example.manseryeok.page.CalendarActivity
import com.example.manseryeok.page.CalendarInputActivity
import com.example.manseryeok.page.NameActivity
import com.example.manseryeok.utils.ParentActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserDBActivity : ParentActivity() {
    private val binding by lazy { ActivityDbactivityBinding.inflate(layoutInflater) }
    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }
    private val userGroupDAO by lazy { AppDatabase.getInstance(applicationContext).userGroupDAO() }
    private val groupGroupDao by lazy { AppDatabase.getInstance(applicationContext).groupDao() }
    private val userTagDAO by lazy { AppDatabase.getInstance(applicationContext).userTagDAO() }
    private val groupRvItems = ArrayList<GroupRVItem>()
    private lateinit var groupListAdapter: GroupListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                        val manseryeok = manseryeokSQLHelper.getDayData(user.birthYear, user.birthMonth, user.birthDay)
                        val tags = userTagDAO.getTagsByUser(user.userId)

                        val userRVItem = UserRVItem(user, manseryeok, tags)

                        groupRVItem.userRVItemList.add(userRVItem)
                    }

                    groupRvItems.add(groupRVItem)
                }



                val notGroupRVItem = GroupRVItem("미분류", ArrayList<UserRVItem>())
                val notGroupUsers = userGroupDAO.getUsersWithoutGroup()

                notGroupUsers.forEach { user ->
                    val manseryeok = manseryeokSQLHelper.getDayData(user.birthYear, user.birthMonth, user.birthDay)
                    val tags = userTagDAO.getTagsByUser(user.userId)

                    val userRVItem = UserRVItem(user, manseryeok, tags)

                    notGroupRVItem.userRVItemList.add(userRVItem)
                }

                groupRvItems.add(notGroupRVItem)
            }
        }

        binding.run {
            groupListAdapter = GroupListAdapter(this@UserDBActivity, groupRvItems.toList())
            rvDbList.addItemDecoration(RecyclerViewDecorator(30, Color.parseColor("#d9d9d9")))
            groupListAdapter.notifyDataSetChanged()
            rvDbList.adapter = groupListAdapter
        }

        groupListAdapter.setUserMenuClickListener(object : OnUserMenuClickListener {
            override fun onManseryeokView(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, CalendarActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, id)
                startActivity(intent)
            }

            override fun onNameView(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, NameActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, id)
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
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, id)
                startActivity(intent)
            }

            override fun onEditClick(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, CalendarInputActivity::class.java).apply {
                    putExtra(Utils.INTENT_EXTRAS_USER_ID, id)
                    putExtra(Utils.INTENT_EXTRAS_INFO_TYPE, Utils.InfoType.EDIT.value)
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
}