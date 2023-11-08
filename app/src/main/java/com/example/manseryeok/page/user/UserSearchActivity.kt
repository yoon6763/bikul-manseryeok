package com.example.manseryeok.page.user

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.manseryeok.adapter.userlist.UserListAdapter
import com.example.manseryeok.adapter.userlist.item.UserRVItem
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

    private val userRvItems = ArrayList<UserRVItem>()
    private val manseryeokList = ArrayList<Manseryeok>()

    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            etSearch.addTextChangedListener {
                userSearch(it.toString())
            }

            userListAdapter = UserListAdapter(this@UserSearchActivity, userRvItems)
            rvSearchList.adapter = userListAdapter
        }
    }

    private fun userSearch(keyword: String) {
        userRvItems.clear()

        if(keyword.isEmpty()) {
            userListAdapter.notifyDataSetChanged()
            return
        }

        runBlocking {
            launch(IO) {

                val manseryeokSQLHelper = ManseryeokSQLHelper(applicationContext)
                manseryeokSQLHelper.createDataBase()
                manseryeokSQLHelper.open()


                val foundUsers = HashSet<User>()

                val foundUsersForName = userDAO.searchUserByName(keyword)
                foundUsers.addAll(foundUsersForName)

                val foundTags = tagDAO.findTagIncludeKeyword(keyword)
                val foundUsersForTag = userTagDAO.getUsersByTags(foundTags.map { it.tagId })
                foundUsers.addAll(foundUsersForTag)

                foundUsers.forEach { user ->
                    val userTags = userTagDAO.getTagsByUser(user.userId)
                    val userRVItem = UserRVItem(user, manseryeokSQLHelper.getDayData(user.birthYear, user.birthMonth, user.birthDay), userTags)
                    userRvItems.add(userRVItem)
                }
            }
        }

        userListAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()


    }
}