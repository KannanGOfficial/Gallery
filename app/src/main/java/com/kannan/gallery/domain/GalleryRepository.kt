package com.kannan.gallery.domain

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.paging.PagingData
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {

    fun getAllMediaPagedStream(): Flow<PagingData<Media>>

    suspend fun getAllAlbum(): List<Album>

    fun getMediaByAlbumName(albumId: Long): Flow<PagingData<Media>>

    suspend fun copyMedia(from: Media, toPath: String): Boolean

    suspend fun moveMedia(media: Media, toPath: String): Boolean

    suspend fun trashMedia(
        mediaList: List<Media>,
        trash: Boolean,
        result: ActivityResultLauncher<IntentSenderRequest>
    )

    suspend fun deleteMedia(
        mediaList: List<Media>,
        result: ActivityResultLauncher<IntentSenderRequest>
    )

    suspend fun toggleFavorite(
        mediaList: List<Media>,
        favorite: Boolean,
        result: ActivityResultLauncher<IntentSenderRequest>
    )
}