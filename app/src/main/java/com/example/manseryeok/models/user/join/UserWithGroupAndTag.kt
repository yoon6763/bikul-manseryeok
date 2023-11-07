package com.example.manseryeok.models.user.join

import androidx.room.Embedded
import androidx.room.Relation
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.user.groups.Group
import com.example.manseryeok.models.user.groups.UserGroup
import com.example.manseryeok.models.user.tags.Tag
import com.example.manseryeok.models.user.tags.UserTag

data class UserWithGroupAndTag(

    @Embedded val user: User,

    @Relation(
        parentColumn = "userId",
        entity = Group::class,
        entityColumn = "groupId",
        associateBy = androidx.room.Junction(UserGroup::class)
    ) val groups: List<Group>,

    @Relation(
        parentColumn = "userId",
        entity = Tag::class,
        entityColumn = "tagId",
        associateBy = androidx.room.Junction(UserTag::class)
    ) val tags: List<Tag>

)