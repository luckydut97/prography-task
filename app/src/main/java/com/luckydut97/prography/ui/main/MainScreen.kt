package com.luckydut97.prography.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luckydut97.prography.ui.components.CommonHeader
import com.luckydut97.prography.ui.components.cells.PhotoListLoadingCell
import com.luckydut97.prography.ui.components.layout.StaggeredGrid
import com.luckydut97.prography.ui.detail.DetailDialog


@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val selectedPhotoId = remember { mutableStateOf<String?>(null) }

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

        LazyColumn {  // LazyColumn 추가
            item {
                if (isLoading) {
                    PhotoListLoadingCell()
                } else {
                    Column {  // Column으로 감싸서 순서대로 배치
                        if (bookmarks.isNotEmpty()) {
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
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        StaggeredGrid(
                            photos = photos,
                            onPhotoClick = { photo ->
                                viewModel.selectPhoto(photo)
                                selectedPhotoId.value = photo.id
                            }
                        )
                    }
                }
            }
        }
    }
}