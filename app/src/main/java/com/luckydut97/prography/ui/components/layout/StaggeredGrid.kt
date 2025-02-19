package com.luckydut97.prography.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onPhotoClick(photo) }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photo.urls.raw)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )

                    // 텍스트 오버레이
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalArrangement = Arrangement.spacedBy(-10.dp) // 줄 간격
                    ) {
                        Text(
                            text = "titletitletitle",
                            color = Color.White,
                            fontSize = 12.sp, // 글자 크기 줄임
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "타이틀은최대2줄까지",
                            color = Color.White, // 흰색으로 통일
                            fontSize = 10.sp, // 더 작은 글자 크기
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
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
            // 텍스트 영역을 고려하여 약간의 높이 추가
            val imageHeight = (imageWidth * ratio) + 48.dp.toPx()

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