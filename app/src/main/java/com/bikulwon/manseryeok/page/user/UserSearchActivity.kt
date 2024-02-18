package com.bikulwon.manseryeok.page.user

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.bikulwon.manseryeok.adapter.userlist.OnUserMenuClickListener
import com.bikulwon.manseryeok.adapter.userlist.UserListAdapter
import com.bikulwon.manseryeok.adapter.userlist.item.UserRVItem
import com.bikulwon.manseryeok.databinding.ActivityUserSearchBinding
import com.bikulwon.manseryeok.manseryeokdb.ManseryeokSQLHelper
import com.bikulwon.manseryeok.models.AppDatabase
import com.bikulwon.manseryeok.models.user.User
import com.bikulwon.manseryeok.page.calendarname.CalendarActivity
import com.bikulwon.manseryeok.page.calendarname.CalendarInputActivity
import com.bikulwon.manseryeok.page.calendarname.NameActivity
import com.bikulwon.manseryeok.service.calendar.CalendarService
import com.bikulwon.manseryeok.utils.Extras
import com.bikulwon.manseryeok.utils.ParentActivity
import com.bikulwon.manseryeok.utils.Utils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

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

            val calenderService = CalendarService(this@UserSearchActivity, LocalDateTime.now(), false)

            userListAdapter = UserListAdapter(this@UserSearchActivity, userRvItems, calenderService)
            rvSearchList.adapter = userListAdapter

            userListAdapter.useKeywordHighlight = true
        }


        userListAdapter.onUserMenuClickListener = object : OnUserMenuClickListener {
            override fun onManseryeokView(id: Long, position: Int) {
                val intent = Intent(this@UserSearchActivity, CalendarActivity::class.java)
                intent.putExtra(Extras.INTENT_EXTRAS_USER_ID, id)
                startActivity(intent)
            }

            override fun onNameView(id: Long, position: Int) {
                val intent = Intent(this@UserSearchActivity, NameActivity::class.java)
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
                val intent = Intent(this@UserSearchActivity, GroupActivity::class.java)
                intent.putExtra(Extras.INTENT_EXTRAS_USER_ID, id)
                startActivity(intent)
            }

            override fun onEditClick(id: Long, position: Int) {
                val intent = Intent(this@UserSearchActivity, CalendarInputActivity::class.java).apply {
                    putExtra(Extras.INTENT_EXTRAS_USER_ID, id)
                    putExtra(Extras.INTENT_EXTRAS_INFO_TYPE, Utils.InfoType.EDIT.value)
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