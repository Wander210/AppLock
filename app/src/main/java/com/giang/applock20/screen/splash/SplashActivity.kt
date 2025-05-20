package com.giang.applock20.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.giang.applock20.R
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.constant.EXTRA_FROM_SPLASH
import com.giang.applock20.dao.AppInfoDatabase
import com.giang.applock20.databinding.ActivitySplashBinding
import com.giang.applock20.model.AppInfo
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.screen.language.LanguageActivity
import com.giang.applock20.screen.validate_lock_pattern.LockPatternActivity
import com.giang.applock20.util.AppInfoUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private lateinit var splashAnimation: Animation
    private lateinit var handler: Handler
    private lateinit var db: AppInfoDatabase

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initData() {
        splashAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_scaling)
        handler = Handler(Looper.getMainLooper())
        db = AppInfoDatabase.getInstance(this)
    }

    override fun setupView() {
        binding.imgSplashIcon.startAnimation(splashAnimation)
        lifecycleScope.launch {
            if (MyPreferences.read(MyPreferences.PREF_LOCK_PATTERN, null) == null)
                processAppDataAndNavigate(false)
            else
                processAppDataAndNavigate(true)
        }
    }

    private suspend fun processAppDataAndNavigate(hasLockPattern : Boolean) {
        val startTime = System.currentTimeMillis()
        //Initial 2 app lists
        withContext(Dispatchers.IO) {
            val appInfoDao = db.appInfoDAO()
            if(!hasLockPattern) {
                AppInfoUtil.initInstalledApps(this@SplashActivity)
                AppInfoUtil.listAppInfo.forEach{ appInfo ->
                appInfoDao.insertAppInfo(appInfo)
                }
            }
            else {
                AppInfoUtil.listAppInfo = appInfoDao.getAllApp() as ArrayList<AppInfo>
                AppInfoUtil.listLockedAppInfo = appInfoDao.getLockedApp() as ArrayList<AppInfo>
            }
        }

        val elapsedTime = System.currentTimeMillis() - startTime
        if (elapsedTime < 3000) delay(3000 - elapsedTime)

        navigateTo(hasLockPattern)
    }

    private fun navigateTo(hasLockPattern: Boolean) {
        if (!hasLockPattern) {
            startActivity(Intent(this, LanguageActivity::class.java).apply {
                putExtra(EXTRA_FROM_SPLASH, true)
            })
        } else startActivity(Intent(this, LockPatternActivity::class.java))
        finish()
    }

    override fun handleEvent() {}
}
