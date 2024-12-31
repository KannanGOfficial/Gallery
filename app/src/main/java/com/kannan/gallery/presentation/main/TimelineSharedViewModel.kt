package com.kannan.gallery.presentation.main

import androidx.lifecycle.ViewModel
import com.kannan.gallery.domain.model.Media

class TimelineSharedViewModel : ViewModel() {
    val timelineMediaList = dummyTimelineMediaList
}

val dummyTimelineMediaList = (0..30).map { index ->
    Media(
        id = index.toLong(),
        uri = "",
        isSelected = false
    )
}