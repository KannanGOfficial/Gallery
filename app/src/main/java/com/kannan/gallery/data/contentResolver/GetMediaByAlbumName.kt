package com.kannan.gallery.data.contentResolver

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import com.kannan.gallery.data.contentResolver.models.MediaCR
import com.kannan.gallery.utils.ext.getDate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO: Need to filter out the Hidden files
class GetMediaByAlbumName @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val queryUri =
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    private val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.DATE_MODIFIED,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.MIME_TYPE
    )

    suspend fun invoke(albumId: Long, pageNumber: Int, pageSize: Int): List<MediaCR> {
        return withContext(Dispatchers.IO) {

            val mediaList = mutableListOf<MediaCR>()
            val offset = pageNumber * pageSize

            // TODO: To remove the media type Image condition once you implemented the video
            val selection =
//                "${MediaStore.Images.Media.MIME_TYPE}=${MediaStore.Media.FileColumns.MEDIA_TYPE_IMAGE}" +
                "${MediaStore.Images.Media.BUCKET_ID}='$albumId'"

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
                val dateModifiedX =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)
                val displayNameX =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val mimeTypeX = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idX)
                    val data = cursor.getString(dataX)
                    val dateModified = cursor.getLong(dateModifiedX)
                    val displayName = cursor.getString(displayNameX)
                    val mimeType = cursor.getString(mimeTypeX)
                    val contentUri = if (mimeType.contains("image"))
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    else
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    val uri = ContentUris.withAppendedId(contentUri, id)
                    val mediaCR = MediaCR(
                        id = id,
                        uri = data,
                        URI = uri,
                        dateModified = dateModified.getDate(),
                        displayName = displayName,
                        mimeType = mimeType
                    )
                    mediaList.add(mediaCR)
                }
            }
            return@withContext mediaList
        }
    }
}