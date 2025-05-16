package com.giang.applock20.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.giang.applock20.R;
import com.giang.applock20.screen.lock.LockActivity;

public class AppMonitorService extends Service {
    public static boolean isLockActivityActive = false;

    private static final String CHANNEL_ID = "AppMonitorChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final long CHECK_INTERVAL = 100; // Check every 100ms
    private static final String TAG = "AppMonitor";

    private Handler handler;
    private Runnable monitorRunnable;
    private String lastForegroundApp = "";
    private UsageStatsManager usageStatsManager;

    private final String[] blockedApps = {
            "com.android.chrome"
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "AppMonitorService created");
        startForegroundService();
        usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "AppMonitorService started");
        startMonitoring();
        return START_STICKY;
    }

    private void startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "App Monitor Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("App đang theo dõi")
                .setContentText("Đang kiểm tra ứng dụng đang sử dụng")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    private void startMonitoring() {
        handler = new Handler();
        monitorRunnable = new Runnable() {
            @Override
            public void run() {
                checkForegroundApp();
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        };
        handler.post(monitorRunnable);
    }

    private void checkForegroundApp() {
        try {
            long time = System.currentTimeMillis();
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = usageStatsManager.queryEvents(time - 1000, time);
            String currentApp = "";

            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    currentApp = event.getPackageName();
                    Log.e(TAG, "Detected app in foreground: " + currentApp);
                }
            }

            if (!currentApp.isEmpty() && !currentApp.equals(lastForegroundApp) && !currentApp.equals(getPackageName())) {
                Log.e(TAG, "New app detected: " + currentApp);
                lastForegroundApp = currentApp;

                boolean isBlocked = false;
                for (String blocked : blockedApps) {
                    if (blocked.equals(currentApp)) {
                        isBlocked = true;
                        break;
                    }
                }

                if (isBlocked) {
                    Log.e(TAG, "Blocked app detected: " + currentApp);
                    startLockActivity();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking foreground app: " + e.getMessage());
        }
    }

    private void startLockActivity() {
        if (!isLockActivityActive) {
            Intent intent = new Intent(this, LockActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            isLockActivityActive = true;
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "AppMonitorService destroyed");
        if (handler != null) {
            handler.removeCallbacks(monitorRunnable);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
