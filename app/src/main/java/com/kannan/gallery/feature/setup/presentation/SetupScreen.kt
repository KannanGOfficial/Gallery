package com.kannan.gallery.feature.setup.presentation

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import com.kannan.gallery.utils.ext.CollectAsEffect
import com.kannan.gallery.utils.ext.launchManageFiles
import com.kannan.gallery.utils.ext.launchManageMedia
import com.kannan.gallery.utils.permission.Permission
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SetupScreen(
    modifier: Modifier = Modifier,
    uiAction: ((SetupScreenUiAction) -> Unit),
    uiEvent: Flow<SetupScreenUiEvent>,
    uiState: SetupScreenUiState,
    navigateToCallback: ((NavigationScreen) -> Unit)
) {

    val context = LocalContext.current
    val activity = context as Activity
    val lifecycleOwner = LocalLifecycleOwner.current


    val mediaPermissions = rememberMultiplePermissionsState(
        permissions = Permission.PERMISSIONS,
        onPermissionsResult = {
            val permissionGranted = it.all { item -> item.value }
            uiAction.invoke(SetupScreenUiAction.PermissionCallback(permissionGranted))
        }
    )

    uiEvent.CollectAsEffect { event ->
        when (event) {
            SetupScreenUiEvent.NavigateUp -> activity.finish()
            SetupScreenUiEvent.LaunchManageFiles -> context.launchManageFiles()
            SetupScreenUiEvent.LaunchManageMedia -> context.launchManageMedia()
            SetupScreenUiEvent.LaunchMultiplePermissionRequest -> mediaPermissions.launchMultiplePermissionRequest()
            is SetupScreenUiEvent.NavigateTo -> navigateToCallback.invoke(event.navigationScreen)
        }
    }

    SetupScreenContent(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        uiAction = uiAction
    )

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                uiAction.invoke(SetupScreenUiAction.OnResume)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

}
