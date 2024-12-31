package com.kannan.gallery.presentation.main

import androidx.lifecycle.ViewModel
import com.kannan.gallery.domain.model.Media

class AlbumSharedViewModel : ViewModel() {
    val timelineMediaList = dummyTimelineMediaList
}

val dummyAlbumMediaList = (0..10).map { index ->
    Media(
        id = index.toLong(),
        uri = "",
        isSelected = false
    )
}