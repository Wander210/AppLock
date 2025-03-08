package com.giang.applock20.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.giang.applock20.R
import com.giang.applock20.databinding.ActivitySplashBinding
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.screen.validate_pattern_lock.LockPatternActivity
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.screen.language.LanguageActivity
import com.giang.applock20.util.AppInfoUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                AppInfoUtil.initInstalledApps(this@SplashActivity)
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if(MyPreferences.read(MyPreferences.PREF_LOCK_PATTERN, null) == null)
                startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, LockPatternActivity::class.java))
            finish()}, 3000)
    }

    override fun handleEvent() {

    }
}