package com.kannan.gallery.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationScreen {

    @Serializable
    data object SetupScreen : NavigationScreen

    @Serializable
    data object PhotoScreen : NavigationScreen

    @Serializable
    data object AlbumScreen : NavigationScreen

    @Serializable
    data class AlbumDetailScreen(
        val albumName: String,
        val albumId: Long
    ) : NavigationScreen

    @Serializable
    data object SettingsScreen : NavigationScreen
}
