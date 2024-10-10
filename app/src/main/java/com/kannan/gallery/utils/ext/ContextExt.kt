package com.kannan.gallery.utils.ext

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.kannan.gallery.utils.permission.Permission

fun Context.launchManageMedia() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val intent = Intent().apply {
            action = Settings.ACTION_REQUEST_MANAGE_MEDIA
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}

fun Context.launchManageFiles() {
    val intent = Intent().apply {
        action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(intent)
}

fun Context.checkPermission(): Boolean =
    Permission.PERMISSIONS.all {
        checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
    }