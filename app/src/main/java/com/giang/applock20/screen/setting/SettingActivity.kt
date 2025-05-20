package com.giang.applock20.screen.setting

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.Toast
import com.giang.applock20.base.BaseActivity
import com.giang.applock20.constant.EXTRA_FROM_SPLASH
import com.giang.applock20.databinding.ActivitySettingBinding
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.screen.language.LanguageActivity
import com.giang.applock20.screen.set_new_lock_pattern.SetLockPatternActivity

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
            itemHidePatternDrawPath.setOnClickListener {
                MyPreferences.write(MyPreferences.IS_HIDE_DRAW_PATTERN, !MyPreferences.read(MyPreferences.IS_HIDE_DRAW_PATTERN, false))
                updateSwitchHidePatternUI()
            }

            itemChangePassword.setOnClickListener {
                startActivity(Intent(this@SettingActivity, SetLockPatternActivity::class.java))
                finish()
            }

            itemChangeLanguages.setOnClickListener {
                startActivity(Intent(this@SettingActivity, LanguageActivity::class.java))
                finish()
            }

            itemShareWithFriends.setOnClickListener {
                shareApp(this@SettingActivity)
            }

            itemFeedback.setOnClickListener {
                sendFeedback(this@SettingActivity)
            }

            imgBack.setOnClickListener {
                finish()
            }
        }
    }

    fun shareApp(context: Context) {
        val appPackageName = context.packageName
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Hãy thử ứng dụng này nhé: https://play.google.com/store/apps/details?id=$appPackageName"
            )
        }

        val chooser = Intent.createChooser(shareIntent, "Chia sẻ ứng dụng qua")
        // Kiểm tra nếu có ứng dụng xử lý được intent
        if (shareIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooser)
        } else {
            Toast.makeText(context, "Không tìm thấy ứng dụng để chia sẻ", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendFeedback(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf("hoaocshit@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback for Your App")
            putExtra(Intent.EXTRA_TEXT, "Hello,\n\nI would like to provide the following feedback:\n")
        }

        try {
            context.startActivity(Intent.createChooser(intent, "Send Feedback"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No email app found to send feedback", Toast.LENGTH_SHORT).show()
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