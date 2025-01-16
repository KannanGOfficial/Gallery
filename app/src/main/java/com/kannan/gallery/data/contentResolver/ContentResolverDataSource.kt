package com.kannan.gallery.data.contentResolver

import com.kannan.gallery.data.contentResolver.models.AlbumCR
import com.kannan.gallery.data.contentResolver.models.MediaCR

interface ContentResolverDataSource {

    suspend fun getAllMedia(pageNumber: Int, pageSize: Int): List<MediaCR>

    suspend fun getAllAlbum(): List<AlbumCR>
}