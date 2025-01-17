package com.kannan.gallery.domain

import androidx.paging.PagingData
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {

    fun getAllMediaPagedStream(): Flow<PagingData<Media>>

    suspend fun getAllAlbum(): List<Album>

    fun getMediaByAlbumName(albumId: Long): Flow<PagingData<Media>>
}