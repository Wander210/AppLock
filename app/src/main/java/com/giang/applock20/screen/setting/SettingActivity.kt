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
            imgToggle1.setOnClickListener {
                imgToggle1.startAnim {

                }
            }

            imgToggle2.setOnClickListener {
                imgToggle2.startAnim {

                }
            }
            imgToggle3.setOnClickListener {
                imgToggle3.startAnim {

                }
            }
            imgToggle4.setOnClickListener {
                imgToggle4.startAnim {

                }
            }
            imgToggle5.setOnClickListener {
                imgToggle5.startAnim {

                }
            }
            imgToggle6.setOnClickListener {
                imgToggle6.startAnim {

                }
            }
            imgChevron1.setOnClickListener {
                imgChevron1.startAnim {

                }
            }
            imgChevron2.setOnClickListener {
                imgChevron2.startAnim {

                }
            }
            imgChevron3.setOnClickListener {
                imgChevron3.startAnim {

                }
            }
            imgChevron4.setOnClickListener {
                imgChevron4.startAnim {

                }
            }
            imgChevron5.setOnClickListener {
                imgChevron5.startAnim {

                }
            }
            imgChevron6.setOnClickListener {
                imgChevron6.startAnim {

                }
            }
        }
    }
}