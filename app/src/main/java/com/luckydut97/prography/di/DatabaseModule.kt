package com.luckydut97.prography.di

import android.content.Context
import com.luckydut97.prography.data.local.AppDatabase
import com.luckydut97.prography.data.local.dao.BookmarkDao
import com.luckydut97.prography.data.repository.PhotoRepository

object DatabaseModule {
    fun provideBookmarkDao(context: Context): BookmarkDao {
        return AppDatabase.getDatabase(context).bookmarkDao()
    }

    fun providePhotoRepository(context: Context): PhotoRepository {
        val bookmarkDao = provideBookmarkDao(context)
        return PhotoRepository(NetworkModule.unsplashApi, bookmarkDao)
    }
}