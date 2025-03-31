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
class GetAllMedia @Inject constructor(@ApplicationContext val context: Context) {
    private val queryUri =
        MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

    private val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DATA,
    )

    suspend fun invoke(pageNumber: Int, pageSize: Int): List<MediaCR> {
        return withContext(Dispatchers.IO) {

            val imageList: ArrayList<MediaCR> = arrayListOf()

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

                    while (moveToNext()) {
                        val id = getLong(idX)
                        val data = getString(dataX)

                        val mediaCR = MediaCR(
                            id = id,
                            uri = data
                        )
                        imageList.add(mediaCR)
                    }
                }
            }
            return@withContext imageList
        }
    }
}