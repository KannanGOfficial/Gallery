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

class GetAllFavouriteMedia @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val queryUri =
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    private val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.MIME_TYPE,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.DATE_MODIFIED
    )

    suspend fun invoke(pageNumber: Int, pageSize: Int): List<MediaCR> =
        withContext(Dispatchers.IO) {
            val imageList = mutableSetOf<MediaCR>()

            val offset = pageNumber * pageSize

            val bundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
                putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.MediaColumns.DATE_MODIFIED)
                )
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )

                putString(
                    ContentResolver.QUERY_ARG_SQL_SELECTION,
                    MediaStore.MediaColumns.MIME_TYPE + " like ?"
                )
                putStringArray(
                    ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                    arrayOf("image%")
                )
                putInt(MediaStore.QUERY_ARG_MATCH_FAVORITE, MediaStore.MATCH_ONLY)
            }

            val cursor = context.contentResolver.query(
                queryUri,
                projection,
                bundle,
                null
            )

            cursor?.use {
                with(cursor) {

                    val idX = getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                    val dataX = getColumnIndex(MediaStore.MediaColumns.DATA)
                    val dateModifiedX = getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)
                    val displayNameX = getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                    val mimeTypeX = getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)


                    while (moveToNext()) {
                        val id = getLong(idX)
                        val data = getString(dataX)
                        val dateModified = getLong(dateModifiedX)
                        val displayName = getString(displayNameX)
                        val mimeType = getString(mimeTypeX)
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
                        imageList.add(mediaCR)
                    }
                }
            }
            return@withContext imageList.toList()
        }
}