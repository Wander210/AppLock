package com.giang.applock20.screen.lock_pattern

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.giang.applock20.databinding.ActivityLockPatternBinding
import com.giang.applock20.screen.lock_pattern.PatternLockView.Dot
import com.giang.applock20.screen.lock_pattern.PatternLockView.PatternViewMode
import com.giang.applock20.screen.lock_pattern.listener.PatternLockViewListener

class LockPatternActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLockPatternBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLockPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val correctPattern = mutableListOf(
            PatternLockView.Dot.of(0, 0),
            PatternLockView.Dot.of(1, 0),
            PatternLockView.Dot.of(1, 1),
            PatternLockView.Dot.of(0, 1)
        )

        var tempPattern = ArrayList<PatternLockView.Dot>()

        binding.patternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onStarted() {

            }

            override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {

            }

            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                tempPattern = pattern?.let { ArrayList(it) } ?: arrayListOf()
                val mode = if (pattern == correctPattern) PatternViewMode.CORRECT else PatternViewMode.WRONG
                binding.patternLockView.setPattern(mode, tempPattern)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.patternLockView.clearPattern()
                }, 3000)
            }


            override fun onCleared() {

            }
        })
    }
}