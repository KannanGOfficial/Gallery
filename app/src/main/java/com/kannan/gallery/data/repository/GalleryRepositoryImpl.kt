package com.kannan.gallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kannan.gallery.data.contentResolver.ContentResolverDataSource
import com.kannan.gallery.data.contentResolver.models.AlbumCR
import com.kannan.gallery.data.contentResolver.models.MediaCR
import com.kannan.gallery.data.contentResolver.models.toAlbum
import com.kannan.gallery.data.contentResolver.models.toMedia
import com.kannan.gallery.domain.GalleryRepository
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.domain.model.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val contentResolverDataSource: ContentResolverDataSource
) : GalleryRepository {

    override fun getAllMediaPagedStream(): Flow<PagingData<Media>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                GetAllMediaPagingSource(contentResolverDataSource)
            }
        ).flow.map {
            it.map(MediaCR::toMedia)
        }
    }

    override suspend fun getAllAlbum(): List<Album> {
        return contentResolverDataSource.getAllAlbum().map(AlbumCR::toAlbum)
    }
}