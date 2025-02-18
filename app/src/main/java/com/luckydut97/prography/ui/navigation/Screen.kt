package com.luckydut97.prography.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Random : Screen("random")
}