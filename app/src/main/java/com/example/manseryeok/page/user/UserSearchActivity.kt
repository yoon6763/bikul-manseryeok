package com.example.manseryeok.page.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.manseryeok.adapter.userlist.UserListAdapter
import com.example.manseryeok.databinding.ActivityUserSearchBinding
import com.example.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.Manseryeok
import com.example.manseryeok.models.user.User
import com.example.manseryeok.utils.ParentActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserSearchActivity : ParentActivity() {

    private val binding by lazy { ActivityUserSearchBinding.inflate(layoutInflater) }
    private val userDAO by lazy { AppDatabase.getInstance(applicationContext).userDao() }
    private val userGroupDAO by lazy { AppDatabase.getInstance(applicationContext).userGroupDAO() }
    private val groupDAO by lazy { AppDatabase.getInstance(applicationContext).groupDao() }
    private val userTagDAO by lazy { AppDatabase.getInstance(applicationContext).userTagDAO() }
    private val tagDAO by lazy { AppDatabase.getInstance(applicationContext).tagDao() }

    private val userList = ArrayList<User>()
    private val manseryeokList = ArrayList<Manseryeok>()

    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            etSearch.addTextChangedListener {
                userSearch(it.toString())
            }

            userListAdapter = UserListAdapter(this@UserSearchActivity, userList, manseryeokList)
            rvSearchList.adapter = userListAdapter
        }
    }

    private fun userSearch(keyword: String) {
        userList.clear()

        runBlocking {
            launch(IO) {

                val manseryeokSQLHelper = ManseryeokSQLHelper(applicationContext)
                manseryeokSQLHelper.createDataBase()
                manseryeokSQLHelper.open()

                val users = userDAO.searchUserByName(keyword)
                users.forEach {
                    userList.add(it)
                    manseryeokList.add(
                        manseryeokSQLHelper.getDayData(
                            it.birthYear, it.birthMonth, it.birthDay
                        )
                    )
                }
            }
        }

        userListAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()


    }
}