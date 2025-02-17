package com.giang.applock20.screen

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.giang.applock20.R

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)

        val installedApps = packageManager.getInstalledApplications(
            PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        )

        for (appInfo in installedApps) {
            val icon: Drawable = appInfo.loadIcon(packageManager)

            val imageView = ImageView(this).apply {
                setImageDrawable(icon)
                layoutParams = LinearLayout.LayoutParams(150, 150).apply {
                    setMargins(8, 8, 8, 8)
                }
                adjustViewBounds = true
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            linearLayout.addView(imageView)
        }
    }
}
