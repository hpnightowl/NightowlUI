package com.hpnightowl.systemui.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.hpnightowl.systemui.manager.SystemBarManager

class SystemUIConfigReceiver(private val manager: SystemBarManager) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_UPDATE_BARS) {
            val displayId = intent.getIntExtra(EXTRA_DISPLAY_ID, -1)
            if (displayId == -1) {
                Log.w("SystemUIConfig", "Missing displayId extra")
                return
            }

            Log.d("SystemUIConfig", "Received update request for display $displayId")

            manager.updateConfig(displayId) { current ->
                current.copy(
                    topEnabled = intent.getBooleanExtra(EXTRA_TOP_ENABLED, current.topEnabled),
                    bottomEnabled = intent.getBooleanExtra(
                        EXTRA_BOTTOM_ENABLED,
                        current.bottomEnabled
                    ),
                    leftEnabled = intent.getBooleanExtra(EXTRA_LEFT_ENABLED, current.leftEnabled),
                    rightEnabled = intent.getBooleanExtra(EXTRA_RIGHT_ENABLED, current.rightEnabled)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun register(context: Context) {
        val filter = IntentFilter(ACTION_UPDATE_BARS)
        context.registerReceiver(this, filter, Context.RECEIVER_EXPORTED)
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(this)
    }

    companion object {
        const val ACTION_UPDATE_BARS = "com.android.systemui.ACTION_UPDATE_BARS"
        const val EXTRA_DISPLAY_ID = "displayId"
        const val EXTRA_TOP_ENABLED = "top"
        const val EXTRA_BOTTOM_ENABLED = "bottom"
        const val EXTRA_LEFT_ENABLED = "left"
        const val EXTRA_RIGHT_ENABLED = "right"
    }
}
