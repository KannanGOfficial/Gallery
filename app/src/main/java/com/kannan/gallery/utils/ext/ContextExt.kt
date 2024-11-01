package com.kannan.gallery.utils.ext

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.kannan.gallery.utils.permission.Permission
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

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

fun <T : Any> Class<T>.getConstructorNames(): String {
    // Get all constructors of the class
    val constructors = this.kotlin.primaryConstructor


    return constructors?.parameters?.joinToString("&") { parameter ->
        "$parameter={$parameter}"
    } ?: ""
}

fun <T : Any> KClass<T>.getPrimaryConstructorParameterNames(): List<String?> {
    return this.primaryConstructor?.parameters?.map { it.name } ?: emptyList()
}

fun <T : Any> KClass<T>.getRoute(): String {

    val primaryConstructorNames = this.primaryConstructor?.parameters?.map { it.name }

    val paramRouteNullable = primaryConstructorNames?.joinToString("&") { paramName ->
        "$paramName={$paramName}"
    }

    val paramRoute: String = paramRouteNullable?.let {
        "?$it"
    } ?: ""


    return this.qualifiedName + paramRoute
}