package com.hpnightowl.systemui.service

import android.app.Service
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.IBinder
import android.util.Log
import com.hpnightowl.systemui.manager.CommandQueueManager
import com.hpnightowl.systemui.manager.SystemBarManager
import com.hpnightowl.systemui.receiver.SystemUIConfigReceiver

class SystemUIService : Service() {
    companion object {
        private const val TAG = "SystemUIService"
    }

    private lateinit var displayManager: DisplayManager
    private lateinit var systemBarManager: SystemBarManager
    private lateinit var commandQueueManager: CommandQueueManager
    private lateinit var configReceiver: SystemUIConfigReceiver

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Custom SystemUI Service Started!")

        displayManager = getSystemService(DisplayManager::class.java)
        systemBarManager = SystemBarManager(this)
        systemBarManager.start()

        commandQueueManager = CommandQueueManager(systemBarManager)
        commandQueueManager.register()

        configReceiver = SystemUIConfigReceiver(systemBarManager)
        configReceiver.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        configReceiver.unregister(this)

        systemBarManager.destroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        // Return null for now; we'll implement IStatusBar later
        return null
    }
}
