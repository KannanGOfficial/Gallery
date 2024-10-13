package com.kannan.gallery.core.presentation.sharedViewModel

import androidx.lifecycle.ViewModel
import com.kannan.gallery.feature.timeline.domain.Media

class TimelineSharedViewModel : ViewModel() {
    val timelineMediaList = dummyTimelineMediaList
}

val dummyTimelineMediaList = (0..10).map { index ->
    Media(
        id = index.toLong(),
        uri = "",
        isSelected = false
    )
}