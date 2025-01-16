package com.giang.applock20.screen.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.giang.applock20.R
import com.giang.applock20.databinding.ActivitySplashBinding
import com.giang.applock20.screen.language.LanguageActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        val splashAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.splash_scaling)
        val splashIconiv = binding.splashIconiv
        splashIconiv.startAnimation(splashAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
            finish()
        }, 3000)
    }

}