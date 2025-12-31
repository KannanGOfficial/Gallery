package com.kannan.gallery.domain.model

import android.net.Uri


data class Media(
    val id: Long,
    val uri: String,
    val dateModified: String,
    val mimeType: String,
    val displayName: String,
    val URI: Uri,
    val isSelected: Boolean = false
)