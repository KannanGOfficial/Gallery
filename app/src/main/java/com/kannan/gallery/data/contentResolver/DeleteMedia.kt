package com.kannan.gallery.data.contentResolver

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.kannan.gallery.data.contentResolver.models.MediaCR
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteMedia @Inject constructor(@param:ApplicationContext val context: Context) {
    suspend fun invoke(
        result: ActivityResultLauncher<IntentSenderRequest>,
        mediaList: List<MediaCR>,
    ) = withContext(Dispatchers.IO) {
        val intentSender =
            MediaStore.createDeleteRequest(
                context.contentResolver,
                mediaList.map { it.URI }
            ).intentSender
        val senderRequest: IntentSenderRequest = IntentSenderRequest.Builder(intentSender)
            .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
            .build()
        result.launch(senderRequest)
    }
}