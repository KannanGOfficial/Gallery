package com.kannan.gallery.domain.model

sealed class MediaUiModel(val key: String) {
    data class Header(val title: String, val id: String) : MediaUiModel(id)
    data class Item(val item: Media, var position: Int = 0) : MediaUiModel(item.id.toString())
}