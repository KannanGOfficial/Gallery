package com.kannan.gallery.data.contentResolver

import com.kannan.gallery.data.contentResolver.models.MediaCR
import javax.inject.Inject

class ContentResolverDataSourceImpl @Inject constructor(
    private val getAllMedia: GetAllMedia
) : ContentResolverDataSource {

    override suspend fun getAllMedia(pageNumber: Int, pageSize: Int): List<MediaCR> {
        return getAllMedia.invoke(pageNumber = pageNumber, pageSize = pageSize)
    }
}