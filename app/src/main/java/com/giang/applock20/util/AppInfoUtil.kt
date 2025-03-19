package com.giang.applock20.util

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import com.giang.applock20.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

object AppInfoUtil {
    var listAppInfo = ArrayList<AppInfo>()
    var listLockedAppInfo = ArrayList<AppInfo>()


    fun initInstalledApps(context: Context) {
        val packageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)

        for (resolveInfo in resolveInfoList) {
            val activityInfo = resolveInfo.activityInfo
            if (activityInfo != null) {
                val name: String = activityInfo.loadLabel(packageManager).toString()
                val icon: Drawable = activityInfo.loadIcon(packageManager)
                val packageName : String = activityInfo.packageName

                listAppInfo.add(AppInfo(icon, name, packageName, false))

            }
        }
        listAppInfo.sortWith(compareBy { it.name })
    }

    //Binary sort
    fun insertSortedAppInfo(sortedList: MutableList<AppInfo>, newApp: AppInfo) {
        val index = sortedList.binarySearchBy(newApp.name) { it.name }
        val insertIndex = if (index >= 0) index else -index - 1
        sortedList.add(insertIndex, newApp)
    }
}
