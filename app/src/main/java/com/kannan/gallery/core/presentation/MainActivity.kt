package com.kannan.gallery.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import com.kannan.gallery.core.presentation.navigation.SetupNavGraph
import com.kannan.gallery.core.presentation.navigation.bottomnav.SetupBottomNavigation
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.ui.theme.navigateBottomBar
import com.kannan.gallery.utils.ext.checkPermission

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GalleryTheme {

                val viewModel by viewModels<MainViewModel>()
                val uiAction = viewModel::onUiAction
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                val navHostController = rememberNavController()
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                navHostController.addOnDestinationChangedListener { _: NavController, destination: NavDestination, _: Bundle? ->
                    destination.route?.let { route ->
                        uiAction.invoke(MainUiAction.OnNavDestinationChanged(route))
                    }
                }
                val startDestination = remember {
                    getStartDestination()
                }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        SetupBottomNavigation(
                            currentDestination = navBackStackEntry?.destination?.route,
                            shouldShowBottomBar = uiState.shouldShowBottomBar,
                            onClickedBottomNavigationItem = { navHostController.navigateBottomBar(it.screen) }
                        )
                    }) { innerPadding ->

                    val bottomPadding by animateDpAsState(
                        targetValue = if (uiState.shouldShowBottomBar) innerPadding.calculateBottomPadding() else 0.dp,
                        label = "bottom-padding"
                    )

                    SetupNavGraph(
                        navHostController = navHostController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(bottom = bottomPadding)
                    )
                }
            }
        }
    }

    private fun getStartDestination() =
        when (checkPermission()) {
            true -> NavigationScreen.TimelineScreen
            false -> NavigationScreen.SetupScreen
        }
}
