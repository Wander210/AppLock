package com.giang.applock

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.giang.applock20.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        val splashAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.splash_scaling)
        val splashIconiv = findViewById<ImageView>(R.id.splashIconiv)
        splashIconiv.startAnimation(splashAnimation)
    }
}