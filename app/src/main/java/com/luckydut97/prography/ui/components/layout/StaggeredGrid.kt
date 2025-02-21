package com.luckydut97.prography.ui.components.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
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
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photo.urls.raw)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)  // 줄 간격 축소
                    ) {
                        Text(
                            text = "titletitletitle",
                            color = Color.White,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "타이틀은최대2줄까지",
                            color = Color.White,
                            fontSize = 10.sp,
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
        val spacing = 4.dp.toPx().toInt()
        val columnWidth = (constraints.maxWidth / columns) - spacing

        // 각 열의 현재 높이를 추적
        val columnHeights = IntArray(columns) { 0 }

        // 각 열의 아이템들을 저장할 리스트
        val columnItems = Array(columns) { mutableListOf<Pair<Placeable, Int>>() }

        // 각 아이템을 측정하고 적절한 열에 배치
        measurables.forEachIndexed { index, measurable ->
            val photo = photos[index]
            val ratio = photo.height.toFloat() / photo.width.toFloat()

            // 이미지 크기 계산
            val itemWidth = columnWidth
            val itemHeight = (itemWidth * ratio).toInt()

            // 아이템 측정
            val placeable = measurable.measure(
                constraints.copy(
                    maxWidth = itemWidth,
                    maxHeight = itemHeight
                )
            )

            // 가장 짧은 열 찾기
            val shortestColumn = columnHeights.withIndex()
                .minByOrNull { it.value }?.index ?: 0

            // 해당 열에 아이템 추가
            columnItems[shortestColumn].add(placeable to itemHeight)
            columnHeights[shortestColumn] += itemHeight + spacing
        }

        // 전체 높이는 가장 긴 열의 높이
        val maxHeight = columnHeights.maxOrNull() ?: 0

        layout(constraints.maxWidth, maxHeight) {
            // 각 열의 아이템들을 배치
            columnItems.forEachIndexed { columnIndex, items ->
                var yPosition = 0
                items.forEach { (placeable, _) ->
                    placeable.place(
                        x = columnIndex * (columnWidth + spacing),
                        y = yPosition
                    )
                    yPosition += placeable.height + spacing
                }
            }
        }
    }
}