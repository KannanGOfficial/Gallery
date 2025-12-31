package com.kannan.gallery.domain.model

data class Album(
    val id: Long,
    val name: String,
    val coverImage: String,
    val relativePath: String
)