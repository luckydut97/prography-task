package com.luckydut97.prography.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.luckydut97.prography.R

@Composable
fun TabBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(53.dp)
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Tab
            Image(
                painter = painterResource(
                    id = if (selectedTab == "main") R.drawable.ic_house
                    else R.drawable.ic_house_inactive
                ),
                contentDescription = "Home",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onTabSelected("main") }
            )

            // Random Tab (Cards)
            Image(
                painter = painterResource(
                    id = if (selectedTab == "random") R.drawable.ic_cards
                    else R.drawable.ic_cards_inactive
                ),
                contentDescription = "Random",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onTabSelected("random") }
            )
        }
    }
}