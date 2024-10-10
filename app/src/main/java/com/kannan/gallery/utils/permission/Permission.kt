package com.kannan.gallery.utils.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object Permission {


    private val PERMISSION_COMMON = listOf(Manifest.permission.ACCESS_MEDIA_LOCATION)


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val PERMISSION_T = /*PERMISSION_COMMON.toMutableList().apply {
    addAll(*/
        listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
//            Manifest.permission.ACCESS_MEDIA_LOCATION
        )
    /* )
 }*/

    private val PERMISSION_OLD =
        PERMISSION_COMMON.toMutableList().apply {
            addAll(
                listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            )
        }

    val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PERMISSION_T
    } else {
        PERMISSION_OLD
    }
}