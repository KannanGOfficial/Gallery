package com.kannan.gallery.data.contentResolver.dataSource

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.kannan.gallery.data.contentResolver.models.AlbumCR
import com.kannan.gallery.data.contentResolver.models.MediaCR
import com.kannan.gallery.domain.model.Media

interface ContentResolverDataSource {

    suspend fun getAllMedia(pageNumber: Int, pageSize: Int): List<MediaCR>

    suspend fun getAllAlbum(): List<AlbumCR>

    suspend fun getMediaByAlbumName(
        albumId: Long,
        pageNumber: Int,
        pageSize: Int
    ): List<MediaCR>

    suspend fun copyMedia(
        from: Media,
        path: String
    ): Boolean

    suspend fun moveMedia(
        media: Media,
        toPath: String
    ): Boolean

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