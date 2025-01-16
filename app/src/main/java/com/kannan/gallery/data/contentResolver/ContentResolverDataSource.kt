package com.kannan.gallery.data.contentResolver

import com.kannan.gallery.data.contentResolver.models.MediaCR

interface ContentResolverDataSource {

    suspend fun getAllMedia(pageNumber: Int, pageSize: Int): List<MediaCR>
}