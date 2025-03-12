package com.giang.applock20.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.giang.applock20.R
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.databinding.ActivitySplashBinding
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.screen.language.LanguageActivity
import com.giang.applock20.screen.validate_pattern_lock.LockPatternActivity
import com.giang.applock20.util.AppInfoUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initData() {
    }

    override fun setupView() {
        val splashAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.splash_scaling)
        binding.imgSplashIcon.startAnimation(splashAnimation)

        lifecycleScope.launch {
            val startTime = System.currentTimeMillis()
            withContext(Dispatchers.IO) {
                AppInfoUtil.initInstalledApps(this@SplashActivity)
            }
            val elapsedTime = System.currentTimeMillis() - startTime
            if (elapsedTime < 3000) delay(3000 - elapsedTime)

            if (MyPreferences.read(MyPreferences.PREF_LOCK_PATTERN, null) == null)
                startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, LockPatternActivity::class.java))
            finish()
        }
    }


    override fun handleEvent() {

    }
}