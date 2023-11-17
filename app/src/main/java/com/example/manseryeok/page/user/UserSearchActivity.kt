package com.example.manseryeok.page.user

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.manseryeok.adapter.userlist.OnUserMenuClickListener
import com.example.manseryeok.adapter.userlist.UserListAdapter
import com.example.manseryeok.adapter.userlist.item.UserRVItem
import com.example.manseryeok.databinding.ActivityUserSearchBinding
import com.example.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.user.User
import com.example.manseryeok.page.calendarname.CalendarActivity
import com.example.manseryeok.page.calendarname.CalendarInputActivity
import com.example.manseryeok.page.calendarname.NameActivity
import com.example.manseryeok.utils.ParentActivity
import com.example.manseryeok.utils.Utils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserSearchActivity : ParentActivity() {

    private val binding by lazy { ActivityUserSearchBinding.inflate(layoutInflater) }
    private val userDao by lazy { AppDatabase.getInstance(applicationContext).userDao() }
    private val userGroupDao by lazy { AppDatabase.getInstance(applicationContext).userGroupDAO() }
    private val groupDao by lazy { AppDatabase.getInstance(applicationContext).groupDao() }
    private val userTagDao by lazy { AppDatabase.getInstance(applicationContext).userTagDAO() }
    private val tagDao by lazy { AppDatabase.getInstance(applicationContext).tagDao() }

    private val userRvItems = ArrayList<UserRVItem>()

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

            userListAdapter.useKeywordHighlight = true
        }


        userListAdapter.onUserMenuClickListener = object : OnUserMenuClickListener {
            override fun onManseryeokView(id: Long, position: Int) {
                val intent = Intent(this@UserSearchActivity, CalendarActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, id)
                startActivity(intent)
            }

            override fun onNameView(id: Long, position: Int) {
                val intent = Intent(this@UserSearchActivity, NameActivity::class.java)
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
                val intent = Intent(this@UserSearchActivity, GroupActivity::class.java)
                intent.putExtra(Utils.INTENT_EXTRAS_USER_ID, id)
                startActivity(intent)
            }

            override fun onEditClick(id: Long, position: Int) {
                val intent = Intent(this@UserSearchActivity, CalendarInputActivity::class.java).apply {
                    putExtra(Utils.INTENT_EXTRAS_USER_ID, id)
                    putExtra(Utils.INTENT_EXTRAS_INFO_TYPE, Utils.InfoType.EDIT.value)
                }
                startActivity(intent)
            }
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

                val foundUsersForName = userDao.searchUserByName(keyword)
                foundUsers.addAll(foundUsersForName)

                val foundTags = tagDao.findTagIncludeKeyword(keyword)
                val foundUsersForTag = userTagDao.getUsersByTags(foundTags.map { it.tagId })
                foundUsers.addAll(foundUsersForTag)

                foundUsers.forEach { user ->
                    val userTags = userTagDao.getTagsByUser(user.userId)
                    val userRVItem = UserRVItem(user, manseryeokSQLHelper.getDayData(user.birthYear, user.birthMonth, user.birthDay), userTags)
                    userRvItems.add(userRVItem)
                }
            }
        }

        userListAdapter.highlightKeyword = keyword

        userListAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()


    }
}