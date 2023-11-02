package com.example.manseryeok.page

import android.os.Bundle
import com.example.manseryeok.adapter.userlist.UserGroupCheckBoxAdapter
import com.example.manseryeok.databinding.ActivityGroupBinding
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.dao.group.GroupDAO
import com.example.manseryeok.models.dao.UserDAO
import com.example.manseryeok.models.dao.group.UserGroupDAO
import com.example.manseryeok.models.user.groups.Group
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.user.groups.UserGroup
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
    private lateinit var groupDAO: GroupDAO
    private lateinit var userGroupDAO: UserGroupDAO
    private lateinit var userGroupCheckBoxAdapter: UserGroupCheckBoxAdapter
    private val groups = ArrayList<Group>()
    private val selectedGroups = HashSet<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userDAO = appDatabase.userDao()
        groupDAO = appDatabase.groupDao()
        userGroupDAO = appDatabase.userGroupDAO()

        toolbarSetting()
        loadUser()
        loadGroups()
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
                    val group = Group(0, groupName)
                    val insertedGroupId = groupDAO.insertGroup(group)
                    group.id = insertedGroupId
                    groups.add(group)
                }
            }

            userGroupCheckBoxAdapter.notifyDataSetChanged()
        }
    }

    private fun groupAdapterSetting() = with(binding) {
        userGroupCheckBoxAdapter = UserGroupCheckBoxAdapter(this@GroupActivity, groups, selectedGroups)
        rvGroup.adapter = userGroupCheckBoxAdapter

        userGroupCheckBoxAdapter.onGroupCheckListener = object : UserGroupCheckBoxAdapter.OnGroupCheckListener {
            override fun onGroupCheck(groupId: Long, check: Boolean) {
                runBlocking {
                    launch(IO) {
                        if (check) {
                            selectedGroups.add(groupId)
                            userGroupDAO.insertUserGroup(UserGroup(userModel.id, groupId))
                        } else {
                            selectedGroups.remove(groupId)
                            userGroupDAO.deleteUserGroup(UserGroup(userModel.id, groupId))
                        }
                    }
                }
            }
        }

        userGroupCheckBoxAdapter.notifyDataSetChanged()
    }

    private fun usernameSetting() = with(binding) {
        val birth = Utils.dateSlideFormat.format(userModel.getBirthCalculated().timeInMillis)
        val username = "${userModel.firstName}${userModel.lastName} ($birth)"
        tvUsername.text = username
    }

    private fun loadGroups() {
        runBlocking {
            launch(IO) {
                groupDAO.getAllGroups().forEach {
                    groups.add(it)
                }

                val userGroups = userGroupDAO.getGroupsByUser(userModel.id)
                userGroups.forEach { userGroup ->
                    selectedGroups.add(userGroup.groupId)
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
