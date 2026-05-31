package com.kannan.gallery.data.repository

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.kannan.gallery.data.contentResolver.dataSource.ContentResolverDataSource
import com.kannan.gallery.data.contentResolver.models.AlbumCR
import com.kannan.gallery.data.contentResolver.models.MediaCR
import com.kannan.gallery.data.contentResolver.models.toAlbum
import com.kannan.gallery.data.contentResolver.models.toMedia
import com.kannan.gallery.data.pagingSource.GetAllMediaPagingSource
import com.kannan.gallery.data.pagingSource.GetMediaByAlbumNamePagingSource
import com.kannan.gallery.domain.GalleryRepository
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.utils.StringUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val contentResolverDataSource: ContentResolverDataSource
) : GalleryRepository {

    override fun getAllMediaPagedStream(): Flow<PagingData<Media>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30
            ),
            pagingSourceFactory = {
                GetAllMediaPagingSource(contentResolverDataSource)
            }
        ).flow.map { pagingData ->
            /**
             * To filter out hidden files
             * */
            pagingData.filter { !StringUtil.pathStartsWithDot(it.uri) }
                .map(MediaCR::toMedia)
        }
    }

    override suspend fun getAllAlbum(): List<Album> {
        return contentResolverDataSource.getAllAlbum().map(AlbumCR::toAlbum)
    }

    override fun getMediaByAlbumName(albumId: Long): Flow<PagingData<Media>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                GetMediaByAlbumNamePagingSource(
                    contentResolverDataSource = contentResolverDataSource,
                    albumId = albumId
                )
            }
        ).flow.map {
            it.map(MediaCR::toMedia)
        }
    }

    override suspend fun copyMedia(from: Media, toPath: String): Boolean {
        return contentResolverDataSource.copyMedia(
            from = from,
            path = toPath
        )
    }

    override suspend fun moveMedia(media: Media, toPath: String): Boolean =
        contentResolverDataSource.moveMedia(media = media, toPath = toPath)

    override suspend fun trashMedia(
        mediaList: List<Media>,
        trash: Boolean,
        result: ActivityResultLauncher<IntentSenderRequest>
    ) = contentResolverDataSource.trashMedia(
        mediaList = mediaList,
        trash = trash,
        result = result
    )

    override suspend fun deleteMedia(
        mediaList: List<Media>,
        result: ActivityResultLauncher<IntentSenderRequest>
    ) = contentResolverDataSource.deleteMedia(
        mediaList = mediaList,
        result = result
    )

    override suspend fun toggleFavorite(
        mediaList: List<Media>,
        favorite: Boolean,
        result: ActivityResultLauncher<IntentSenderRequest>
    ) = contentResolverDataSource.toggleFavorite(
        mediaList = mediaList,
        favorite = favorite,
        result = result
    )
}