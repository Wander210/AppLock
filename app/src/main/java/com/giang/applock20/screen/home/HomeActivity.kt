package com.giang.applock20.screen.home

import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Shader
import android.os.Build
import android.provider.Settings
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.giang.applock20.R
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.databinding.ActivityHomeBinding
import com.giang.applock20.screen.dialog.PermissionDialog
import com.giang.applock20.screen.setting.SettingActivity
import com.giang.applock20.service.LockService
import com.giang.applock20.util.PermissionUtils
import com.google.android.material.tabs.TabLayout

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun setupView() {
        checkAndRequestNotificationPermission()

        binding.apply {
            viewPager2.adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)
            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.getTabAt(position)?.select()
                    updateTabLayoutTextColor(position)
                }
            })
        }
    }

    override fun handleEvent() {
        binding.apply {
            tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if(tab != null) viewPager2.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            btnSetting.setOnClickListener({
                startActivity(Intent(this@HomeActivity, SettingActivity::class.java))

            })
        }
    }

    fun updateTabLayoutTextColor(selectedPosition: Int) {
        for (i in 0 until binding.tabLayout.tabCount) {

            val colors = if (i == selectedPosition) intArrayOf(
                resources.getColor(R.color.gradient_start, null),
                resources.getColor(R.color.gradient_end, null)
            ) else
                intArrayOf(Color.parseColor("#ACACAC"),
                    Color.parseColor("#ACACAC"))
            val tab = binding.tabLayout.getTabAt(i)
            val height = tab?.view?.height ?: 0
            val spannable = SpannableString(tab?.text)

            spannable.setSpan(GradientTextSpan(colors, height.toFloat()), 0, spannable.length, 0)
            tab?.text = spannable

            if (i == selectedPosition) setTabTypeface(binding.tabLayout.getTabAt(i))
        }
    }

    inner class GradientTextSpan(private val colors: IntArray, private val height: Float) :
        CharacterStyle() {
        override fun updateDrawState(tp: TextPaint) {
            val shader = android.graphics.LinearGradient(
                0f, 0f, tp.textSize, height,
                colors, null, Shader.TileMode.CLAMP

            )
            tp.shader = shader
        }
    }

    private fun setTabTypeface(tab: TabLayout.Tab?) {
        tab?.let {
            for (i in 0 until tab.view.childCount) {
                val typeface = ResourcesCompat.getFont(tab.view.context, R.font.exo_bold)
                val tabViewChild = tab.view.getChildAt(i)
                if (tabViewChild is TextView) tabViewChild.typeface = typeface
            }
        }
    }

    // 2. Hàm gọi để kiểm tra và xin quyền:
    fun checkAndRequestNotificationPermission() {
        // Chỉ cần request trên Android 13+ (SDK 33)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Đã có quyền
                    showDialogRequestPermission()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Nên giải thích cho người dùng vì sao cần quyền
                    AlertDialog.Builder(this)
                        .setTitle("Cho phép thông báo")
                        .setMessage("Ứng dụng cần quyền gửi thông báo để giữ cho các ứng dụng của bạn luôn an toàn")
                        .setPositiveButton("Đồng ý") { _, _ ->
                            requestNotificationPermissionLauncher.launch(
                                android.Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                        .setNegativeButton("Hủy", null)
                        .show()
                }
                else -> {
                    // Chưa request lần nào
                    requestNotificationPermissionLauncher.launch(
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }
        } else {
            // Trên Android < 13 không cần request runtime
            showDialogRequestPermission()
        }
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showDialogRequestPermission()
            } else {
                Toast.makeText(this, "Quyền thông báo bị từ chối", Toast.LENGTH_SHORT).show()
            }
        }

    private var permissionDialog: PermissionDialog? = null

    private fun showDialogRequestPermission() {
        if (PermissionUtils.isAllPermissisionRequested()) {
            ContextCompat.startForegroundService(this, Intent(this, LockService::class.java))
        } else {
            permissionDialog = PermissionDialog()
            permissionDialog?.show(supportFragmentManager, "rating_dialog")
            permissionDialog?.onToggleUsageClick = {
                PermissionUtils.requestUsageStatsPermission()
            }
            permissionDialog?.onToggleOverlayClick = {
                PermissionUtils.requestOverlayPermission()
            }
            permissionDialog?.onGotoSettingClick = {
                if (!PermissionUtils.checkUsageStatsPermission()) {
                    PermissionUtils.requestUsageStatsPermission()
                } else if (!PermissionUtils.checkOverlayPermission()) {
                    PermissionUtils.requestOverlayPermission()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        permissionDialog?.updateToggle()
    }
}