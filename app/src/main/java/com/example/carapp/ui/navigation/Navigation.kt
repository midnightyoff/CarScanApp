package com.example.carapp.ui.navigation

sealed class Screen(val route: String) {
    object Journal : Screen("journal")
    object Main : Screen("main")
    object Settings : Screen("settings")
    object Terminal: Screen("terminal")
}