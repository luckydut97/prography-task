package com.luckydut97.prography.ui.components.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luckydut97.prography.data.api.model.UnsplashPhotoDto

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    photos: List<UnsplashPhotoDto>,
    onPhotoClick: (UnsplashPhotoDto) -> Unit
) {
    Layout(
        content = {
            photos.forEach { photo ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photo.urls.raw)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onPhotoClick(photo) },
                    contentScale = ContentScale.FillWidth
                )
            }
        },
        modifier = modifier.padding(horizontal = 8.dp)
    ) { measurables, constraints ->
        val columns = 2
        val columnWidths = IntArray(columns) { constraints.maxWidth / columns }
        val columnHeights = IntArray(columns) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            val photo = photos[index]
            val ratio = photo.height.toFloat() / photo.width.toFloat()

            val imageWidth = columnWidths[0] - 16.dp.toPx()
            val imageHeight = imageWidth * ratio

            val columnIndex = columnHeights.indexOf(columnHeights.minOrNull()!!)

            columnHeights[columnIndex] += imageHeight.toInt() + 8.dp.toPx().toInt()

            measurable.measure(constraints.copy(
                maxWidth = imageWidth.toInt(),
                maxHeight = imageHeight.toInt()
            )) to columnIndex
        }

        val height = columnHeights.maxOrNull()!!.toInt()

        layout(constraints.maxWidth, height) {
            val columnY = IntArray(columns) { 0 }

            placeables.forEach { (placeable, column) ->
                placeable.place(
                    x = column * columnWidths[0],
                    y = columnY[column]
                )
                columnY[column] += placeable.height + 8.dp.toPx().toInt()
            }
        }
    }
}