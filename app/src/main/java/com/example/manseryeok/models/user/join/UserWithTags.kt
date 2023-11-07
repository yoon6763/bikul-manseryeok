package com.example.manseryeok.models.user.join

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.manseryeok.models.user.User
import com.example.manseryeok.models.user.tags.Tag
import com.example.manseryeok.models.user.tags.UserTag

@DatabaseView
data class UserWithTags(
    @Embedded val user: User, // 사용자 정보
    @Relation(
        parentColumn = "userId", // User 엔티티의 기본 키 열
        entity = Tag::class,
        entityColumn = "tagId", // Tag 엔티티의 기본 키 열
        associateBy = Junction(UserTag::class) // 중간 엔티티와의 관계
    )
    val tags: List<Tag> // 사용자의 태그 목록
)