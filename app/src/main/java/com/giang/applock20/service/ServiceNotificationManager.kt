package com.giang.applock20.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.giang.applock20.R
import com.giang.applock20.screen.splash.SplashActivity

class ServiceNotificationManager(val context: Context) {
    @SuppressLint("MissingPermission")
    fun createNotification(): Notification {
        createAppLockerServiceChannel()

        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val resultIntent = Intent(context, SplashActivity::class.java)
        resultIntent.putExtra(EXTRA_NOTIFY, true)
        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, flags)

        return NotificationCompat.Builder(context, CHANNEL_ID_APP_LOCKER_SERVICE)
            .setSmallIcon(R.drawable.ic_round_lock_24px)
            .setContentTitle("AppLock")
            .setContentText("Protect your privacy")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(resultPendingIntent)
            .setOngoing(true)
            .build()
    }

    @SuppressLint("MissingPermission")
    fun createPermissionNeedNotification(): Notification {
        createAppLockerServiceChannel()

        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val resultIntent = Intent(context, SplashActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, flags)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_APP_LOCKER_SERVICE)
            .setSmallIcon(R.drawable.ic_round_lock_24px)
            .setContentTitle(context.getString(R.string.notification_permission_need_title))
            .setContentText(context.getString(R.string.notification_permission_need_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setOngoing(true)
            .setContentIntent(resultPendingIntent).build()
        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_ID_APP_LOCKER_PERMISSION_NEED, notification)
        return notification
    }

    fun hidePermissionNotification() {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID_APP_LOCKER_PERMISSION_NEED)
    }

    fun createAppLockerServiceChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel =
                NotificationChannel(CHANNEL_ID_APP_LOCKER_SERVICE, name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID_APP_LOCKER_SERVICE = "CHANNEL_ID_APP_LOCKER_SERVICE"
        private const val NOTIFICATION_ID_APP_LOCKER_SERVICE = 1
        private const val NOTIFICATION_ID_APP_LOCKER_PERMISSION_NEED = 2
        const val EXTRA_NOTIFY = "EXTRA_NOTIFY"
    }
}