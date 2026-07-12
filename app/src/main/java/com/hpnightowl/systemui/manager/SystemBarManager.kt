package com.hpnightowl.systemui.manager

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.hpnightowl.systemui.model.BarConfig
import com.hpnightowl.systemui.ui.controller.DisplayWindowController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SystemBarManager(private val context: Context) : DisplayManager.DisplayListener {

    private val displayManager = context.getSystemService(DisplayManager::class.java)
    private val displayControllers = mutableMapOf<Int, DisplayWindowController>()
    private val _barConfigs = MutableStateFlow<Map<Int, BarConfig>>(emptyMap())
    val barConfigs: StateFlow<Map<Int, BarConfig>> = _barConfigs.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.S)
    fun start() {
        displayManager.registerDisplayListener(this, null)
        // Initialize for all currently connected displays
        displayManager.displays.forEach { display ->
            onDisplayAdded(display.displayId)
        }
    }

    fun destroy() {
        displayManager.unregisterDisplayListener(this)
        displayControllers.values.forEach { it.destroy() }
        displayControllers.clear()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDisplayAdded(displayId: Int) {
        val display = displayManager.getDisplay(displayId) ?: return
        Log.d("SystemBarManager", "Display added: $displayId")

        // Initialize default config for this display
        val res = context.resources
        val initialConfig = BarConfig(
            topZOrder = res.getInteger(com.hpnightowl.systemui.R.integer.config_topSystemBarZOrder),
            bottomZOrder = res.getInteger(com.hpnightowl.systemui.R.integer.config_bottomSystemBarZOrder),
            leftZOrder = res.getInteger(com.hpnightowl.systemui.R.integer.config_leftSystemBarZOrder),
            rightZOrder = res.getInteger(com.hpnightowl.systemui.R.integer.config_rightSystemBarZOrder)
        )
        _barConfigs.update { current ->
            current + (displayId to initialConfig)
        }

        val controller = DisplayWindowController(context, display, this)
        displayControllers[displayId] = controller
        controller.attachBars()
    }

    override fun onDisplayRemoved(displayId: Int) {
        Log.d("SystemBarManager", "Display removed: $displayId")
        displayControllers.remove(displayId)?.destroy()

        _barConfigs.update { current ->
            current - displayId
        }
    }

    override fun onDisplayChanged(displayId: Int) {
        // Handle rotation or resolution changes if needed
    }

    fun updateConfig(displayId: Int, modifier: (BarConfig) -> BarConfig) {
        _barConfigs.update { current ->
            val existing = current[displayId] ?: return@update current
            current + (displayId to modifier(existing))
        }
    }
}
