package com.kannan.gallery.data

import com.kannan.gallery.domain.model.Media

val dummyTimelineMediaList = (0..30).map { index ->
    Media(
        id = index.toLong(),
        uri = "",
        isSelected = false
    )
}


val dummyAlbumMediaList = (0..30).map { index ->
    Media(
        id = index.toLong(),
        uri = "",
        isSelected = false
    )
}