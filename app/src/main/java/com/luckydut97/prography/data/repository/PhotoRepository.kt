package com.luckydut97.prography.data.repository

import com.luckydut97.prography.data.api.UnsplashApi
import com.luckydut97.prography.data.api.model.UnsplashPhotoDto
import com.luckydut97.prography.data.api.model.UnsplashPhotoUrls
import com.luckydut97.prography.data.local.dao.BookmarkDao
import com.luckydut97.prography.data.local.entity.BookmarkEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PhotoRepository(
    private val api: UnsplashApi,
    private val bookmarkDao: BookmarkDao
) {
    suspend fun getPhotos(page: Int, perPage: Int = 10): List<UnsplashPhotoDto> = withContext(Dispatchers.IO) {
        api.getPhotos(page = page, perPage = perPage)
    }

    // 북마크 관련 메서드
    suspend fun addBookmark(photo: UnsplashPhotoDto) {
        println("*디버깅 Repository: 북마크 추가 시작 - ID: ${photo.id}")
        bookmarkDao.insertBookmark(
            BookmarkEntity(
                id = photo.id,
                imageUrl = photo.urls.raw,
                width = photo.width,
                height = photo.height
            )
        )
        println("*디버깅 Repository: 북마크 추가 완료")
    }

    suspend fun removeBookmark(photoId: String) {
        println("*디버깅 Repository: 북마크 제거 시작 - ID: $photoId")
        bookmarkDao.deleteBookmarkById(photoId)
        println("*디버깅 Repository: 북마크 제거 완료")
    }


    suspend fun isBookmarked(photoId: String): Boolean {
        return bookmarkDao.isBookmarked(photoId)
    }

    fun getAllBookmarks(): Flow<List<BookmarkEntity>> {
        return bookmarkDao.getAllBookmarks()
    }

    // Entity를 DTO로 변환하는 함수
    fun mapEntityToDto(entity: BookmarkEntity): UnsplashPhotoDto {
        return UnsplashPhotoDto(
            id = entity.id,
            urls = UnsplashPhotoUrls(raw = entity.imageUrl),
            width = entity.width,
            height = entity.height
        )
    }
}