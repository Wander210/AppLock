package com.giang.applock20.screen.home

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Shader
import android.os.Build
import android.provider.Settings
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.giang.applock20.R
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.databinding.ActivityHomeBinding
import com.giang.applock20.screen.setting.SettingActivity
import com.giang.applock20.util.PermissionUtils
import com.google.android.material.tabs.TabLayout

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private lateinit var permissionUtils: PermissionUtils

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun setupView() {
        permissionUtils = PermissionUtils(this)
        // Yêu cầu các quyền cần thiết
        checkAndRequestPermissions()
        val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(intent)

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

    private fun checkAndRequestPermissions() {
        if (!permissionUtils.checkUsageStatsPermission()) {
            permissionUtils.requestUsageStatsPermission()
        }

        // Kiểm tra quyền Overlay
        if (!permissionUtils.checkOverlayPermission()) {
            permissionUtils.requestOverlayPermission()
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

    private fun checkUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun requestUsageStatsPermission() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}