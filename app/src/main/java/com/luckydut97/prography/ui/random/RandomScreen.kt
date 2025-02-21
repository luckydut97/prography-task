package com.luckydut97.prography.ui.random

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.luckydut97.prography.R
import com.luckydut97.prography.ui.components.CommonHeader
import com.luckydut97.prography.ui.detail.DetailDialog
import com.luckydut97.prography.ui.main.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RandomScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val photos by viewModel.photos.collectAsState()
    val bookmarkedPhotoIds by viewModel.bookmarkedPhotoIds.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    var showDetailDialog by remember { mutableStateOf(false) }
    val currentPhoto = if (photos.isNotEmpty() && currentIndex < photos.size) photos[currentIndex] else null

    // 스와이핑 관련 상태
    var dragOffset by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        CommonHeader()

        if (photos.isEmpty()) {
            // 로딩 상태 표시
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // 카드 UI
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // 현재 카드 - 흰색 배경 카드
                currentPhoto?.let { photo ->
                    val isBookmarked = bookmarkedPhotoIds.contains(photo.id)

                    // 외부 흰색 카드
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.9f)
                            .offset(x = dragOffset.dp)
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures(
                                    onDragStart = { isDragging = true },
                                    onDragEnd = {
                                        coroutineScope.launch {
                                            // 왼쪽으로 스와이프 - 그냥 다음 카드로 넘어감
                                            if (dragOffset < -100) {
                                                if (currentIndex < photos.size - 1) {
                                                    val steps = 10
                                                    val targetOffset = -500f
                                                    val stepSize = (targetOffset - dragOffset) / steps

                                                    repeat(steps) {
                                                        dragOffset += stepSize
                                                        delay(5)
                                                    }
                                                    currentIndex++
                                                    dragOffset = 0f
                                                } else {
                                                    dragOffset = 0f
                                                }
                                            }
                                            // 오른쪽으로 스와이프 - 북마크에 저장하고 다음 카드로
                                            else if (dragOffset > 100) {
                                                // 북마크 추가
                                                if (!isBookmarked) {
                                                    viewModel.toggleBookmark(photo)
                                                }

                                                // 다음 카드로 넘어가기
                                                if (currentIndex < photos.size - 1) {
                                                    val steps = 10
                                                    val targetOffset = 500f
                                                    val stepSize = (targetOffset - dragOffset) / steps

                                                    repeat(steps) {
                                                        dragOffset += stepSize
                                                        delay(5)
                                                    }
                                                    currentIndex++
                                                    dragOffset = 0f
                                                } else {
                                                    dragOffset = 0f
                                                }
                                            } else {
                                                // 드래그 거리가 충분하지 않으면 원위치
                                                dragOffset = 0f
                                            }
                                            isDragging = false
                                        }
                                    },
                                    onDragCancel = {
                                        isDragging = false
                                        dragOffset = 0f
                                    },
                                    onHorizontalDrag = { _, dragAmount ->
                                        // 드래그 중 카드 이동
                                        dragOffset += dragAmount
                                        // 너무 많이 드래그되지 않도록 제한
                                        dragOffset = dragOffset.coerceIn(-200f, 200f)
                                    }
                                )
                            },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // 상단 검은색 이미지 영역 (카드 높이의 75%를 차지)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.75f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Black)
                            ) {
                                // 이미지
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(photo.urls.raw)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 80.dp),
                                    contentScale = ContentScale.Crop,
                                    loading = {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = Color.White)
                                        }
                                    }
                                )
                            }

                            // 하단 버튼 영역 (카드 높이의 25%를 차지)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.25f),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 패스 버튼 (왼쪽)
                                    IconButton(
                                        onClick = {
                                            if (currentIndex < photos.size - 1) {
                                                currentIndex++
                                            }
                                        },
                                        modifier = Modifier
                                            .size(60.dp)
                                            .padding(8.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_random_pass),
                                            contentDescription = "Pass",
                                            modifier = Modifier.size(44.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(20.dp))

                                    // 북마크 버튼 (가운데)
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                viewModel.toggleBookmark(photo)
                                                delay(100) // 북마크 상태 업데이트 대기

                                                // 북마크 후 자동으로 다음 카드로 넘어가기
                                                if (currentIndex < photos.size - 1) {
                                                    currentIndex++
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .size(76.dp)
                                            .padding(8.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_random_bookmark),
                                            contentDescription = "Bookmark",
                                            modifier = Modifier.size(60.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(20.dp))

                                    // 정보 버튼 (오른쪽)
                                    IconButton(
                                        onClick = { showDetailDialog = true },
                                        modifier = Modifier
                                            .size(60.dp)
                                            .padding(8.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_info),
                                            contentDescription = "Info",
                                            modifier = Modifier.size(44.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 상세 정보 다이얼로그
    if (showDetailDialog && currentPhoto != null) {
        DetailDialog(
            photoId = currentPhoto.id,
            onDismiss = { showDetailDialog = false },
            viewModel = viewModel
        )
    }
}