package com.luckydut97.prography

import android.app.Application
import com.luckydut97.prography.data.local.AppDatabase
import com.luckydut97.prography.data.local.dao.BookmarkDao
import com.luckydut97.prography.data.repository.PhotoRepository
import com.luckydut97.prography.di.NetworkModule

class PrographyApplication : Application() {
    // 나중에 사용할 수 있도록 데이터베이스와 DAO를 늦은 초기화로 선언
    lateinit var database: AppDatabase
        private set

    lateinit var bookmarkDao: BookmarkDao
        private set

    lateinit var photoRepository: PhotoRepository
        private set

    override fun onCreate() {
        super.onCreate()
        // 데이터베이스 초기화
        database = AppDatabase.getDatabase(this)
        bookmarkDao = database.bookmarkDao()

        // Repository 초기화
        photoRepository = PhotoRepository(NetworkModule.unsplashApi, bookmarkDao)
    }
}