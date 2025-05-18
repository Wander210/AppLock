package com.giang.applock20.screen.dialog

import android.view.View
import com.giang.applock20.R
import com.giang.applock20.base.BaseDialogFragment
import com.giang.applock20.databinding.DialogPermissionBinding
import com.giang.applock20.preference.MyPreferences
import com.giang.applock20.util.PermissionUtils

class PermissionDialog :
    BaseDialogFragment<DialogPermissionBinding>(DialogPermissionBinding::inflate) {
    private var numStar = 0
    var onGotoSettingClick: (() -> Unit)? = null
    var onToggleUsageClick: (() -> Unit)? = null
    var onToggleOverlayClick: (() -> Unit)? = null

    override fun onViewCreated() {
        binding.btnUsageToggle.setOnClickListener {
            onGotoSettingClick?.invoke()
        }
        binding.btnOverlayToggle.setOnClickListener {
            onToggleOverlayClick?.invoke()
        }
        binding.btnGoToSetting.setOnClickListener {
            onGotoSettingClick?.invoke()
        }
    }

    fun updateToggle() {
        binding.btnUsageToggle.setImageResource(
            if (PermissionUtils.checkUsageStatsPermission()) R.drawable.ic_toggle_inactive
            else R.drawable.ic_toggle_active
        )
        binding.btnOverlayToggle.setImageResource(
            if (PermissionUtils.checkOverlayPermission()) R.drawable.ic_toggle_inactive
            else R.drawable.ic_toggle_active
        )

        if (PermissionUtils.isAllPermissisionRequested()) {
            dismiss()
        }
    }
}