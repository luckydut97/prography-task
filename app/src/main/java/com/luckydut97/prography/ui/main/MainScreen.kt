package com.luckydut97.prography.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luckydut97.prography.ui.components.CommonHeader
import com.luckydut97.prography.ui.components.cells.PhotoListLoadingCell
import com.luckydut97.prography.ui.components.layout.StaggeredGrid
import com.luckydut97.prography.ui.detail.DetailDialog
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val selectedPhotoId = remember { mutableStateOf<String?>(null) }

    // 디버깅용 로그 추가
    LaunchedEffect(photos.size) {
        println("현재 사진 개수: ${photos.size}")
    }

    // 스크롤 상태 관리
    val listState = rememberLazyListState()

    // 스크롤 위치 디버깅
    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        println("스크롤 위치: ${listState.firstVisibleItemIndex}, 오프셋: ${listState.firstVisibleItemScrollOffset}")
        println("보이는 아이템: ${listState.layoutInfo.visibleItemsInfo.map { it.index }}")
    }

    // 스크롤이 마지막 아이템에 도달했는지 감지 (수정된 로직)
    val isAtBottom by remember {
        derivedStateOf {
            // 디버깅 정보 출력
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            val totalItems = listState.layoutInfo.totalItemsCount

            if (totalItems == 0 || visibleItems.isEmpty()) {
                false
            } else {
                val lastVisibleItem = visibleItems.last()
                val isLastItemVisible = lastVisibleItem.index >= totalItems - 1
                val isScrolledToEnd = lastVisibleItem.offset + lastVisibleItem.size <= listState.layoutInfo.viewportEndOffset + 200

                println("마지막 아이템: ${lastVisibleItem.index}, 전체 아이템: $totalItems")
                println("마지막 보이는지: $isLastItemVisible, 스크롤 끝: $isScrolledToEnd")

                isLastItemVisible && isScrolledToEnd
            }
        }
    }

    // 스크롤이 마지막에 도달하면 추가 로드 (수정됨)
    LaunchedEffect(isAtBottom) {
        if (isAtBottom) {
            println("바닥 도달 감지: 로딩=$isLoading, 더 로딩=$isLoadingMore, 사진=${photos.size}")
            if (!isLoading && !isLoadingMore && photos.isNotEmpty()) {
                println("추가 로딩 시작!")
                viewModel.loadMorePhotos()
            }
        }
    }

    selectedPhotoId.value?.let { photoId ->
        DetailDialog(
            photoId = photoId,
            onDismiss = { selectedPhotoId.value = null },
            viewModel = viewModel
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommonHeader()

        /*// 이미지 로드 개수 확인 디버깅 로그
        if (!isLoading) {
            Text(
                text = "로드된 사진: ${photos.size}개",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                color = Color.Gray
            )
        }*/

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                item {
                    PhotoListLoadingCell()
                }
            } else {
                // 북마크 섹션
                if (bookmarks.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "북마크",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(bookmarks.size.coerceAtMost(3)) { index ->
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFF0F0F0))
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // 최신 이미지 제목
                item {
                    Text(
                        text = "최신 이미지",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }

                // 이미지 그리드 - 별도 아이템으로 분리
                item {
                    StaggeredGrid(
                        photos = photos,
                        onPhotoClick = { photo ->
                            viewModel.selectPhoto(photo)
                            selectedPhotoId.value = photo.id
                        }
                    )
                }
            }

            // 로딩 인디케이터
            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // 스크롤 감지 위한 추가 아이템
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
        }
    }
}