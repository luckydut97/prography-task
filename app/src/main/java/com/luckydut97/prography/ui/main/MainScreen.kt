package com.luckydut97.prography.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luckydut97.prography.ui.components.CommonHeader
import com.luckydut97.prography.ui.components.cells.PhotoListLoadingCell

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommonHeader()

        if (isLoading) {
            PhotoListLoadingCell()
        } else {
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
                                    .background(Color(0xFFF0F0F0))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(photos.size) { index ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(Color(0xFFC4C4C4))
                    )
                }
            }
        }
    }
}