package com.example.manseryeok.page.user

import android.os.Bundle
import com.example.manseryeok.adapter.userlist.group.UserGroupCheckBoxAdapter
import com.example.manseryeok.adapter.userlist.tag.UserTagListAdapter
import com.example.manseryeok.databinding.ActivityGroupBinding
import com.example.manseryeok.models.AppDatabase
import com.example.manseryeok.models.user.groups.Group
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.user.groups.UserGroup
import com.example.manseryeok.models.user.tags.Tag
import com.example.manseryeok.models.user.tags.UserTag
import com.example.manseryeok.utils.ParentActivity
import com.example.manseryeok.utils.Utils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GroupActivity : ParentActivity() {

    private lateinit var userGroupCheckBoxAdapter: UserGroupCheckBoxAdapter
    private lateinit var userTagListAdapter: UserTagListAdapter
    private lateinit var userModel: User

    private val binding by lazy { ActivityGroupBinding.inflate(layoutInflater) }
    private val appDatabase by lazy { AppDatabase.getInstance(applicationContext) }

    private val userDAO by lazy { appDatabase.userDao() }
    private val groupDAO by lazy { appDatabase.groupDao() }
    private val userGroupDAO by lazy { appDatabase.userGroupDAO() }
    private val tagDAO by lazy { appDatabase.tagDao() }
    private val userTagDAO by lazy { appDatabase.userTagDAO() }

    private val groups = ArrayList<Group>()
    private val selectedGroups = HashSet<Long>()

    private val tags = ArrayList<Tag>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toolbarSetting()

        loadUser()
        loadGroups()

        groupAddSetting()
        userinfoSetting()
        groupAdapterSetting()

        loadTags()
        tagAdapterSetting()
        tagAddSetting()

    }

    private fun tagAddSetting() {
        binding.btnTagAdd.setOnClickListener {

            if (binding.etTagAdd.text.isEmpty()) {
                showShortToast("태그 이름을 입력해주세요")
                return@setOnClickListener
            }

            val tagName = binding.etTagAdd.text.toString()
            binding.etTagAdd.text.clear()

            if (tags.any { it.name == tagName }) {
                showShortToast("이미 존재하는 태그입니다")
                return@setOnClickListener
            }

            runBlocking {
                launch(IO) {

                    if (!tagDAO.isTagExist(tagName)) {
                        val tag = Tag(0, tagName)
                        val insertedTagId = tagDAO.insertTag(tag)
                        tag.tagId = insertedTagId
                    }

                    userTagDAO.insertUserTag(UserTag(userModel.userId, tagDAO.getTagByName(tagName)!!.tagId))

                    tags.add(tagDAO.getTagByName(tagName)!!)
                }
            }

            userTagListAdapter.notifyDataSetChanged()
        }
    }

    private fun loadTags() {
        runBlocking {
            launch(IO) {
                userTagDAO.getTagsByUser(userModel.userId).forEach { userTag ->
                    val foundTag = tagDAO.getTagById(userTag.tagId)
                    if (foundTag != null) {
                        tags.add(foundTag)
                    }
                }
            }
        }
    }

    private fun tagAdapterSetting() {
        userTagListAdapter = UserTagListAdapter(applicationContext, tags)
        userTagListAdapter.notifyDataSetChanged()

        binding.rvTag.adapter = userTagListAdapter

        userTagListAdapter.onRemoveClickListener =
            object : UserTagListAdapter.OnRemoveClickListener {
                override fun onRemoveClick(position: Int, tagId: Long) {
                    runBlocking {
                        launch(IO) {
                            tagDAO.deleteTag(tags[position])
                            tags.removeAt(position)
                        }
                    }

                    userTagListAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun groupAddSetting() {
        binding.btnGroupAdd.setOnClickListener {

            if (binding.etGroupAdd.text.isEmpty()) {
                showShortToast("그룹 이름을 입력해주세요")
                return@setOnClickListener
            }

            val groupName = binding.etGroupAdd.text.toString()
            binding.etGroupAdd.text.clear()

            if (groups.any { it.name == groupName }) {
                showShortToast("이미 존재하는 그룹입니다")
                return@setOnClickListener
            }

            runBlocking {
                launch(IO) {
                    val group = Group(0, groupName)
                    val insertedGroupId = groupDAO.insertGroup(group)
                    group.groupId = insertedGroupId
                    groups.add(group)
                }
            }

            userGroupCheckBoxAdapter.notifyDataSetChanged()
        }
    }

    private fun groupAdapterSetting() = with(binding) {
        userGroupCheckBoxAdapter =
            UserGroupCheckBoxAdapter(this@GroupActivity, groups, selectedGroups)
        rvGroup.adapter = userGroupCheckBoxAdapter

        userGroupCheckBoxAdapter.onGroupCheckListener =
            object : UserGroupCheckBoxAdapter.OnGroupCheckListener {
                override fun onGroupCheck(groupId: Long, check: Boolean) {
                    runBlocking {
                        launch(IO) {
                            if (check) {
                                selectedGroups.add(groupId)
                                userGroupDAO.insertUserGroup(UserGroup(userModel.userId, groupId))
                            } else {
                                selectedGroups.remove(groupId)
                                userGroupDAO.deleteUserGroup(UserGroup(userModel.userId, groupId))
                            }
                        }
                    }
                }
            }

        userGroupCheckBoxAdapter.notifyDataSetChanged()
    }

    private fun userinfoSetting() = with(binding) {
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

                val userGroups = userGroupDAO.getGroupsByUser(userModel.userId)
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
