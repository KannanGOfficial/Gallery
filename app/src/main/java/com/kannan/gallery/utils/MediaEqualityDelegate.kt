package com.kannan.gallery.utils

import coil.compose.EqualityDelegate

class MediaEqualityDelegate : EqualityDelegate {
    override fun equals(self: Any?, other: Any?): Boolean = true

    override fun hashCode(self: Any?): Int = 31
}