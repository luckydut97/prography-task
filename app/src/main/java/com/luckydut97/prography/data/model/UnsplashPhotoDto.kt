package com.luckydut97.prography.data.api.model

import com.google.gson.annotations.SerializedName

data class UnsplashPhotoDto(
    @SerializedName("id") val id: String,
    @SerializedName("urls") val urls: UnsplashPhotoUrls,
    @SerializedName("width") val width: Int,  // 추가
    @SerializedName("height") val height: Int  // 추가
)

data class UnsplashPhotoUrls(
    @SerializedName("raw") val raw: String
)