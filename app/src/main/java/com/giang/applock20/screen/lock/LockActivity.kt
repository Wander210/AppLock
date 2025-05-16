package com.giang.applock20.screen.lock

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.databinding.ActivityLockBinding
import com.giang.applock20.service.AppMonitorService

class LockActivity : BaseActivity<ActivityLockBinding>() {
    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityLockBinding {
        return ActivityLockBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun setupView() {
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 3000)
    }

    override fun handleEvent() {

    }

    override fun onDestroy() {
        super.onDestroy()
        AppMonitorService.isLockActivityActive = false
    }
}