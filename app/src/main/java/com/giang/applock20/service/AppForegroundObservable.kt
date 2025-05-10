package com.giang.applock20.service

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import com.giang.applock20.model.UsageEventWrapper
import com.giang.applock20.util.PermissionChecker
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit


class AppForegroundObservable(val context: Context) {

    private var foregroundFlowable: Flowable<Pair<String, Int>>? = null

    fun get(): Flowable<Pair<String, Int>> {
        foregroundFlowable = getForegroundObservable()
        return foregroundFlowable!!
    }

    private fun getForegroundObservable(): Flowable<Pair<String, Int>> {
        return Flowable.interval(100, TimeUnit.MILLISECONDS)
            .filter { 
                val hasPermission = PermissionChecker.checkUsageAccessPermission(context)
                Log.e("AppForegroundObservable", "Usage access permission: $hasPermission")
                hasPermission
            }
            .map {
                var usageEvent: UsageEvents.Event? = null
                val usageStatsManager = context.getSystemService(Service.USAGE_STATS_SERVICE) as UsageStatsManager
                val time = System.currentTimeMillis()
                val usageEvents = usageStatsManager.queryEvents(time - 30000, time + 2500)
                val event = UsageEvents.Event()
                while (usageEvents.hasNextEvent()) {
                    usageEvents.getNextEvent(event)
                    if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED &&
                        event.packageName != "com.google.android.packageinstaller" ||
                        event.packageName == "com.google.android.packageinstaller" &&
                        event.className == "com.android.packageinstaller.UninstallerActivity") {
                        usageEvent = event
                    }
                }
                UsageEventWrapper(usageEvent)
            }
            .filter { 
                val hasEvent = it.usageEvent != null
                Log.e("AppForegroundObservable", "Filtered event: hasEvent=$hasEvent")
                hasEvent
            }
            .map { it.usageEvent!! }
            .filter { 
                val isNotSelf = it.packageName != null && it.packageName.contains(context.packageName).not()
                Log.e("AppForegroundObservable", "Filtered package: packageName=${it.packageName}, isNotSelf=$isNotSelf")
                isNotSelf
            }
            .map { event ->
                Pair(event.packageName, event.eventType)
            }
            .distinctUntilChanged()
    }
}
