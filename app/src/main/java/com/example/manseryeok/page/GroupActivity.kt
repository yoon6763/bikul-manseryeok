package com.example.manseryeok.page

import android.os.Bundle
import com.example.manseryeok.adapter.userlist.UserTagCheckBoxAdapter
import com.example.manseryeok.databinding.ActivityGroupBinding
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.dao.GroupTagDAO
import com.example.manseryeok.models.dao.UserDAO
import com.example.manseryeok.models.dao.UserTagDAO
import com.example.manseryeok.models.user.GroupTag
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.user.UserTag
import com.example.manseryeok.utils.ParentActivity
import com.example.manseryeok.utils.Utils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GroupActivity : ParentActivity() {
    private val binding by lazy { ActivityGroupBinding.inflate(layoutInflater) }
    private val appDatabase by lazy { AppDatabase.getInstance(applicationContext) }
    private lateinit var userModel: User
    private lateinit var userDAO: UserDAO
    private lateinit var groupTagDAO: GroupTagDAO
    private lateinit var userTagDAO: UserTagDAO
    private lateinit var userTagCheckBoxAdapter: UserTagCheckBoxAdapter
    private val groupTags = ArrayList<GroupTag>()
    private val selectedTags = HashSet<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userDAO = appDatabase.userDao()
        groupTagDAO = appDatabase.groupTagDao()
        userTagDAO = appDatabase.userTagDao()

        toolbarSetting()
        loadUser()
        loadGroupTag()
        groupAddSetting()
        usernameSetting()
        groupAdapterSetting()
    }

    private fun groupAddSetting() {
        binding.btnGroupAdd.setOnClickListener {

            if (binding.etGroupAdd.text.isEmpty()) {
                showShortToast("그룹 이름을 입력해주세요")
                return@setOnClickListener
            }

            val groupName = binding.etGroupAdd.text.toString()
            binding.etGroupAdd.text.clear()

            runBlocking {
                launch(IO) {
                    val groupTag = GroupTag(0, groupName)
                    val insertedTagId = groupTagDAO.insertTag(groupTag)
                    groupTag.id = insertedTagId
                    groupTags.add(groupTag)
                }
            }

            userTagCheckBoxAdapter.notifyDataSetChanged()
        }
    }

    private fun groupAdapterSetting() = with(binding) {
        userTagCheckBoxAdapter = UserTagCheckBoxAdapter(this@GroupActivity, groupTags, selectedTags)
        rvGroup.adapter = userTagCheckBoxAdapter

        userTagCheckBoxAdapter.onGroupCheckListener = object : UserTagCheckBoxAdapter.OnGroupCheckListener {
            override fun onGroupCheck(groupId: Long, check: Boolean) {
                runBlocking {
                    launch(IO) {
                        if (check) {
                            selectedTags.add(groupId)
                            userTagDAO.insertUserTag(UserTag(userModel.id, groupId))
                        } else {
                            selectedTags.remove(groupId)
                            userTagDAO.deleteUserTag(UserTag(userModel.id, groupId))
                        }
                    }
                }
            }
        }

        userTagCheckBoxAdapter.notifyDataSetChanged()
    }

    private fun usernameSetting() = with(binding) {
        val birth = Utils.dateSlideFormat.format(userModel.getBirthCalculated().timeInMillis)
        val username = "${userModel.firstName}${userModel.lastName} ($birth)"
        tvUsername.text = username
    }

    private fun loadGroupTag() {
        runBlocking {
            launch(IO) {
                groupTagDAO.getAllTags().forEach {
                    groupTags.add(it)
                }

                val userTags = userTagDAO.getTagsByUser(userModel.id)
                userTags.forEach { userTag ->
                    selectedTags.add(userTag.tagId)
                }
            }
        }
    }

    private fun loadUser() {
        val userId = intent.getLongExtra(Utils.INTENT_EXTRAS_USER_ID, 0)

        runBlocking {
            launch(IO) {
                userModel = userDAO.getUser(userId)
            }
        }
    }

    private fun toolbarSetting() {
        setSupportActionBar(binding.toolbarGroup)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
