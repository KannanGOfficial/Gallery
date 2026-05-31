package com.kannan.gallery.data.contentResolver

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ActivityOptionsCompat
import com.kannan.gallery.data.contentResolver.models.MediaCR
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrashMedia @Inject constructor(@param:ApplicationContext val context: Context) {
    suspend fun invoke(
        result: ActivityResultLauncher<IntentSenderRequest>,
        mediaList: List<MediaCR>,
        trash: Boolean
    ) = withContext(Dispatchers.IO) {
        val intentSender = MediaStore.createTrashRequest(
            context.contentResolver,
            mediaList.map { it.URI },
            trash
        ).intentSender
        val senderRequest: IntentSenderRequest = IntentSenderRequest.Builder(intentSender)
            .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
            .build()
        result.launch(senderRequest, ActivityOptionsCompat.makeTaskLaunchBehind())
    }
}