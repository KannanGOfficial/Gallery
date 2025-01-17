package com.kannan.gallery.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kannan.gallery.data.contentResolver.dataSource.ContentResolverDataSource
import com.kannan.gallery.data.contentResolver.models.MediaCR

class GetMediaByAlbumNamePagingSource(
    private val contentResolverDataSource: ContentResolverDataSource,
    private val albumId: Long,
) : PagingSource<Int, MediaCR>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaCR> {
        return try {

            val pageNumber = params.key ?: STARTING_INDEX
            val pageSize = params.loadSize

            val mediaList = contentResolverDataSource.getMediaByAlbumName(
                albumId = albumId,
                pageSize = pageSize,
                pageNumber = pageNumber
            )

            val nextKey = if (mediaList.isEmpty()) null else pageNumber + 1

            LoadResult.Page(
                data = mediaList,
                prevKey = null,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MediaCR>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    companion object {
        const val STARTING_INDEX = 0
    }
}