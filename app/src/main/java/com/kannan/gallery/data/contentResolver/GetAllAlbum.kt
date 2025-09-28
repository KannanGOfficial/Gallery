package com.kannan.gallery.data.contentResolver

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.kannan.gallery.data.contentResolver.models.AlbumCR
import com.kannan.gallery.utils.StringUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllAlbum @Inject constructor(@ApplicationContext private val context: Context) {
    private val queryUri =
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    private val projection = arrayOf(
        MediaStore.MediaColumns.BUCKET_ID,
        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        MediaStore.MediaColumns.RELATIVE_PATH,
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
                with(cursor) {

                    val bucketIdX = getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)
                    val bucketNameX =
                        getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
                    val dataX = getColumnIndex(MediaStore.MediaColumns.DATA)
                    val relativePathX =
                        getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH)

                    while (moveToNext()) {
                        val bucketId = getLong(bucketIdX)
                        val bucketName = getString(bucketNameX)
                        val data = getString(dataX)
                        val relativePath = getString(relativePathX)

                        if (StringUtil.pathStartsWithDot(data)) {
                            // Skip hidden files or files inside hidden folders
                            continue
                        }

                        val albumCR = AlbumCR(
                            id = bucketId,
                            name = bucketName ?: Build.MODEL,
                            coverImage = data,
                            relativePath = relativePath
                        )

                        albumList.add(albumCR)
                    }
                }
            }

            return@withContext albumList.toList()
        }
    }
}