package com.kannan.gallery.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import com.kannan.gallery.core.presentation.navigation.SetupNavGraph
import com.kannan.gallery.core.presentation.navigation.bottomnav.SetupBottomNavigation
import com.kannan.gallery.ui.theme.GalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GalleryTheme {
                val navHostController = rememberNavController()
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        SetupBottomNavigation(
                            currentDestination = navBackStackEntry?.destination?.route,
                            onClickedBottomNavigationItem = { navHostController.navigate(it.screen) }
                        )
                    }) { innerPadding ->

                    val bottomPadding = innerPadding.calculateBottomPadding()
                    SetupNavGraph(
                        navHostController = navHostController,
                        startDestination = NavigationScreen.TimeLineScreen,
                        modifier = Modifier.padding(bottom = bottomPadding)
                    )
                }
            }
        }
    }
}
