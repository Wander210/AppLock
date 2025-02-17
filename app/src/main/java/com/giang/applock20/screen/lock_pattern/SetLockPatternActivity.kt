package com.giang.applock20.screen.lock_pattern

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.giang.applock20.R
import com.giang.applock20.databinding.ActivitySetLockPatternBinding
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.screen.base.BaseActivity
import com.giang.applock20.screen.lock_pattern.PatternLockView.PatternViewMode
import com.giang.applock20.screen.lock_pattern.listener.PatternLockViewListener
import com.giang.applock20.util.AnimationUtil
import com.google.gson.Gson

class SetLockPatternActivity : BaseActivity() {

    private lateinit var binding: ActivitySetLockPatternBinding
    private lateinit var tempPattern : ArrayList<PatternLockView.Dot>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySetLockPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var correctPattern = ArrayList<PatternLockView.Dot>()

        binding.patternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onStarted() {

            }

            override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {

            }

            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                val length = pattern?.size ?: 0
                tempPattern = pattern?.let { ArrayList(it) } ?: arrayListOf()
                if(length < 4) {
                    AnimationUtil.setTextWrong(binding.patternLockView, binding.tvDrawAnUnlockPattern, tempPattern)
                } else {
                    if(correctPattern.isEmpty()) {
                        correctPattern = pattern?.let { ArrayList(it) } ?: arrayListOf()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.patternLockView.clearPattern()
                            changeToConfirmPatternUI()
                        }, 2000)
                    } else {
                        confirmPattern(correctPattern, pattern)
                    }
                }
            }


            override fun onCleared() {
            }
        })
    }

    private fun changeToConfirmPatternUI() {
        binding.apply {
            tvNumberStep2.setTextColor(ContextCompat.getColor(this@SetLockPatternActivity, R.color.white))
            ivStepTwo.setImageDrawable(ContextCompat.getDrawable(this@SetLockPatternActivity, R.drawable.iv_step_1))
            ivProgressBar.setBackgroundColor(ContextCompat.getColor(this@SetLockPatternActivity, R.color.main_blue))
            tvDrawAnUnlockPattern.setText(R.string.draw_pattern_again_to_confirm)
            btnReset.visibility = View.VISIBLE
            tvReset.visibility = View.VISIBLE

        }
    }

    private fun changeToSetPatternUI() {
        binding.apply {
            tvNumberStep2.setTextColor(ContextCompat.getColor(this@SetLockPatternActivity, R.color.main_blue))
            ivStepTwo.setImageDrawable(ContextCompat.getDrawable(this@SetLockPatternActivity, R.drawable.iv_step_2))
            ivProgressBar.setBackgroundColor(ContextCompat.getColor(this@SetLockPatternActivity, R.color.grey))
            tvDrawAnUnlockPattern.setText(R.string.draw_an_unlock_pattern)
            btnReset.visibility = View.INVISIBLE
            tvReset.visibility = View.INVISIBLE
        }
    }

    private fun confirmPattern(correctPattern: ArrayList<PatternLockView.Dot>?, drawPattern: MutableList<PatternLockView.Dot>?) {
        if (tempPattern == correctPattern) {
            binding.patternLockView.setPattern(PatternViewMode.CORRECT, correctPattern)
            val gson = Gson()
            val json = gson.toJson(drawPattern)
            MyPreferences.write(MyPreferences.PREF_LOCK_PATTERN, json)

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@SetLockPatternActivity, LockPatternActivity::class.java)
                startActivity(intent)
            }, 1000)
        } else {
            AnimationUtil.setTextWrong(binding.patternLockView, binding.tvDrawAnUnlockPattern, tempPattern)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.patternLockView.clearPattern()
            }, 1000)
        }

        binding.btnReset.setOnClickListener({
            correctPattern?.clear()
            changeToSetPatternUI()
        })
    }

}