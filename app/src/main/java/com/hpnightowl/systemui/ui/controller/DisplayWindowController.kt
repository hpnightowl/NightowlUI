package com.hpnightowl.systemui.ui.controller

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Display
import android.view.Gravity
import android.view.InsetsState
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.hpnightowl.systemui.LayoutParamsHelper
import com.hpnightowl.systemui.R
import com.hpnightowl.systemui.manager.SystemBarManager
import com.hpnightowl.systemui.model.BarConfig
import com.hpnightowl.systemui.ui.bars.BottomBarContent
import com.hpnightowl.systemui.ui.bars.LeftBarContent
import com.hpnightowl.systemui.ui.bars.RightBarContent
import com.hpnightowl.systemui.ui.bars.TopBarContent
import com.hpnightowl.systemui.ui.core.ComposeWindowLifecycle
import com.hpnightowl.systemui.ui.overlays.ClimateOverlay
import com.hpnightowl.systemui.ui.overlays.NotificationOverlay
import com.hpnightowl.systemui.ui.overlays.QuickSettingsOverlay
import com.hpnightowl.systemui.ui.state.SystemUIStateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DisplayWindowController(
    private val serviceContext: Context,
    private val display: Display,
    private val manager: SystemBarManager
) {
    private val displayId = display.displayId
    private val TYPE_STATUS_BAR = 2000
    private val TYPE_NAVIGATION_BAR = 2019
    private val TYPE_NAVIGATION_BAR_PANEL = 2024

    private var topContext: Context? = null
    private var bottomContext: Context? = null
    private var leftContext: Context? = null
    private var rightContext: Context? = null
    private var overlayContext: Context? = null

    private var topWm: WindowManager? = null
    private var bottomWm: WindowManager? = null
    private var leftWm: WindowManager? = null
    private var rightWm: WindowManager? = null
    private var overlayWm: WindowManager? = null

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var topView: View? = null
    private var bottomView: View? = null
    private var leftView: View? = null
    private var rightView: View? = null
    private var overlayView: View? = null
    private var overlayParams: WindowManager.LayoutParams? = null
    private var lifecycle: ComposeWindowLifecycle? = null

    private var isOverlayAttached = false

    @RequiresApi(Build.VERSION_CODES.S)
    fun attachBars() {
        manager.barConfigs.value[displayId] ?: BarConfig()

        val topType = TYPE_STATUS_BAR
        val bottomType = TYPE_NAVIGATION_BAR
        val leftType = TYPE_NAVIGATION_BAR_PANEL
        val rightType = TYPE_NAVIGATION_BAR_PANEL

        val TYPE_NOTIFICATION_SHADE = 2040

        topContext = serviceContext.createWindowContext(display, topType, null)
        bottomContext = serviceContext.createWindowContext(display, bottomType, null)
        leftContext = serviceContext.createWindowContext(display, leftType, null)
        rightContext = serviceContext.createWindowContext(display, rightType, null)
        overlayContext = serviceContext.createWindowContext(display, TYPE_NOTIFICATION_SHADE, null)

        topWm = topContext!!.getSystemService(WindowManager::class.java)
        bottomWm = bottomContext!!.getSystemService(WindowManager::class.java)
        leftWm = leftContext!!.getSystemService(WindowManager::class.java)
        rightWm = rightContext!!.getSystemService(WindowManager::class.java)
        overlayWm = overlayContext!!.getSystemService(WindowManager::class.java)

        val configFlow = manager.barConfigs.map { it[displayId] ?: BarConfig() }

        topView = createComposeView(topContext!!) {
            val config by configFlow.collectAsState(initial = BarConfig())
            if (config.topEnabled) TopBarContent()
        }
        bottomView = createComposeView(bottomContext!!) {
            val config by configFlow.collectAsState(initial = BarConfig())
            if (config.bottomEnabled) BottomBarContent()
        }
        leftView = createComposeView(leftContext!!) {
            val config by configFlow.collectAsState(initial = BarConfig())
            if (config.leftEnabled) LeftBarContent()
        }
        rightView = createComposeView(rightContext!!) {
            val config by configFlow.collectAsState(initial = BarConfig())
            if (config.rightEnabled) RightBarContent()
        }

        // Overlay View contains all overlays
        overlayView = createComposeView(overlayContext!!) {
            val isClimate by SystemUIStateManager.isClimateOpen.collectAsState()
            val isQs by SystemUIStateManager.isQuickSettingsOpen.collectAsState()
            val isNotif by SystemUIStateManager.isNotificationOpen.collectAsState()

            Box(modifier = Modifier.fillMaxSize()) {
                ClimateOverlay(isVisible = isClimate)
                QuickSettingsOverlay(isVisible = isQs)
                NotificationOverlay(isVisible = isNotif)
            }
        }

        lifecycle = ComposeWindowLifecycle.attachTo(topView!!)
        ComposeWindowLifecycle.attachTo(bottomView!!)
        ComposeWindowLifecycle.attachTo(leftView!!)
        ComposeWindowLifecycle.attachTo(rightView!!)
        ComposeWindowLifecycle.attachTo(overlayView!!)

        val ITYPE_STATUS_BAR = InsetsState.ITYPE_STATUS_BAR
        val ITYPE_NAVIGATION_BAR = InsetsState.ITYPE_NAVIGATION_BAR
        val ITYPE_LEFT_GESTURES = InsetsState.ITYPE_LEFT_GESTURES
        val ITYPE_RIGHT_GESTURES = InsetsState.ITYPE_RIGHT_GESTURES
        val ITYPE_TOP_GESTURES = InsetsState.ITYPE_TOP_GESTURES
        val ITYPE_BOTTOM_GESTURES = InsetsState.ITYPE_BOTTOM_GESTURES
        val ITYPE_CLIMATE_BAR = InsetsState.ITYPE_CLIMATE_BAR
        val ITYPE_EXTRA_NAVIGATION_BAR = InsetsState.ITYPE_EXTRA_NAVIGATION_BAR

        val topHeightPx =
            topContext!!.resources.getDimensionPixelSize(R.dimen.car_top_system_bar_height)
        val bottomHeightPx =
            bottomContext!!.resources.getDimensionPixelSize(R.dimen.car_bottom_system_bar_height)
        val leftWidthPx =
            leftContext!!.resources.getDimensionPixelSize(R.dimen.car_left_system_bar_width)
        val rightWidthPx =
            rightContext!!.resources.getDimensionPixelSize(R.dimen.car_right_system_bar_width)

        topWm!!.addView(
            topView,
            createParams(
                Gravity.TOP,
                WindowManager.LayoutParams.MATCH_PARENT,
                topHeightPx,
                topType,
                ITYPE_STATUS_BAR,
                ITYPE_TOP_GESTURES
            )
        )
        bottomWm!!.addView(
            bottomView,
            createParams(
                Gravity.BOTTOM,
                WindowManager.LayoutParams.MATCH_PARENT,
                bottomHeightPx,
                bottomType,
                ITYPE_NAVIGATION_BAR,
                ITYPE_BOTTOM_GESTURES
            )
        )
        leftWm!!.addView(
            leftView,
            createParams(
                Gravity.LEFT,
                leftWidthPx,
                WindowManager.LayoutParams.MATCH_PARENT,
                leftType,
                ITYPE_CLIMATE_BAR,
                ITYPE_LEFT_GESTURES
            )
        )
        rightWm!!.addView(
            rightView,
            createParams(
                Gravity.RIGHT,
                rightWidthPx,
                WindowManager.LayoutParams.MATCH_PARENT,
                rightType,
                ITYPE_EXTRA_NAVIGATION_BAR,
                ITYPE_RIGHT_GESTURES
            )
        )


        // Observe StateManager to add/remove overlay view dynamically
        overlayParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            TYPE_NOTIFICATION_SHADE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
            PixelFormat.TRANSLUCENT
        ).apply {
            blurBehindRadius = 40
        }

        coroutineScope.launch {
            SystemUIStateManager.isClimateOpen.collect { updateOverlayVisibility() }
        }
        coroutineScope.launch {
            SystemUIStateManager.isQuickSettingsOpen.collect { updateOverlayVisibility() }
        }
        coroutineScope.launch {
            SystemUIStateManager.isNotificationOpen.collect { updateOverlayVisibility() }
        }
    }

    private fun updateOverlayVisibility() {
        val anyOpen = SystemUIStateManager.isClimateOpen.value ||
                SystemUIStateManager.isQuickSettingsOpen.value ||
                SystemUIStateManager.isNotificationOpen.value

        if (anyOpen && !isOverlayAttached) {
            overlayWm?.addView(overlayView, overlayParams)
            isOverlayAttached = true
        } else if (!anyOpen && isOverlayAttached) {
            overlayWm?.removeView(overlayView)
            isOverlayAttached = false
        }
    }

    fun destroy() {
        coroutineScope.cancel()
        lifecycle?.destroy()
        topView?.let { topWm?.removeView(it) }
        bottomView?.let { bottomWm?.removeView(it) }
        leftView?.let { leftWm?.removeView(it) }
        rightView?.let { rightWm?.removeView(it) }
        if (isOverlayAttached) {
            overlayView?.let { overlayWm?.removeView(it) }
        }
    }

    private fun createComposeView(context: Context, content: @Composable () -> Unit): ComposeView {
        return ComposeView(context).apply { setContent(content) }
    }

    private fun createParams(
        gravity: Int,
        width: Int,
        height: Int,
        type: Int,
        insetType: Int,
        gestureInsetType: Int
    ): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            width, height, type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            this.gravity = gravity
            this.title = "SystemBar_${displayId}_$gravity"
            val w = if (width == WindowManager.LayoutParams.MATCH_PARENT) 0 else width
            val h = if (height == WindowManager.LayoutParams.MATCH_PARENT) 0 else height

            var left = 0
            var top = 0
            var right = 0
            var bottom = 0

            when (gravity) {
                Gravity.LEFT -> left = w
                Gravity.RIGHT -> right = w
                Gravity.TOP -> top = h
                Gravity.BOTTOM -> bottom = h
            }
            LayoutParamsHelper.setInsets(
                this,
                insetType,
                left,
                top,
                right,
                bottom,
                gestureInsetType
            )
        }
    }
}
