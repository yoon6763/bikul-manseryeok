package com.example.manseryeok.page

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.example.manseryeok.R
import com.example.manseryeok.utils.Utils
import com.example.manseryeok.adapter.DBListAdapter
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
    private lateinit var dbListAdapter: DBListAdapter
    private val binding by lazy { ActivityDbactivityBinding.inflate(layoutInflater) }
    private val userList = ArrayList<User>()
    private val manseryeokList = ArrayList<Manseryeok>()
    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }

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
            dbListAdapter = DBListAdapter(this@UserDBActivity, userList, manseryeokList)
            dbListAdapter.notifyDataSetChanged()
            rvDbList.adapter = dbListAdapter
        }

        dbListAdapter.onMenuClickListener = object : DBListAdapter.OnMenuClickListener {
            override fun onManseryeokView(ID: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, CalendarActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, userList[position].id)
                startActivity(intent)
            }

            override fun onNameView(ID: Long, position: Int) {
                val intent = Intent(this@UserDBActivity, NameActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, userList[position].id)
                startActivity(intent)
            }

            override fun onDeleteClick(ID: Long, position: Int) {
                runBlocking {
                    launch(IO) {
                        userDao.delete(userList[position])
                        userList.removeAt(position)
                        userList.removeAll { it.id == ID }
                    }
                }
                showShortToast(getString(R.string.msg_delete_complete))
                dbListAdapter.notifyDataSetChanged()
            }

            override fun onGroupClick(ID: Long, position: Int) {
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