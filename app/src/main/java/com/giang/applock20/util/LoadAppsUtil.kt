package com.giang.applock20.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.giang.applock20.model.AppInfo
import com.giang.applock20.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object LoadAppsUtil {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun loadApps(context: Context, lifecycleOwner: LifecycleOwner, viewModel: AppViewModel) {
        lifecycleOwner.lifecycleScope.launch {
            val appInfoList = withContext(Dispatchers.IO) {
                val packageManager = context.packageManager
                val installedApps = packageManager.getInstalledApplications(
                    PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
                )
                installedApps.mapNotNull { appInfo ->
                    val icon: Drawable = appInfo.loadIcon(packageManager)
                    val name = appInfo.loadLabel(packageManager).toString()
                    val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    val isRestricted = (appInfo.flags and ApplicationInfo.FLAG_INSTALLED) == 0
                    if (!isSystemApp && !isRestricted) {
                        AppInfo(icon, name)
                    } else {
                        null
                    }
                }
            }
            viewModel.setAppList(appInfoList)
        }
    }
}
