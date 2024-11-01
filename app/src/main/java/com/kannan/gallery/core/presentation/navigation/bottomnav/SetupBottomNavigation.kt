package com.kannan.gallery.core.presentation.navigation.bottomnav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.ext.getRoute

@Composable
fun SetupBottomNavigation(
    modifier: Modifier = Modifier,
    shouldShowBottomBar: Boolean = true,
    currentDestination: String?,
    onClickedBottomNavigationItem: (BottomNavigationItem) -> Unit
) {

    AnimatedVisibility(
        visible = shouldShowBottomBar,
        enter = slideInVertically(
            initialOffsetY = { it }  // Slide in from the bottom
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }  // Slide out to the bottom
        )
    ) {
        NavigationBar(modifier = modifier) {
            BottomNavigationItem.entries.forEachIndexed { _, bottomNavigationItems ->
                val isSelected =
                    currentDestination == bottomNavigationItems.screen::class.getRoute()

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onClickedBottomNavigationItem.invoke(bottomNavigationItems) },
                    icon = {
                        Icon(
                            painterResource(
                                id = if (isSelected) bottomNavigationItems.selectedIcon
                                else bottomNavigationItems.unSelectedIcon
                            ),
                            contentDescription = null,
                        )
                    })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SetupBottomNavigationPreview() {
    GalleryTheme {
        SetupBottomNavigation(
            currentDestination = BottomNavigationItem.TIMELINE.screen::class.qualifiedName,
            onClickedBottomNavigationItem = {}
        )
    }
}
