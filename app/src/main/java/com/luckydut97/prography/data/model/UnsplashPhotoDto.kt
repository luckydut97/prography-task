package com.luckydut97.prography.data.api.model

import com.google.gson.annotations.SerializedName

data class UnsplashPhotoDto(
    @SerializedName("id") val id: String,
    @SerializedName("urls") val urls: UnsplashPhotoUrls
)

data class UnsplashPhotoUrls(
    @SerializedName("raw") val raw: String
)