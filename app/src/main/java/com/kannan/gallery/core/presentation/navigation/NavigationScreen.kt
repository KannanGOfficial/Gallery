package com.kannan.gallery.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationScreen {
    @Serializable
    data object TimeLineScreen : NavigationScreen

    @Serializable
    data object AlbumScreen : NavigationScreen

    @Serializable
    data object SettingsScreen : NavigationScreen
}
