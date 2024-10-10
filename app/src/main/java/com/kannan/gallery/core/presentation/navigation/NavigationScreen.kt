package com.kannan.gallery.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationScreen {

    @Serializable
    data object SetupScreen : NavigationScreen

    @Serializable
    data object TimelineScreen : NavigationScreen

    @Serializable
    data object TimelineMediaScreen : NavigationScreen

    @Serializable
    data object AlbumScreen : NavigationScreen

    @Serializable
    data object AlbumTimeLineScreen : NavigationScreen

    @Serializable
    data object AlbumMediaScreen : NavigationScreen

    @Serializable
    data object SettingsScreen : NavigationScreen
}
