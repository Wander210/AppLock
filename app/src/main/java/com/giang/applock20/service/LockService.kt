package com.giang.applock20.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.giang.applock20.R
import com.giang.applock20.screen.lock.ValidationPatternActivity
import com.giang.applock20.util.AppInfoUtil

class LockService : Service() {
    private lateinit var usageStatsManager: UsageStatsManager
    private val handler = Handler(Looper.getMainLooper())
    private var lastApp = ""
    private val checkInterval = 2000L // Check every 2 seconds
    private val TAG = "LockService"
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "AppLockServiceChannel"
    private var isChecking = false
    private var isServiceStarted = false
    private var startAttempts = 0
    private val MAX_START_ATTEMPTS = 3

    companion object {
        private var notification: Notification? = null

        fun createNotification(context: Context): Notification {
            if (notification == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val name = "App Lock Service"
                    val descriptionText = "Service for monitoring and locking apps"
                    val importance = NotificationManager.IMPORTANCE_LOW
                    val channel = NotificationChannel("AppLockServiceChannel", name, importance).apply {
                        description = descriptionText
                    }
                    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(channel)
                }

                notification = NotificationCompat.Builder(context, "AppLockServiceChannel")
                    .setContentTitle("App Lock")
                    .setContentText("Service is running")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build()
            }
            return notification!!
        }
    }

    private val checkRunnable = object : Runnable {
        override fun run() {
            if (!isChecking && isServiceStarted) {
                isChecking = true
                try {
                    checkCurrentApp()
                } catch (e: Exception) {
                    // Silent catch to prevent service crash
                } finally {
                    isChecking = false
                }
            }
            handler.postDelayed(this, checkInterval)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        try {
            startForeground(NOTIFICATION_ID, createNotification(this))
            isServiceStarted = true
            startAttempts = 0
            Log.e(TAG, "LockService created and started successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start service: ${e.message}")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isServiceStarted && startAttempts < MAX_START_ATTEMPTS) {
            try {
                startForeground(NOTIFICATION_ID, createNotification(this))
                isServiceStarted = true
                startAttempts = 0
                Log.e(TAG, "LockService started in onStartCommand")
            } catch (e: Exception) {
                startAttempts++
                Log.e(TAG, "Failed to start service in onStartCommand (attempt $startAttempts): ${e.message}")
            }
        }

        if (isServiceStarted) {
            usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            handler.post(checkRunnable)
        }
        
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted = false
        handler.removeCallbacks(checkRunnable)
        Log.e(TAG, "LockService destroyed")
    }

    private fun checkCurrentApp() {
        val time = System.currentTimeMillis()
        val usageEvents = usageStatsManager.queryEvents(time - 2000, time)
        val event = UsageEvents.Event()
        var currentApp = ""

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                currentApp = event.packageName
            }
        }

        if (currentApp.isNotEmpty() && currentApp != lastApp && currentApp != packageName) {
            lastApp = currentApp
            val lockedApp = AppInfoUtil.listLockedAppInfo.find { it.packageName == currentApp }
            
            if (lockedApp != null) {
                Log.e(TAG, "Locked app detected: ${lockedApp.name}")
                val intent = Intent(this, ValidationPatternActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("packageName", currentApp)
                }
                startActivity(intent)
            }
        }
    }
}