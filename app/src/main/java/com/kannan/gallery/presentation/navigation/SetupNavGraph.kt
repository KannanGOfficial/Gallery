package com.kannan.gallery.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kannan.gallery.presentation.feature.album.AlbumDetailScreen
import com.kannan.gallery.presentation.feature.album.AlbumDetailScreenViewModel
import com.kannan.gallery.presentation.feature.album.AlbumScreen
import com.kannan.gallery.presentation.feature.album.AlbumScreenViewModel
import com.kannan.gallery.presentation.feature.media.MediaScreen
import com.kannan.gallery.presentation.feature.media.MediaScreenViewModel
import com.kannan.gallery.presentation.feature.settings.SettingsScreen
import com.kannan.gallery.presentation.feature.setup.SetupScreen
import com.kannan.gallery.presentation.feature.setup.SetupScreenViewModel
import com.kannan.gallery.ui.theme.navigateFromSetupScreen
import com.kannan.gallery.ui.theme.navigateTo

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SetupNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: NavigationScreen,
    shouldShowBottomBar: (Boolean) -> Unit,
) {

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


            composable<NavigationScreen.MediaScreen> {
                val viewModel = hiltViewModel<MediaScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                MediaScreen(
                    uiState = uiState,
                    uiEvent = viewModel.uiEvent,
                    uiAction = viewModel::onUiAction,
                    mediaListPagedStream = viewModel.mediaListPagedStream,
                    mediaListUiModel = viewModel.mediaListUiModelPagedStream,
                    navigateUpCallback = navHostController::navigateUp,
                    shouldShowBottomBar = shouldShowBottomBar
                )
            }

            composable<NavigationScreen.AlbumScreen> {
                val viewModel = hiltViewModel<AlbumScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                AlbumScreen(
                    uiState = uiState,
                    uiEvent = viewModel.uiEvent,
                    uiAction = viewModel::onUiAction,
                    navigateToCallBack = navHostController::navigateTo
                )
            }


            composable<NavigationScreen.AlbumDetailScreen> {
                val viewModel = hiltViewModel<AlbumDetailScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                AlbumDetailScreen(
                    uiState = uiState,
                    uiEvent = viewModel.uiEvent,
                    uiAction = viewModel::onUiAction,
                    mediaListPagedStream = viewModel.mediaListPagedStream,
                    mediaListUiModel = viewModel.mediaListUiModelPagedStream,
                    navigateUpCallback = navHostController::navigateUp
                )
            }

            composable<NavigationScreen.SettingsScreen> {
                SettingsScreen()
            }
        }
    }
}
