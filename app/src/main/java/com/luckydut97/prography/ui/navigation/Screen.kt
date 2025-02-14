package com.luckydut97.prography.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Random : Screen("random")
    object Detail : Screen("detail/{photoId}") {
        fun createRoute(photoId: String) = "detail/$photoId"
    }
}