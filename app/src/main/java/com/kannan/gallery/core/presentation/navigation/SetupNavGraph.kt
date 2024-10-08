package com.kannan.gallery.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kannan.gallery.feature.album.presentation.AlbumScreen
import com.kannan.gallery.feature.settings.presentation.SettingsScreen
import com.kannan.gallery.feature.timeline.presentation.TimelineScreen

@Composable
fun SetupNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: NavigationScreen
) {

    NavHost(
        navController = navHostController,
        modifier = modifier,
        startDestination = startDestination
    ) {

        composable<NavigationScreen.TimeLineScreen> {
            TimelineScreen()
        }

        composable<NavigationScreen.AlbumScreen> {
            AlbumScreen()
        }

        composable<NavigationScreen.SettingsScreen> {
            SettingsScreen()
        }
    }
}
