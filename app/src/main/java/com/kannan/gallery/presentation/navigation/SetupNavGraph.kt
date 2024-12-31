package com.kannan.gallery.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kannan.gallery.presentation.components.MemoriesScreen
import com.kannan.gallery.presentation.components.MemoriesViewModel
import com.kannan.gallery.presentation.feature.album.AlbumMediaScreen
import com.kannan.gallery.presentation.feature.album.AlbumMediaScreenViewModel
import com.kannan.gallery.presentation.feature.album.AlbumScreen
import com.kannan.gallery.presentation.feature.album.AlbumScreenViewModel
import com.kannan.gallery.presentation.feature.album.AlbumTimelineScreen
import com.kannan.gallery.presentation.feature.album.AlbumTimelineScreenViewModel
import com.kannan.gallery.presentation.feature.photo.TimelineMediaScreen
import com.kannan.gallery.presentation.feature.photo.TimelineMediaScreenViewModel
import com.kannan.gallery.presentation.feature.photo.TimelineScreen
import com.kannan.gallery.presentation.feature.photo.TimelineScreenViewModel
import com.kannan.gallery.presentation.feature.settings.SettingsScreen
import com.kannan.gallery.presentation.feature.setup.SetupScreen
import com.kannan.gallery.presentation.feature.setup.SetupScreenViewModel
import com.kannan.gallery.presentation.main.AlbumSharedViewModel
import com.kannan.gallery.presentation.main.TimelineSharedViewModel
import com.kannan.gallery.ui.theme.navigateFromSetupScreen
import com.kannan.gallery.ui.theme.navigateTo

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SetupNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: NavigationScreen
) {
    val timelineSharedViewModel = viewModel<TimelineSharedViewModel>()
    val albumSharedViewModel = viewModel<AlbumSharedViewModel>()

    SharedTransitionLayout(
        modifier = modifier
    ) {

        NavHost(
            navController = navHostController,
            startDestination = startDestination
        ) {

            composable<NavigationScreen.SetupScreen> {
                val viewModel = viewModel<SetupScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                SetupScreen(
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
                    navigateToCallBack = navHostController::navigateTo,
                    animatedVisibilityScope = this@composable
                )
            }

            composable<NavigationScreen.TimelineMediaScreen> { backStackEntry ->
                val viewModel = viewModel<TimelineMediaScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                TimelineMediaScreen(
                    uiState = uiState,
                    timelineMediaList = timelineSharedViewModel.timelineMediaList,
                    animatedVisibilityScope = this@composable
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
                val viewModel = viewModel<AlbumTimelineScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                AlbumTimelineScreen(
                    uiState = uiState,
                    uiAction = viewModel::onUiAction,
                    uiEvent = viewModel.uiEvent,
                    navigateToCallBack = navHostController::navigateTo,
                    albumMediaList = albumSharedViewModel.timelineMediaList,
                    animatedVisibilityScope = this
                )
            }

            composable<NavigationScreen.AlbumMediaScreen> {
                val viewModel = viewModel<AlbumMediaScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                AlbumMediaScreen(
                    uiState = uiState,
                    albumMediaList = albumSharedViewModel.timelineMediaList,
                    animatedVisibilityScope = this
                )
            }

            composable<NavigationScreen.SettingsScreen> {
                SettingsScreen()
            }

            composable<NavigationScreen.MemoriesScreen> {
                val viewModel = viewModel<MemoriesViewModel>()
                val memoriesUiState by viewModel.memoriesUiState.collectAsStateWithLifecycle()
                val timelineUiState by viewModel.timelineUiState.collectAsStateWithLifecycle()
                val detailUiState by viewModel.detailUiState.collectAsStateWithLifecycle()
                MemoriesScreen(
                    uiState = memoriesUiState,
                    uiEvent = viewModel.uiEvent,
                    timelineUiState = timelineUiState,
                    timelineUiAction = viewModel::onTimelineUiAction,
                    detailUiAction = viewModel::onDetailUiAction,
                    detailUiState = detailUiState,
                    navigateUp = navHostController::navigateUp
                )
            }
        }
    }
}
