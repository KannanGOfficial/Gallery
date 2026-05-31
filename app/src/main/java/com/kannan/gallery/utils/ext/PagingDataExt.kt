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

    if (after == null) return null

    val afterDateStr = after.item.dateModified
    val beforeDateStr = before?.item?.dateModified

    return if (beforeDateStr != afterDateStr)
        MediaUiModel.Header(
            title = afterDateStr,
            id = "separator_${before?.item?.uniqueId}_${after.item.uniqueId}"
        )
    else null
}