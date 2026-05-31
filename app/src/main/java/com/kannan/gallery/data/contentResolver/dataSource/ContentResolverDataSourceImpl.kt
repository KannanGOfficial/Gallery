package com.kannan.gallery.data.contentResolver.dataSource

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.kannan.gallery.data.contentResolver.CopyMedia
import com.kannan.gallery.data.contentResolver.DeleteMedia
import com.kannan.gallery.data.contentResolver.GetAllAlbum
import com.kannan.gallery.data.contentResolver.GetAllMedia
import com.kannan.gallery.data.contentResolver.GetMediaByAlbumName
import com.kannan.gallery.data.contentResolver.MoveMedia
import com.kannan.gallery.data.contentResolver.ToggleFavorites
import com.kannan.gallery.data.contentResolver.TrashMedia
import com.kannan.gallery.data.contentResolver.models.AlbumCR
import com.kannan.gallery.data.contentResolver.models.MediaCR
import com.kannan.gallery.data.contentResolver.models.toMediaCR
import com.kannan.gallery.domain.model.Media
import javax.inject.Inject

class ContentResolverDataSourceImpl @Inject constructor(
    private val getAllMedia: GetAllMedia,
    private val getAllAlbum: GetAllAlbum,
    private val getMediaByAlbumName: GetMediaByAlbumName,
    private val copyMedia: CopyMedia,
    private val moveMedia: MoveMedia,
    private val trashMedia: TrashMedia,
    private val deleteMedia: DeleteMedia,
    private val toggleFavorites: ToggleFavorites
) : ContentResolverDataSource {

    override suspend fun getAllMedia(pageNumber: Int, pageSize: Int): List<MediaCR> {
        return getAllMedia.invoke(pageNumber = pageNumber, pageSize = pageSize)
    }

    override suspend fun getAllAlbum(): List<AlbumCR> {
        return getAllAlbum.invoke()
    }

    override suspend fun getMediaByAlbumName(
        albumId: Long,
        pageNumber: Int,
        pageSize: Int
    ): List<MediaCR> {
        return getMediaByAlbumName.invoke(
            albumId = albumId,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    override suspend fun copyMedia(
        from: Media,
        path: String
    ): Boolean {
        return copyMedia.invoke(
            from = from,
            path = path
        )
    }

    override suspend fun moveMedia(media: Media, toPath: String): Boolean =
        moveMedia.invoke(media = media, newPath = toPath)

    override suspend fun trashMedia(
        mediaList: List<Media>,
        trash: Boolean,
        result: ActivityResultLauncher<IntentSenderRequest>
    ) = trashMedia.invoke(
        mediaList = mediaList.map(Media::toMediaCR),
        trash = trash,
        result = result
    )

    override suspend fun deleteMedia(
        mediaList: List<Media>,
        result: ActivityResultLauncher<IntentSenderRequest>
    ) = deleteMedia.invoke(
        mediaList = mediaList.map(Media::toMediaCR),
        result = result
    )

    override suspend fun toggleFavorite(
        mediaList: List<Media>,
        favorite: Boolean,
        result: ActivityResultLauncher<IntentSenderRequest>
    ) = toggleFavorites.invoke(
        mediaList = mediaList.map(Media::toMediaCR),
        favorite = favorite,
        result = result
    )

}