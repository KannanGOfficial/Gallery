package com.kannan.gallery.data.contentResolver

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.kannan.gallery.data.contentResolver.models.AlbumCR
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO: Need to filter out the Hidden files
class GetAllAlbum @Inject constructor(@ApplicationContext private val context: Context) {
    private val queryUri =
        MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

    private val projection = arrayOf(
        MediaStore.MediaColumns.BUCKET_ID,
        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        MediaStore.MediaColumns.DATA
    )

    suspend fun invoke(): List<AlbumCR> {
        return withContext(Dispatchers.IO) {
            val albumList = mutableSetOf<AlbumCR>()

            val bundle = Bundle().apply {
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
                val bucketIdX = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)
                val bucketNameX =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
                val dataX = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)

                while (cursor.moveToNext()) {
                    val bucketId = cursor.getLong(bucketIdX)
                    val bucketName = cursor.getString(bucketNameX)
                    val data = cursor.getString(dataX)

                    val albumCR = AlbumCR(
                        id = bucketId,
                        name = bucketName ?: Build.MODEL,
                        coverImage = data
                    )

                    albumList.add(albumCR)
                }
            }

            return@withContext albumList.toList()
        }
    }
}