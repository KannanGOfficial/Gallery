package com.kannan.gallery.data.contentResolver.models

import android.net.Uri
import com.kannan.gallery.domain.model.Media

data class MediaCR(
    val id: Long,
    val displayName: String,
    val mimeType: String,
    val uri: String,
    val URI: Uri,
    val dateModified: String
)

fun MediaCR.toMedia() = Media(
    id = id,
    uri = uri,
    dateModified = dateModified,
    displayName = displayName,
    mimeType = mimeType,
    URI = URI
)
