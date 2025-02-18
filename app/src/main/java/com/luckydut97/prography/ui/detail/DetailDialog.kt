package com.luckydut97.prography.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luckydut97.prography.ui.main.MainViewModel
import com.luckydut97.prography.R


@Composable
fun DetailDialog(
    photoId: String,
    onDismiss: () -> Unit,
    viewModel: MainViewModel
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
        ) {
            val photo = viewModel.getPhotoById(photoId)

            photo?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    verticalArrangement = Arrangement.SpaceBetween // 상하단 분리
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onDismiss) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = "Close",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Text(
                                text = "UserName",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(onClick = { /* 저장 기능 구현 전 */ }) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_save),
                                    contentDescription = "Save",
                                    modifier = Modifier.size(34.dp)
                                )
                            }

                            var isBookmarked by remember { mutableStateOf(false) }
                            IconButton(onClick = { isBookmarked = !isBookmarked }) {
                                Image(
                                    painter = painterResource(
                                        id = if (isBookmarked)
                                            R.drawable.ic_bookmark
                                        else
                                            R.drawable.ic_bookmark_inactive
                                    ),
                                    contentDescription = "Bookmark",
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }
                    }

                    // Center Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photo.urls.raw)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    // Bottom Content
                    // Bottom Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)  // background 제거하고 padding만 유지
                    ) {
                        Text(
                            text = "Title",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "description\ndescription은 최대 2줄\n#tag #tag # tag #tag",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}