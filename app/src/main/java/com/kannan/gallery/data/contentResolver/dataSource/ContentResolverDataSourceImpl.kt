package com.kannan.gallery.data.contentResolver.dataSource

import com.kannan.gallery.data.contentResolver.CopyMedia
import com.kannan.gallery.data.contentResolver.GetAllAlbum
import com.kannan.gallery.data.contentResolver.GetAllMedia
import com.kannan.gallery.data.contentResolver.GetMediaByAlbumName
import com.kannan.gallery.data.contentResolver.models.AlbumCR
import com.kannan.gallery.data.contentResolver.models.MediaCR
import com.kannan.gallery.domain.model.Media
import javax.inject.Inject

class ContentResolverDataSourceImpl @Inject constructor(
    private val getAllMedia: GetAllMedia,
    private val getAllAlbum: GetAllAlbum,
    private val getMediaByAlbumName: GetMediaByAlbumName,
    private val copyMedia: CopyMedia
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
}