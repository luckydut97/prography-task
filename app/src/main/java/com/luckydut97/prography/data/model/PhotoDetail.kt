package com.luckydut97.prography.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoDetail(
    val id: String,
    val imageUrl: String
) : Parcelable