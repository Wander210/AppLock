package com.giang.applock20.screen.setting

import android.view.LayoutInflater
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun getViewBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
       return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    override fun setupView() {

    }

    override fun handleEvent() {
        binding.apply {
            imgToggle3.setOnClickListener {
                imgToggle3.startAnim {

                }
            }
        }
    }
}