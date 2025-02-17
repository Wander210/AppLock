package com.giang.applock20.util

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.giang.applock20.screen.lock_pattern.PatternLockView
import com.giang.applock20.screen.lock_pattern.PatternLockView.PatternViewMode

object AnimationUtil {
    fun setTextWrong(patternLockView: PatternLockView, textView: TextView, pattern:  MutableList<PatternLockView.Dot>?) {
        patternLockView.setPattern(PatternViewMode.WRONG, pattern)
        val animator = ObjectAnimator.ofFloat(
            textView,
            "translationX",
            0f, 20f, -20f, 15f, -15f, 5f, -5f, 0f
        )
        animator.duration = 500

        textView.setTextColor(Color.RED)
        animator.start()

        Handler(Looper.getMainLooper()).postDelayed({
            textView.setTextColor(Color.BLACK)
            patternLockView.clearPattern()
        }, 500)
    }

}
