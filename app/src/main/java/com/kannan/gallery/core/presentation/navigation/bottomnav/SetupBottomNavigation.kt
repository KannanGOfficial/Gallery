package com.kannan.gallery.core.presentation.navigation.bottomnav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun SetupBottomNavigation(
    currentDestination: String?,
    onClickedBottomNavigationItem: (BottomNavigationItem) -> Unit
) {
    NavigationBar {
        BottomNavigationItem.entries.forEachIndexed { _, bottomNavigationItems ->
            val isSelected =
                currentDestination == bottomNavigationItems.screen::class.qualifiedName

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
