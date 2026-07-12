package com.android.systemui

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.hpnightowl.systemui.manager.SystemBarManager

class SystemUIService : Service() {
    private lateinit var systemBarManager: SystemBarManager

    override fun onCreate() {
        super.onCreate()
        android.util.Log.d("SystemUIService", "Custom SystemUI Service Started!")
        systemBarManager = SystemBarManager(this)
        systemBarManager.start()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        systemBarManager.destroy()
    }
}
