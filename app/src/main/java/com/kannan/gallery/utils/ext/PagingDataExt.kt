package com.kannan.gallery.utils.ext

import androidx.paging.PagingData
import androidx.paging.map
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.domain.model.MediaUiModel

fun PagingData<Media>.mapAsMediaUiModelItem(): PagingData<MediaUiModel.Item> =
    this.map { media ->
        MediaUiModel.Item(
            item = media
        )
    }

fun insertLineSeparator(
    before: MediaUiModel.Item?,
    after: MediaUiModel.Item?
): MediaUiModel? {
    val afterDateStr = after?.item?.dateModified
    val beforeDateStr = before?.item?.dateModified

    return when {
        afterDateStr == null -> null

        before == null -> {
            MediaUiModel.Header(
                title = "today",
                id = "separator_${before?.item?.id}_${after.item.id}"
            )
        }

        beforeDateStr == null -> null

        (afterDateStr < beforeDateStr) -> {
            MediaUiModel.Header(
                title = afterDateStr,
                id = "separator_${before.item.id}_${after.item.id}"
            )
        }

        else -> null
    }
}