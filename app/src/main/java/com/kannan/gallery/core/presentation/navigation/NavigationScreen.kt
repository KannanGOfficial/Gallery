package com.kannan.gallery.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationScreen {

    @Serializable
    data object SetupScreen : NavigationScreen

    @Serializable
    data object TimelineScreen : NavigationScreen

    @Serializable
    data class TimelineMediaScreen(val initialPagerPosition: Int) : NavigationScreen

    @Serializable
    data object AlbumScreen : NavigationScreen

    @Serializable
    data class AlbumTimeLineScreen(val albumName: String) : NavigationScreen

    @Serializable
    data class AlbumMediaScreen(val initialPagerPosition: Int) : NavigationScreen

    @Serializable
    data object SettingsScreen : NavigationScreen
}
