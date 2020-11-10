package com.applehealth.androidApp.ui

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        enterFullScreen()
    }

    private fun enterFullScreen() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    }
}