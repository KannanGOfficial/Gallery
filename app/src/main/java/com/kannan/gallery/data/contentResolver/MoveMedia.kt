package com.kannan.gallery.data.contentResolver

import android.content.ContentValues
import android.provider.MediaStore
import com.kannan.gallery.domain.model.Media
import javax.inject.Inject

class MoveMedia @Inject constructor(private val updateMedia: UpdateMedia) {
    suspend fun invoke(media: Media, newPath: String): Boolean {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.RELATIVE_PATH, newPath)
        }
        return updateMedia.invoke(media, contentValues)
    }
}