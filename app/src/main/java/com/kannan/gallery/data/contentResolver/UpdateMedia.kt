package com.kannan.gallery.data.contentResolver

import android.content.ContentValues
import com.kannan.gallery.domain.model.Media
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateMedia @Inject constructor(@param:ApplicationContext private val context: android.content.Context) {
    suspend fun invoke(media: Media, contentValues: ContentValues): Boolean =
        withContext(Dispatchers.IO) {
            try {
                with(context.contentResolver) {
                    update(
                        media.URI,
                        contentValues,
                        null
                    ) > 0
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
}