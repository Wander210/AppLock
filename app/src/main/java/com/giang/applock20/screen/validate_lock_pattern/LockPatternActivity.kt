package com.giang.applock20.screen.validate_lock_pattern

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import com.giang.applock20.databinding.ActivityLockPatternBinding
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.custom.lock_pattern.PatternLockView
import com.giang.applock20.custom.lock_pattern.PatternLockView.PatternViewMode
import com.giang.applock20.custom.lock_pattern.listener.PatternLockViewListener
import com.giang.applock20.screen.home.HomeActivity
import com.giang.applock20.util.AnimationUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LockPatternActivity : BaseActivity<ActivityLockPatternBinding>() {

    private lateinit var correctPattern: List<PatternLockView.Dot>
    private lateinit var tempPattern : ArrayList<PatternLockView.Dot>

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityLockPatternBinding {
        return ActivityLockPatternBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initData() {
        val gson = Gson()
        val json = MyPreferences.read(MyPreferences.PREF_LOCK_PATTERN, null)
        val type = object : TypeToken<List<PatternLockView.Dot>>() {}.type
        correctPattern = gson.fromJson(json, type)
    }

    override fun setupView() {
    }

    override fun handleEvent() {
        binding.patternLockView.addPatternLockListener(object :
            PatternLockViewListener {
            override fun onStarted() {

            }

            override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {

            }

            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                tempPattern = pattern?.let { ArrayList(it) } ?: arrayListOf()

                if (pattern != correctPattern) {
                    AnimationUtil.setTextWrong(binding.patternLockView, binding.tvDrawAnUnlockPattern, tempPattern)

                } else {
                    binding.patternLockView.setPattern(PatternViewMode.CORRECT, tempPattern)
                    startActivity(Intent(this@LockPatternActivity,  HomeActivity::class.java))
                    finish()
                }

            }

            override fun onCleared() {

            }
        })
    }
}