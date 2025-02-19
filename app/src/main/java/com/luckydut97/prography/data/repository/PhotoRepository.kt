package com.luckydut97.prography.data.repository

import com.luckydut97.prography.data.api.UnsplashApi
import com.luckydut97.prography.data.api.model.UnsplashPhotoDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepository(private val api: UnsplashApi) {
    suspend fun getPhotos(page: Int, perPage: Int = 10): List<UnsplashPhotoDto> = withContext(Dispatchers.IO) {
        api.getPhotos(page = page, perPage = perPage)
    }
}