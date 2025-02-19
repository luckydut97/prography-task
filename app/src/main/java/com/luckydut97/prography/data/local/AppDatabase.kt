package com.luckydut97.prography.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.luckydut97.prography.data.local.dao.BookmarkDao
import com.luckydut97.prography.data.local.entity.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prography_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}