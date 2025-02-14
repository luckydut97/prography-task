package com.luckydut97.prography.ui.random

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.luckydut97.prography.ui.components.CommonHeader

@Composable
fun RandomScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommonHeader()

        // 여기에 나중에 랜덤 카드 UI 구현 예정
    }
}