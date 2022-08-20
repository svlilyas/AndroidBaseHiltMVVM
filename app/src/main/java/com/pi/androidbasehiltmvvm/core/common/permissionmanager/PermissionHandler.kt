package com.pi.androidbasehiltmvvm.core.common.permissionmanager

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

var PERMISSION_REQUEST_CODE = 222

object PermissionHandler {

    fun requestPermission(
        activity: Activity,
        requestCode: Int,
        requestedPermissions: Array<String>,
        successAction: () -> Unit
    ) {
        if (needPermissions(activity, requestedPermissions)) {
            getPermissions(activity, requestedPermissions, requestCode)
        } else {
            successAction.invoke()
        }
    }

    fun requestPermission(
        fragment: Fragment,
        requestCode: Int,
        requestedPermissions: Array<String>,
        action: () -> Unit
    ) {
        fragment.activity.let {
            if (needPermissions(it!!, requestedPermissions)) {
                getPermissions(fragment, requestedPermissions, requestCode)
            } else {
                action.invoke()
            }
        }
    }

    fun permissionsResult(
        requestedPermissions: Array<String>,
        requestCode: Int,
        grantResults: IntArray,
        errorMessageAction: (() -> Unit)? = null,
        successAction: () -> Unit,
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.size == requestedPermissions.size) {
            var allGranted = true
            for (gr in grantResults) {
                allGranted = allGranted && gr == PackageManager.PERMISSION_GRANTED
            }
            when {
                allGranted -> successAction()

                else -> errorMessageAction?.invoke()
            }
        }
    }

    private fun needPermissions(activity: Activity, requestedPermissions: Array<String>): Boolean {
        for (permission in requestedPermissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                return true
            }
        }
        return false
    }

    fun checkPermissions(activity: Activity, requestedPermissions: Array<String>): Boolean {
        for (permission in requestedPermissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
        }
        return false
    }

    private fun shouldShowRationaleAllPermissions(
        activity: Activity,
        requestedPermissions: Array<String>
    ): Boolean {
        for (permission in requestedPermissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true
            }
        }
        return false
    }

    fun getPermissions(
        activity: Activity,
        requestedPermissions: Array<String>,
        requestCode: Int = 0
    ) {
        PERMISSION_REQUEST_CODE = requestCode
        ActivityCompat.requestPermissions(activity, requestedPermissions, requestCode)
    }

    private fun getPermissions(
        fragment: Fragment,
        requestedPermissions: Array<String>,
        requestCode: Int = 0
    ) {
        PERMISSION_REQUEST_CODE = requestCode
        fragment.requestPermissions(requestedPermissions, requestCode)
    }
}
