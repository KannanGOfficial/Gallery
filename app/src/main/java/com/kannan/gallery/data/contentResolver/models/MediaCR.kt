package com.kannan.gallery.data.contentResolver.models

import com.kannan.gallery.domain.model.Media

data class MediaCR(
    val id: Long,
    val uri: String
)

fun MediaCR.toMedia() = Media(
    id = id,
    uri = uri
)
