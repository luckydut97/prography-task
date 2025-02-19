package com.luckydut97.prography.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val createdAt: Long = System.currentTimeMillis()
)