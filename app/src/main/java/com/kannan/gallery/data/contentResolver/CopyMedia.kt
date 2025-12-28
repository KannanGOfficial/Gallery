package com.kannan.gallery.data.contentResolver

import android.content.ContentValues
import android.content.Context
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import com.kannan.gallery.domain.model.Media
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class CopyMedia @Inject constructor(@ApplicationContext private val context: Context) {
    suspend fun invoke(from: Media, path: String) = withContext(Dispatchers.IO) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, from.displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, from.mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, path)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        val volumeUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        try {
            with(context.contentResolver) {
                val outUri = insert(volumeUri, contentValues)
                if (outUri != null) {
                    async {
                        openFileDescriptor(outUri, "w", null).use { target ->
                            openFileDescriptor(from.URI, "r").use { from ->
                                if (target != null && from != null) {
                                    try {
                                        FileUtils.copy(from.fileDescriptor, target.fileDescriptor)
                                    } catch (e: IOException) {
                                        if (e.message.toString().contains("ENOSPC")) {
                                            Log.e("Constants.TAG", "No space left on device")
                                        } else {
                                            Log.e("Constants.TAG", e.message.toString())
                                        }
                                        return@async
                                    }
                                }
                            }
                        }
                    }.await()
                    val updatedValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.IS_PENDING, 0)
                        put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis())
                    }

                    update(
                        outUri,
                        updatedValues,
                        null
                    ) > 0
                } else false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}