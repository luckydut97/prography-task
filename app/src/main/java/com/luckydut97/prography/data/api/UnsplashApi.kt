// data/api/UnsplashApi.kt
package com.luckydut97.prography.data.api

import com.luckydut97.prography.data.api.model.UnsplashPhotoDto
import retrofit2.http.GET
import retrofit2.http.Headers

interface UnsplashApi {
    companion object {
        private const val CLIENT_ID = "7csPXr0PNG3J8DgR1tgbenzuYPJpbFKe2-U8ebTkEuw"
    }

    @Headers("Authorization: Client-ID $CLIENT_ID")
    @GET("photos")
    suspend fun getPhotos(): List<UnsplashPhotoDto>
}
