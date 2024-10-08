package com.kannan.gallery.core.presentation.navigation.bottomnav

import androidx.annotation.DrawableRes
import com.kannan.gallery.R
import com.kannan.gallery.core.presentation.navigation.NavigationScreen

enum class BottomNavigationItem(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
    val screen: NavigationScreen
) {
    TIMELINE(
        selectedIcon = R.drawable.timeline_selected,
        unSelectedIcon = R.drawable.timeline_unselected,
        screen = NavigationScreen.TimeLineScreen
    ),
    ALBUM(
        selectedIcon = R.drawable.album_selected,
        unSelectedIcon = R.drawable.album_unselected,
        screen = NavigationScreen.AlbumScreen
    ),
    SETTINGS(
        selectedIcon = R.drawable.settings_selected,
        unSelectedIcon = R.drawable.settings_unselected,
        screen = NavigationScreen.SettingsScreen
    )
}
