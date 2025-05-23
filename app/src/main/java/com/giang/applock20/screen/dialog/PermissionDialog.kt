package com.giang.applock20.screen.dialog

import com.giang.applock20.R
import com.giang.applock20.base.BaseDialogFragment
import com.giang.applock20.databinding.DialogPermissionBinding
import com.giang.applock20.util.PermissionUtil

class PermissionDialog :
    BaseDialogFragment<DialogPermissionBinding>(DialogPermissionBinding::inflate) {

    var onGotoSettingClick: (() -> Unit)? = null
    var onToggleUsageClick: (() -> Unit)? = null
    var onToggleOverlayClick: (() -> Unit)? = null
    var onToggleDeviceAdminClick: (() -> Unit)? = null

    override fun onViewCreated() {
        binding.btnUsageToggle.setOnClickListener {
            onGotoSettingClick?.invoke()
        }
        binding.btnOverlayToggle.setOnClickListener {
            onToggleOverlayClick?.invoke()
        }
        binding.btnDeviceAdminToggle.setOnClickListener {
            onToggleDeviceAdminClick?.invoke()
        }
        binding.btnGoToSetting.setOnClickListener {
            onGotoSettingClick?.invoke()
        }
    }

    fun updateToggle() {
        binding.btnUsageToggle.setImageResource(
            if (PermissionUtil.checkUsageStatsPermission()) R.drawable.ic_toggle_inactive
            else R.drawable.ic_toggle_active
        )
        binding.btnOverlayToggle.setImageResource(
            if (PermissionUtil.checkOverlayPermission()) R.drawable.ic_toggle_inactive
            else R.drawable.ic_toggle_active
        )
        binding.btnDeviceAdminToggle.setImageResource(
            if (PermissionUtil.checkOverlayPermission()) R.drawable.ic_toggle_inactive
            else R.drawable.ic_toggle_active
        )
        if (PermissionUtil.isAllPermissionRequested()) {
            dismiss()
        }
    }
}