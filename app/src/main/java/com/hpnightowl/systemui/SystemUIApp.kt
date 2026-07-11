package com.hpnightowl.systemui

import android.app.Application
import android.util.Log

class SystemUIApp : Application() {
    companion object {
        private const val TAG = "SystemUIApp"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Custom SystemUI Application Started!")
    }
}
