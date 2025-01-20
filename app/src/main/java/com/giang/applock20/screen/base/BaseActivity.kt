package com.giang.applock20.screen.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.giang.applock20.util.LanguageUtil

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageUtil.setLanguage(this@BaseActivity)
    }
}