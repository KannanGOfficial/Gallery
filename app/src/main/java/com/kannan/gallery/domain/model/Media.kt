package com.kannan.gallery.domain.model


data class Media(
    val id: Long,
    val uri: String,
    val dateModified: String,
    val isSelected: Boolean = false
)