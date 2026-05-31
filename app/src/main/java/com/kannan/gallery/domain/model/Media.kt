package com.kannan.gallery.domain.model

import android.net.Uri
import java.util.UUID


data class Media(
    val id: Long,
    val uri: String,
    val dateModified: String,
    val mimeType: String,
    val displayName: String,
    val URI: Uri,
    val uniqueId: String = UUID.randomUUID().toString(),
    val isSelected: Boolean = false
)