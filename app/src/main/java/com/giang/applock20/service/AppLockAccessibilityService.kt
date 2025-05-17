package com.giang.applock20.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView
import android.widget.TextView
import com.giang.applock20.R
import com.giang.applock20.custom.lock_pattern.PatternLockView
import com.giang.applock20.custom.lock_pattern.listener.PatternLockViewListener
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.util.AnimationUtil
import com.giang.applock20.util.AppInfoUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppLockAccessibilityService : AccessibilityService() {
    private val TAG = "AppLockAccessibility"
    private var lastPackageName = ""
    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null
    private var isOverlayShown = false

    // Pattern cho xác thực
    private lateinit var correctPattern: List<PatternLockView.Dot>

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Tải pattern từ SharedPreferences
        loadSavedPattern()

        Log.i(TAG, "AppLockAccessibilityService đã được khởi tạo")
    }

    private fun loadSavedPattern() {
        val gson = Gson()
        val json = MyPreferences.read(MyPreferences.PREF_LOCK_PATTERN, null)
        if (json != null) {
            val type = object : TypeToken<List<PatternLockView.Dot>>() {}.type
            correctPattern = gson.fromJson(json, type)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "AppLockAccessibilityService đã được kết nối")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // Chỉ xử lý sự kiện WINDOW_STATE_CHANGED (khi chuyển ứng dụng)
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()

            // Nếu không có package name hoặc là package name của chính ứng dụng này, bỏ qua
            if (packageName.isNullOrEmpty() || packageName == "com.giang.applock20") {
                return
            }

            // Nếu package name giống với package name cuối cùng đã xử lý, bỏ qua
            if (packageName == lastPackageName) {
                return
            }

            val isLocked = AppInfoUtil.listLockedAppInfo.stream()
                .anyMatch { appInfo -> appInfo.packageName == packageName }

            Log.d(TAG, "Is app locked: $isLocked for package: $packageName")

            // Ghi log
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val logMessage = "[$timestamp] Ứng dụng được mở: $packageName"
            Log.i(TAG, logMessage)
            writeToLogFile(logMessage)

            if (isLocked) {
                Log.d(TAG, "Attempting to show overlay for: $packageName")
                showPatternLockOverlay(packageName)
            } else if (isOverlayShown) {
                hideOverlay()
            }

            lastPackageName = packageName
        }
    }

    private fun showPatternLockOverlay(packageName: String) {
        if (isOverlayShown) return

        try {
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            overlayView = inflater.inflate(R.layout.lock_pattern_overlay, null)

            // Lấy các view từ layout
            val patternLockView = overlayView?.findViewById<PatternLockView>(R.id.pattern_lock_view)
            val tvAppName = overlayView?.findViewById<TextView>(R.id.tv_app_name)
            val tvDrawPattern = overlayView?.findViewById<TextView>(R.id.tv_draw_an_unlock_pattern)

            // Cập nhật tên ứng dụng
            tvAppName?.text = getAppNameFromPackage(packageName)

            // Xử lý sự kiện pattern lock
            patternLockView?.addPatternLockListener(object : PatternLockViewListener {
                override fun onStarted() {
                    // Không cần xử lý
                }

                override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
                    // Không cần xử lý
                }

                override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                    val tempPattern = pattern?.let { ArrayList(it) } ?: arrayListOf()

                    if (!::correctPattern.isInitialized || pattern != correctPattern) {
                        // Pattern không đúng, hiển thị thông báo lỗi
                        AnimationUtil.setTextWrong(patternLockView, tvDrawPattern, tempPattern)
                    } else {
                        // Pattern đúng, ẩn overlay
                        patternLockView.setPattern(PatternLockView.PatternViewMode.CORRECT, tempPattern)

                        // Thêm độ trễ ngắn để người dùng thấy mẫu hình đúng
                        Handler(Looper.getMainLooper()).postDelayed({
                            hideOverlay()
                        }, 300)
                    }
                }

                override fun onCleared() {
                    // Không cần xử lý
                }
            })

            // Thiết lập tùy chọn cho overlay - Thay đổi từ TYPE_ACCESSIBILITY_OVERLAY sang TYPE_APPLICATION_OVERLAY
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                },
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
            )

            params.gravity = Gravity.CENTER

            // Hiển thị overlay - Thêm try-catch để bắt lỗi cụ thể
            try {
                windowManager.addView(overlayView, params)
                isOverlayShown = true

                // Cập nhật flag để có thể tương tác với overlay
                params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                windowManager.updateViewLayout(overlayView, params)

                Log.d(TAG, "Đã hiển thị overlay thành công")
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi khi addView: ${e.message}", e)
                isOverlayShown = false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi hiển thị màn hình khóa: ${e.message}")
        }
    }

    private fun hideOverlay() {
        if (!isOverlayShown || overlayView == null) return

        try {
            windowManager.removeView(overlayView)
            overlayView = null
            isOverlayShown = false

            Log.i(TAG, "Đã ẩn màn hình khóa")
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi ẩn màn hình khóa: ${e.message}")
        }
    }

    private fun getAppNameFromPackage(packageName: String): String {
        try {
            val packageManager = applicationContext.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            return packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: Exception) {
            return packageName
        }
    }

    private fun writeToLogFile(message: String) {
        try {
            val logFile = applicationContext.getFileStreamPath("app_access_log.txt")
            if (!logFile.exists()) {
                logFile.createNewFile()
            }

            applicationContext.openFileOutput("app_access_log.txt", Context.MODE_APPEND).use { output ->
                output.write("$message\n".toByteArray())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Không thể ghi log ra file: ${e.message}")
        }
    }

    override fun onInterrupt() {
        Log.i(TAG, "AppLockAccessibilityService bị gián đoạn")
    }

    override fun onDestroy() {
        super.onDestroy()

        // Đảm bảo ẩn overlay khi service bị hủy
        if (isOverlayShown) {
            hideOverlay()
        }

        Log.i(TAG, "AppLockAccessibilityService đã bị hủy")
    }
}