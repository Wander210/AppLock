package com.giang.applock20.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.app.usage.UsageEvents
import android.content.Intent
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationManagerCompat
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.preference.MyPreferences.PREF_IS_PREVENT_UNINSTALL
import com.giang.applock20.util.AppInfoUtil.listLockedAppInfo
import com.giang.applock20.util.PermissionChecker
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers


private lateinit var windowManager: WindowManager
private lateinit var overlayParams: WindowManager.LayoutParams
private lateinit var overlayView: View
private const val NOTIFICATION_ID_APP_LOCKER_SERVICE = 125555
private const val NOTIFICATION_ID_APP_LOCKER_PERMISSION_NEED = 2

class LockService : Service() {

    private var notification: Notification? = null
    private val dbDisposables: CompositeDisposable = CompositeDisposable()
    private val serviceNotificationManager by lazy { ServiceNotificationManager(this) }
    private var tryAttempt = 0
    private var packageNameLock = ""
    private var isOverlayShowing = false
    private var unLockTime = 0L
    private var foregroundAppDisposable: Disposable? = null
    private val appForegroundObservable by lazy { AppForegroundObservable(this) }
    private val allDisposables: CompositeDisposable = CompositeDisposable()
    private val lockedAppPackageSet: Set<String> = listLockedAppInfo
        .map { it.packageName }
        .toSet()
    private val events by lazy { arrayListOf<Int>() }
    private var lastForegroundAppPackage: String? = null
    private var isUninstallNotDone = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initializeAppLockerNotification()
        Log.e("ThoNH", "Service created")
//        initHandleMessage()
//        initializeOverlayView()
//        registerScreenReceiver()
//        observeDB()
//        observeDBForLockedAppsChanged()
//        observeOverlayView()
//
        observeForegroundApplication()
//        startPermissionCheckerInterval()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("ThoNH", "Service started with startId: $startId")

//        if (intent?.action.equals("START_DATABASE_NEW")) {
//            if (dbDisposables.isDisposed.not()) {
//                dbDisposables.dispose()
//            }
//            observeDBForLockedAppsChanged()
//            observeOverlayView()
//        }
//        if (intent?.action.equals("STOP_FORE_GROUND_ACTION")) {
//            stopForeground(STOP_FOREGROUND_REMOVE)
//            stopSelfResultAndLog(startId)
//        } else {
//            initializeAppLockerNotification()
//        }

        return START_STICKY
    }

//    private fun observeDBForLockedAppsChanged() {
//        db.lockAppDao().getLockedApps().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread()).subscribe({ lockedAppList ->
//                lockedAppPackageSet.clear()
//                lockedAppList.forEach { lockedAppPackageSet.add(it.parsePackageName()) }
//                SystemPackages.getSystemPackages().forEach { lockedAppPackageSet.add(it) }
//            }, { error -> error.printStackTrace() }).also {
//                dbDisposables.add(it)
//            }
//    }
//
//    private fun observeOverlayView() {
//        if (SessionManager.getInstance().getAppSetting().passType == SessionManager.PATTERN) {
//            Flowable.combineLatest(
//                db.patternDao().getPatternAsync().map { it.patternMetadata.pattern },
//                validatedPatternObservable.toFlowable(BackpressureStrategy.BUFFER),
//                PatternValidatorFunction()
//            ).subscribe(this@LockService::onPatternValidated).also {
//                dbDisposables.add(it)
//            }
//        }
//    }
//
//    private fun stopSelfResultAndLog(startId: Int) {
//        FirebaseAnalyticsUtil.onLogEvent(this, "on_stop_self_result")
//        Log.e("ThoNH", "stopSelfResultAndLog: $startId")
//        stopSelfResult(startId)
//    }

    @SuppressLint("MissingPermission")
    private fun initializeAppLockerNotification() {
        Log.e("ThoNH", "Service initializeAppLockerNotification")
        try {
            if (notification == null) {
                notification = serviceNotificationManager.createNotification()
                NotificationManagerCompat.from(serviceNotificationManager.context)
                    .notify(NOTIFICATION_ID_APP_LOCKER_SERVICE, notification!!)
            }
            startForeground(NOTIFICATION_ID_APP_LOCKER_SERVICE, notification)
        } catch (e: Exception) {
            tryAttempt++

            if (tryAttempt < 3) {
                FirebaseAnalytics.getInstance(this)
                    .logEvent("initializeAppLockerNotification_$tryAttempt", null)
                initializeAppLockerNotification()
            }

            e.printStackTrace()
            FirebaseAnalytics.getInstance(this)
                .logEvent("dev_cc_${e.message?.takeLast(40)}", null)
        }
    }

//    object UnitHandler {
//        var handler: Handler? = null
//
//        fun sendMessageToHandler(what: Int, packageName: String) {
//            handler?.let {
//                val msg = Message.obtain()
//                msg.obj = packageName
//                msg.what = what
//                msg.target = it
//                msg.sendToTarget()
//            }
//        }
//    }


