package com.pi.androidbasehiltmvvm.core.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import timber.log.Timber

/**
 * @property PermissionHelper is for handling App's permissions
 */
object PermissionHelper {
    private val cameraPermission =
        arrayOf(
            Manifest.permission.CAMERA
        )

    private val wifiPermission =
        arrayOf(
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
        )

    fun checkWifiPermissions(context: Context) =
        checkPermissions(context, wifiPermission)

    fun checkCameraPermissions(context: Context) =
        checkPermissions(context, cameraPermission)

    /**
     * Checks Permissions
     * @param permissions is the array of permissions
     * @return `true` if all permissions granted
     */
    private fun checkPermissions(
        context: Context,
        permissions: Array<String>
    ): Boolean {
        return permissions.all {
            Timber.d("PERMISSION $it")
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}
