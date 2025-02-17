package com.giang.applock20.screen.lock_pattern

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import com.giang.applock20.databinding.ActivityLockPatternBinding
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.screen.base.BaseActivity
import com.giang.applock20.screen.lock_pattern.PatternLockView.PatternViewMode
import com.giang.applock20.screen.lock_pattern.listener.PatternLockViewListener
import com.giang.applock20.util.AnimationUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LockPatternActivity : BaseActivity() {

    private lateinit var binding: ActivityLockPatternBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLockPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gson = Gson()

        val json = MyPreferences.read(MyPreferences.PREF_LOCK_PATTERN, null)
        val type = object : TypeToken<List<PatternLockView.Dot>>() {}.type
        val correctPattern: List<PatternLockView.Dot> = gson.fromJson(json, type)

        var tempPattern : ArrayList<PatternLockView.Dot>

        binding.patternLockView.addPatternLockListener(object : PatternLockViewListener {
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
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.patternLockView.clearPattern()
                    }, 1000)
                }

            }

            override fun onCleared() {

            }
        })
    }
}