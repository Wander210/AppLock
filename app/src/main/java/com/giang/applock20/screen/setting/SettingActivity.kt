package com.giang.applock20.screen.setting

import android.content.Intent
import android.view.LayoutInflater
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.constant.EXTRA_FROM_SPLASH
import com.giang.applock20.databinding.ActivitySettingBinding
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.screen.language.LanguageActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun getViewBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
       return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun setupView() {
        updateSwitchHidePatternUI()
    }

    override fun handleEvent() {
        binding.apply {
            imgToggle.setOnClickListener {
                MyPreferences.write(MyPreferences.IS_HIDE_DRAW_PATTERN, !MyPreferences.read(MyPreferences.IS_HIDE_DRAW_PATTERN, false))
                updateSwitchHidePatternUI()
            }

            binding.btnChangeLanguages.setOnClickListener {
                startActivity(Intent(this@SettingActivity, LanguageActivity::class.java))
            }

            imgBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun updateSwitchHidePatternUI() {
        binding.imgToggle.setImageResource(
            if (MyPreferences.read(MyPreferences.IS_HIDE_DRAW_PATTERN, false)) {
                com.giang.applock20.R.drawable.ic_toggle_inactive
            } else {
                com.giang.applock20.R.drawable.ic_toggle_active
            }
        )
    }
}