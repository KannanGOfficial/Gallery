package com.kannan.gallery.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kannan.gallery.data.contentResolver.ContentResolverDataSource
import com.kannan.gallery.data.contentResolver.models.MediaCR

class GetAllMediaPagingSource(
    private val contentResolverDataSource: ContentResolverDataSource
) : PagingSource<Int, MediaCR>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaCR> {

        return try {

            val pageNumber = params.key ?: STARTING_INDEX
            val pageSize = params.loadSize

            val mediaList = contentResolverDataSource.getAllMedia(
                pageNumber = pageNumber,
                pageSize = pageSize
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
        const val STARTING_INDEX = 1
    }
}