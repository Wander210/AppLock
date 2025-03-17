package com.giang.applock20.screen.home

import android.graphics.Color
import android.graphics.Shader
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.giang.applock20.R
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.dao.AppInfoDatabase
import com.giang.applock20.databinding.ActivityHomeBinding
import com.giang.applock20.screen.home.all_app.AllAppFragment
import com.giang.applock20.screen.home.locked_app.LockedAppFragment
import com.giang.applock20.util.AppInfoUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun setupView() {
        binding.apply {
            viewPager2.adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)
            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tabLayout.getTabAt(position)?.select()
                    updateTabLayoutTextColor(position)

                    when (position) {
                        0 -> AllAppFragment()
                        1 -> LockedAppFragment()
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val db = AppInfoDatabase.getInstance(this@HomeActivity)
        val appInfoDao = db.appInfoDAO()
        lifecycleScope.launch {
            appInfoDao.deleteAll()

            AppInfoUtil.listAppInfo.forEach { appInfo ->
                appInfoDao.insertAppInfo(appInfo)
            }

            AppInfoUtil.listLockedAppInfo.forEach { appInfo ->
                appInfoDao.insertAppInfo(appInfo)
            }
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
        }
    }

    fun updateTabLayoutTextColor(selectedPosition: Int) {
        for (i in 0 until binding.tabLayout.tabCount) {
            val colors = if (i == selectedPosition) intArrayOf(
                resources.getColor(
                    R.color.gradient_start,
                    null
                ), resources.getColor(R.color.gradient_end, null)
            ) else intArrayOf(Color.parseColor("#ACACAC"), Color.parseColor("#ACACAC"))

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
                if (tabViewChild is TextView) (tabViewChild).typeface = typeface
            }
        }
    }
}