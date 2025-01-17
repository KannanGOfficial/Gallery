package com.kannan.gallery.data.contentResolver

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import com.kannan.gallery.data.contentResolver.models.MediaCR
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO: Need to filter out the Hidden files
class GetMediaByAlbumName @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val queryUri =
        MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

    private val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DATA,
    )

    suspend fun invoke(albumId: Long, pageNumber: Int, pageSize: Int): List<MediaCR> {
        return withContext(Dispatchers.IO) {

            val mediaList = mutableListOf<MediaCR>()
            val offset = pageNumber * pageSize

            // TODO: To remove the media type Image condition once you implemented the video
            val selection =
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}" +
                        " AND ${MediaStore.Files.FileColumns.BUCKET_ID}='$albumId'"

            val bundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
                putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.Files.FileColumns.DATE_ADDED)
                )
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
            }

            val cursor = context.contentResolver.query(
                queryUri,
                projection,
                bundle,
                null
            )

            cursor?.use {
                val idX = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val dataX = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idX)
                    val data = cursor.getString(dataX)

                    val mediaCR = MediaCR(
                        id = id,
                        uri = data
                    )
                    mediaList.add(mediaCR)
                }
            }
            return@withContext mediaList
        }
    }
}