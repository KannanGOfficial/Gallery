package com.kannan.gallery.core.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kannan.gallery.core.presentation.sharedViewModel.TimelineSharedViewModel
import com.kannan.gallery.feature.album.presentation.AlbumMediaScreen
import com.kannan.gallery.feature.album.presentation.AlbumScreen
import com.kannan.gallery.feature.album.presentation.AlbumScreenViewModel
import com.kannan.gallery.feature.album.presentation.AlbumTimelineScreen
import com.kannan.gallery.feature.settings.presentation.SettingsScreen
import com.kannan.gallery.feature.setup.presentation.SetupScreen
import com.kannan.gallery.feature.setup.presentation.SetupScreenViewModel
import com.kannan.gallery.feature.timeline.presentation.TimelineMediaScreen
import com.kannan.gallery.feature.timeline.presentation.TimelineMediaScreenViewModel
import com.kannan.gallery.feature.timeline.presentation.TimelineScreen
import com.kannan.gallery.feature.timeline.presentation.TimelineScreenViewModel
import com.kannan.gallery.ui.theme.navigateFromSetupScreen
import com.kannan.gallery.ui.theme.navigateTo

@Composable
fun SetupNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: NavigationScreen
) {
    val timelineSharedViewModel = viewModel<TimelineSharedViewModel>()

    NavHost(
        navController = navHostController,
        modifier = modifier,
        startDestination = startDestination
    ) {

        composable<NavigationScreen.SetupScreen> {
            val viewModel = viewModel<SetupScreenViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            SetupScreen(
                modifier = Modifier.fillMaxSize(),
                uiEvent = viewModel.uiEvent,
                uiState = uiState,
                uiAction = viewModel::onUiAction,
                navigateToCallback = navHostController::navigateFromSetupScreen
            )
        }

        composable<NavigationScreen.TimelineScreen> {
            val viewModel = viewModel<TimelineScreenViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            TimelineScreen(
                uiState = uiState,
                uiAction = viewModel::onUiAction,
                uiEvent = viewModel.uiEvent,
                timelineMedia = timelineSharedViewModel.timelineMediaList,
                navigateToCallBack = navHostController::navigateTo
            )
        }

        composable<NavigationScreen.TimelineMediaScreen> { backStackEntry ->
            val viewModel = viewModel<TimelineMediaScreenViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            TimelineMediaScreen(
                modifier = modifier,
                uiState = uiState,
                timelineMediaList = timelineSharedViewModel.timelineMediaList
            )
        }

        composable<NavigationScreen.AlbumScreen> {
            val viewModel = viewModel<AlbumScreenViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            AlbumScreen(
                uiState = uiState,
                uiEvent = viewModel.uiEvent,
                uiAction = viewModel::onUiAction,
                navigateToCallBack = navHostController::navigateTo
            )
        }

        composable<NavigationScreen.AlbumTimeLineScreen> {
            AlbumTimelineScreen(
                navigateToCallBack = navHostController::navigateTo
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
