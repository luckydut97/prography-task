package com.luckydut97.prography.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.luckydut97.prography.R

@Composable
fun CommonHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_prography),
            contentDescription = "Prography Logo",
            modifier = Modifier
                .width(144.dp)
                .height(24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Divider(
            color = Color(0xFFE9E9E9),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
