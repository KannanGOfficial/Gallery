package com.kannan.gallery.data.contentResolver.models

import com.kannan.gallery.domain.model.Album

data class AlbumCR(
    val id: Long,
    val name: String,
    val coverImage: String,
    val relativePath: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as AlbumCR
        return relativePath == other.relativePath // Two albums are equal if they have the same relativePath
    }

    override fun hashCode(): Int {
        return relativePath.hashCode() // hashCode is based on relativePath
    }
}

fun AlbumCR.toAlbum() = Album(
    id = id,
    name = name,
    coverImage = coverImage
)