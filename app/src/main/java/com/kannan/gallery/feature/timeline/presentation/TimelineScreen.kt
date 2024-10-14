package com.kannan.gallery.feature.timeline.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import com.kannan.gallery.core.presentation.sharedViewModel.dummyTimelineMediaList
import com.kannan.gallery.feature.timeline.domain.Media
import com.kannan.gallery.feature.timeline.presentation.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.ext.CollectAsEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TimelineScreen(
    modifier: Modifier = Modifier,
    navigateToCallBack: (NavigationScreen) -> Unit,
    uiState: TimelineScreenUiState,
    uiAction: ((TimelineScreenUiAction) -> Unit),
    uiEvent: Flow<TimelineScreenUiEvent>,
    timelineMedia: List<Media>,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    uiEvent.CollectAsEffect { event ->
        when (event) {
            is TimelineScreenUiEvent.NavigateTo -> navigateToCallBack.invoke(event.navigationScreens)
        }
    }

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(3)
    ) {
        items(
            count = timelineMedia.size
        ) { index: Int ->
            val data = timelineMedia[index]
            Thumbnail(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "image/ ${data.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .size(200.dp),
                data = data.uri,
                contentDescription = data.uri,
                onClick = {
                    uiAction.invoke(TimelineScreenUiAction.OnImageClicked(index))
                }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun TimelineScreenPreview() {
    GalleryTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                TimelineScreen(
                    uiAction = {},
                    uiEvent = emptyFlow(),
                    uiState = TimelineScreenUiState(),
                    timelineMedia = dummyTimelineMediaList,
                    navigateToCallBack = {},
                    animatedVisibilityScope = this
                )
            }
        }
    }
}
