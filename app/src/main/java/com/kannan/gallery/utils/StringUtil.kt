package com.kannan.gallery.utils

object StringUtil {

    fun pathStartsWithDot(path: String): Boolean {
        // Split the path by the "/" delimiter
        val pathParts = path.split("/")

        // Check if any part of the path starts with a dot
        for (part in pathParts) {
            if (part.startsWith(".")) {
                return true
            }
        }
        return false
    }
}