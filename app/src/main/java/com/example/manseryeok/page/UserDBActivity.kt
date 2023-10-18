package com.example.manseryeok.page

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.example.manseryeok.R
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.adapter.userlist.UserListAdapter
import com.example.manseryeok.databinding.ActivityDbactivityBinding
import com.example.manseryeok.db.ManseryeokSQLHelper
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.user.User
import com.example.manseryeok.utils.ParentActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserDBActivity : ParentActivity() {
    private lateinit var userListAdapter: UserListAdapter
    private val binding by lazy { ActivityDbactivityBinding.inflate(layoutInflater) }
    private val userList = ArrayList<User>()
    private val manseryeokList = ArrayList<Manseryeok>()
    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }
    private val userTagDao by lazy { AppDatabase.getInstance(applicationContext).userTagDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDb)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onStart() {
        super.onStart()

        userList.clear()
        manseryeokList.clear()


        // 유저의 생일 - 1년 부터 + 100년까지의 정보
        val manseryeokSQLHelper = ManseryeokSQLHelper(this)
        manseryeokSQLHelper.createDataBase()
        manseryeokSQLHelper.open()

        runBlocking {
            launch(IO) {
                val allUserList = userDao.getAllUser()

                allUserList.forEach { user ->
                    userList.add(user)
                    manseryeokList.add(
                        manseryeokSQLHelper.getDayData(
                            user.birthYear,
                            user.birthMonth,
                            user.birthDay
                        )
                    )
                }
            }
        }

        binding.run {
            userListAdapter = UserListAdapter(this@UserDBActivity, userList, manseryeokList)
            userListAdapter.notifyDataSetChanged()
            rvDbList.adapter = userListAdapter
        }

        userListAdapter.onMenuClickListener = object : UserListAdapter.OnMenuClickListener {
            override fun onManseryeokView(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, CalendarActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, userList[position].id)
                startActivity(intent)
            }

            override fun onNameView(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, NameActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, userList[position].id)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Long, position: Int) {
                runBlocking {
                    launch(IO) {
                        userDao.delete(userList[position])
                        userList.removeAll { it.id == id }
                    }
                }
                showShortToast(getString(R.string.msg_delete_complete))
                userListAdapter.notifyDataSetChanged()
            }

            override fun onGroupClick(id: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, GroupActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, userList[position].id)
                startActivity(intent)
            }
        }
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