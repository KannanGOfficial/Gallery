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
import com.kannan.gallery.presentation.feature.album.AlbumDetailScreen
import com.kannan.gallery.presentation.feature.album.AlbumDetailScreenViewModel
import com.kannan.gallery.presentation.feature.album.AlbumScreen
import com.kannan.gallery.presentation.feature.album.AlbumScreenViewModel
import com.kannan.gallery.presentation.feature.photo.PhotoScreen
import com.kannan.gallery.presentation.feature.photo.PhotoScreenViewModel
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


            composable<NavigationScreen.PhotoScreen> {
                val viewModel = viewModel<PhotoScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                PhotoScreen(
                    uiState = uiState,
                    uiEvent = viewModel.uiEvent,
                    uiAction = viewModel::onUiAction,
                    mediaList = viewModel.mediaList,
                    navigateUpCallback = navHostController::navigateUp,
                    shouldShowBottomBar = shouldShowBottomBar
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


            composable<NavigationScreen.AlbumDetailScreen> {
                val viewModel = viewModel<AlbumDetailScreenViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                AlbumDetailScreen(
                    uiState = uiState,
                    uiEvent = viewModel.uiEvent,
                    uiAction = viewModel::onUiAction,
                    mediaList = viewModel.mediaList,
                    navigateUpCallback = navHostController::navigateUp
                )
            }

            composable<NavigationScreen.SettingsScreen> {
                SettingsScreen()
            }
        }
    }
}