//    private fun initHandleMessage() {
//        handler = object : Handler(Looper.getMainLooper()) {
//            override fun handleMessage(msg: Message) {
//                when (msg.what) {
//                    3333 -> {
//                        //hide window
//                        try {
//                            System.out.println("XXXXXXX" + 3333333)
//                            windowManager.removeView(overlayView)
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//
//                    1111 -> {
//                        System.out.println("XXXXXXX" + 1)
//                        unLockOtherAppSuccess()
//                    }
//
//                    2222 -> {
//                        System.out.println("XXXXXXX" + 2)
//                        gotoHome()
//                    }
//
//                    1342 -> {
//                        packageNameLock = ""
//                        unLockOtherAppSuccess()
//                    }
//
//                    else -> {}
//                }
//            }
//        }
//    }
//
//    private fun unLockOtherAppSuccess() {
//        isOverlayShowing = false
//        unLockTime = System.currentTimeMillis()
//    }    private val screenOnOffReceiver by lazy {
//        object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//                intent?.action?.let { action ->
//                    println("LOLLLLLLL screenOnOffReceiver $action")
//                    if (action == Intent.ACTION_SCREEN_OFF) {
//
//                    }
//
//                    if (action == Intent.ACTION_SCREEN_ON) {
//                        onKeyMenuBottom()
//                        // What is the purpose of starting the service?
////                        ServiceStarter.startService(applicationContext)
//                    }
//
//                    when (action) {
//                        Intent.ACTION_PACKAGE_REMOVED -> {
//                            stopForegroundApplicationObserver()
//                        }
//
//                        else -> {
//                            observeForegroundApplication()
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun gotoHome() {
//        try {
//            packageNameLock = ""
//            isOverlayShowing = false
//            unLockTime = System.currentTimeMillis()
//            val intentHome = Intent("android.intent.action.MAIN")
//            intentHome.addCategory("android.intent.category.HOME")
//            intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intentHome)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun onKeyMenuBottom() {
//        packageNameLock = ""
//        isOverlayShowing = false
//    }
//
//    private fun stopForegroundApplicationObserver() {
//        if (foregroundAppDisposable != null && foregroundAppDisposable?.isDisposed?.not() == true) {
//            foregroundAppDisposable?.dispose()
//        }
//    }

    private fun observeForegroundApplication() {
        if (foregroundAppDisposable != null && foregroundAppDisposable?.isDisposed?.not() == true) {
            Log.e("ThoNH", "Foreground application observer already active")
            return
        }
        foregroundAppDisposable = appForegroundObservable.get().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ event ->
                onAppForeground(event)
            }, { error ->
                error.printStackTrace()
            }).also {
                allDisposables.add(it)
            }
    }

    @Synchronized
    private fun onAppForeground(event: Pair<String, Int>) {
        if (lockedAppPackageSet.isEmpty()) return
        // Synchronize on the package name string so that only one thread can process events for the same app at a time
        synchronized(event.first) {
            Log.e("ThoNH", "onAppForeground: package=${event.first}, eventCode=${event.second}")
            if (event.first.isBlank() || !isPermissionChecker()) return

            SystemPackages.getUnLockPackages().forEach { _ ->
                if (event.first.contains("launcher")) {
                    println("SystemPackages" + event.first)
                    onBack()
                    return@forEach
                }
            }
            if (lockedAppPackageSet.contains(event.first)) {
                when (event.second) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> {
                        // Only show lock overlay if enough time has passed since last unlock and we’re not already locking this package
                        if (System.currentTimeMillis() - unLockTime > 200 &&
                            packageNameLock != event.first
                        ) {
                            //showOverlay(event.first)
                        }
                    }
                }
                // Record that we saw this event (resume/stop) for later logic
                events.add(event.second)
            } else {
                // If the app is not in our locked set, check if “prevent uninstall” is enabled
                if (MyPreferences.read(PREF_IS_PREVENT_UNINSTALL, false)) {
                    if (event.first == "com.google.android.packageinstaller" &&
                        event.second == UsageEvents.Event.ACTIVITY_RESUMED &&
                        !isUninstallNotDone
                    ) {
                        //showOverlay(event.first)
                        // Mark that we have already blocked this uninstall session
                        events.add(event.second)
                        isUninstallNotDone = true
                        // Once the installer activity stops, reset the flag
                    } else if (event.first == "com.google.android.packageinstaller" &&
                        event.second == UsageEvents.Event.ACTIVITY_STOPPED
                    ) {
                        isUninstallNotDone = false
                    }
                }
            }
            // Always remember which package was last in the foreground
            lastForegroundAppPackage = event.first
        }
    }

    private fun isPermissionChecker(): Boolean =
        PermissionChecker.checkUsageAccessPermission(applicationContext) &&
                Settings.canDrawOverlays(applicationContext)

    private fun onBack() {
        if (!packageNameLock.isNullOrBlank() && !isOverlayShowing) {
            packageNameLock = ""
        }
    }


}