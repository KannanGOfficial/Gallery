package com.kannan.gallery.data.contentResolver.models

import com.kannan.gallery.domain.model.Album

data class AlbumCR(
    val id: Long,
    val name: String,
    val coverImage: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as AlbumCR
        return id == other.id // Two albums are equal if they have the same id
    }

    override fun hashCode(): Int {
        return id.hashCode() // hashCode is based on id
    }
}

fun AlbumCR.toAlbum() = Album(
    id = id,
    name = name,
    coverImage = coverImage
)