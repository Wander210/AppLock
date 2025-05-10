package com.giang.applock20.util

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.giang.applock20.R
import pub.devrel.easypermissions.EasyPermissions

object PermissionChecker {

    const val REQUEST_NOTIFICATION = 11111

    fun checkUsageAccessPermission(context: Context): Boolean {
        return try {
            val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
            val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            var mode = 0

            mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )

            //Compare and return true if the operation mode is “MODE_ALLOWED”
            mode == AppOpsManager.MODE_ALLOWED

        } catch (e: NameNotFoundException) {
            false
        }

    }

    fun checkOverlayPermission(context: Context) = Settings.canDrawOverlays(context)

    fun isAllPermissionChecked(context: Context) =
        checkUsageAccessPermission(context) && checkOverlayPermission(context)

    fun requestUsageAccessPermission(context: Context) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun haveNotificationPermission(context: Context, onGranted: () -> Unit){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onGranted.invoke()
            }
        } else {
            onGranted.invoke()
        }
    }

    fun hasPostNotifyPermission(context: FragmentActivity, onGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onGranted.invoke()
            } else {
                EasyPermissions.requestPermissions(
                    context,
                    context.getString(R.string.permission_notification),
                    REQUEST_NOTIFICATION,
                    Manifest.permission.POST_NOTIFICATIONS,
                )
            }
        } else {
            onGranted.invoke()
        }
    }
}