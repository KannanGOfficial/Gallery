package com.kannan.gallery.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kannan.gallery.feature.album.presentation.AlbumMediaScreen
import com.kannan.gallery.feature.album.presentation.AlbumScreen
import com.kannan.gallery.feature.album.presentation.AlbumTimelineScreen
import com.kannan.gallery.feature.settings.presentation.SettingsScreen
import com.kannan.gallery.feature.setup.presentation.SetupScreen
import com.kannan.gallery.feature.timeline.presentation.TimelineMediaScreen
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

        composable<NavigationScreen.SetupScreen> {
            SetupScreen()
        }

        composable<NavigationScreen.TimelineScreen> {
            TimelineScreen(
                navigateToCallBack = navHostController::navigate
            )
        }

        composable<NavigationScreen.TimelineMediaScreen> {
            TimelineMediaScreen()
        }

        composable<NavigationScreen.AlbumScreen> {
            AlbumScreen(
                navigateToCallBack = navHostController::navigate
            )
        }

        composable<NavigationScreen.AlbumTimeLineScreen> {
            AlbumTimelineScreen(
                navigateToCallBack = navHostController::navigate
            )
        }

        composable<NavigationScreen.AlbumMediaScreen> {
            AlbumMediaScreen()
        }

        composable<NavigationScreen.SettingsScreen> {
            SettingsScreen()
        }
    }
}
