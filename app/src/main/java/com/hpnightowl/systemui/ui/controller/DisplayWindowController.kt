package com.hpnightowl.systemui.ui.controller


import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Display
import android.view.Gravity
import android.view.InsetsState
import android.view.View
import android.view.WindowManager
import android.widget.TextClock
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.hpnightowl.systemui.LayoutParamsHelper
import com.hpnightowl.systemui.manager.SystemBarManager
import com.hpnightowl.systemui.model.BarConfig
import com.hpnightowl.systemui.ui.core.ComposeWindowLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.map

class DisplayWindowController(
    private val serviceContext: Context,
    private val display: Display,
    private val manager: SystemBarManager
) {
    private val displayId = display.displayId

    // Window types (using raw values to avoid hidden API compilation errors)
    private val TYPE_STATUS_BAR = 2000
    private val TYPE_NAVIGATION_BAR = 2019
    private val TYPE_NAVIGATION_BAR_PANEL = 2024
    private val TYPE_STATUS_BAR_ADDITIONAL = 2036

    private var topContext: Context? = null
    private var bottomContext: Context? = null
    private var leftContext: Context? = null
    private var rightContext: Context? = null

    private var topWm: WindowManager? = null
    private var bottomWm: WindowManager? = null
    private var leftWm: WindowManager? = null
    private var rightWm: WindowManager? = null

    private fun mapZOrderToBarType(zOrder: Int): Int {
        return if (zOrder >= 10) TYPE_NAVIGATION_BAR_PANEL else TYPE_STATUS_BAR_ADDITIONAL
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var topView: View? = null
    private var bottomView: View? = null
    private var leftView: View? = null
    private var rightView: View? = null
    private var lifecycle: ComposeWindowLifecycle? = null

    @RequiresApi(Build.VERSION_CODES.S)
    fun attachBars() {
        val initialConfig = manager.barConfigs.value[displayId] ?: BarConfig()

        val topType = TYPE_STATUS_BAR
        val bottomType = TYPE_NAVIGATION_BAR
        val leftType = mapZOrderToBarType(initialConfig.leftZOrder)
        val rightType = mapZOrderToBarType(initialConfig.rightZOrder)

        topContext = serviceContext.createWindowContext(display, topType, null)
        bottomContext = serviceContext.createWindowContext(display, bottomType, null)
        leftContext = serviceContext.createWindowContext(display, leftType, null)
        rightContext = serviceContext.createWindowContext(display, rightType, null)

        topWm = topContext!!.getSystemService(WindowManager::class.java)
        bottomWm = bottomContext!!.getSystemService(WindowManager::class.java)
        leftWm = leftContext!!.getSystemService(WindowManager::class.java)
        rightWm = rightContext!!.getSystemService(WindowManager::class.java)

        val configFlow = manager.barConfigs.map { it[displayId] ?: BarConfig() }

        topView = createComposeView(topContext!!) {
            val config by configFlow.collectAsState(initial = BarConfig())
            if (config.topEnabled) {
                PlaceholderBar("TOP BAR (Display $displayId)", Color.Red)
            }
        }

        bottomView = createComposeView(bottomContext!!) {
            val config by configFlow.collectAsState(initial = BarConfig())
            if (config.bottomEnabled) {
                PlaceholderBar("BOTTOM BAR (Display $displayId)", Color.Blue)
            }
        }

        leftView = createComposeView(leftContext!!) {
            val config by configFlow.collectAsState(initial = BarConfig())
            if (config.leftEnabled) {
                PlaceholderBar("LEFT BAR", Color.Green)
            }
        }

        rightView = createComposeView(rightContext!!) {
            val config by configFlow.collectAsState(initial = BarConfig())
            if (config.rightEnabled) {
                PlaceholderBar("RIGHT BAR", Color.Magenta)
            }
        }

        // We only need one lifecycle for the whole display ideally, but here we attach to all views
        // Actually, let's create one ComposeWindowLifecycle and attach it to all views.
        lifecycle = ComposeWindowLifecycle.attachTo(topView!!)
        ComposeWindowLifecycle.attachTo(bottomView!!)
        ComposeWindowLifecycle.attachTo(leftView!!)
        ComposeWindowLifecycle.attachTo(rightView!!)

        // Insets Types
        val ITYPE_STATUS_BAR = InsetsState.ITYPE_STATUS_BAR
        val ITYPE_NAVIGATION_BAR = InsetsState.ITYPE_NAVIGATION_BAR
        val ITYPE_LEFT_GESTURES = InsetsState.ITYPE_LEFT_GESTURES
        val ITYPE_RIGHT_GESTURES = InsetsState.ITYPE_RIGHT_GESTURES
        val ITYPE_TOP_GESTURES = InsetsState.ITYPE_TOP_GESTURES
        val ITYPE_BOTTOM_GESTURES = InsetsState.ITYPE_BOTTOM_GESTURES
        val ITYPE_CLIMATE_BAR = InsetsState.ITYPE_CLIMATE_BAR
        val ITYPE_EXTRA_NAVIGATION_BAR = InsetsState.ITYPE_EXTRA_NAVIGATION_BAR

        topWm!!.addView(
            topView,
            createParams(
                Gravity.TOP,
                WindowManager.LayoutParams.MATCH_PARENT,
                100,
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
                100,
                bottomType,
                ITYPE_NAVIGATION_BAR,
                ITYPE_BOTTOM_GESTURES
            )
        )
        leftWm!!.addView(
            leftView,
            createParams(
                Gravity.LEFT,
                100,
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
                100,
                WindowManager.LayoutParams.MATCH_PARENT,
                rightType,
                ITYPE_EXTRA_NAVIGATION_BAR,
                ITYPE_RIGHT_GESTURES
            )
        )
    }

    fun destroy() {
        coroutineScope.cancel()
        lifecycle?.destroy()
        topView?.let { topWm?.removeView(it) }
        bottomView?.let { bottomWm?.removeView(it) }
        leftView?.let { leftWm?.removeView(it) }
        rightView?.let { rightWm?.removeView(it) }
    }

    private fun createComposeView(context: Context, content: @Composable () -> Unit): ComposeView {
        return ComposeView(context).apply {
            setContent(content)
        }
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
            width,
            height,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            this.gravity = gravity
            this.title = "SystemBar_${displayId}_$gravity"

            // Use precompiled Java Stub to assign insets cleanly without reflection
            var left = 0
            var top = 0
            var right = 0
            var bottom = 0

            // For MATCH_PARENT, we don't use it as the inset size.
            val w = if (width == WindowManager.LayoutParams.MATCH_PARENT) 0 else width
            val h = if (height == WindowManager.LayoutParams.MATCH_PARENT) 0 else height

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


@Composable
fun TopBarContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222)),
        contentAlignment = Alignment.CenterEnd
    ) {
        AndroidView(
            factory = { ctx ->
                TextClock(ctx).apply {
                    format12Hour = "hh:mm a"
                    format24Hour = "HH:mm"
                    textSize = 24f
                    setTextColor(android.graphics.Color.WHITE)
                }
            },
            modifier = Modifier.padding(end = 24.dp)
        )
    }
}

@Composable
fun BottomBarContent(onAppDrawerClick: () -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222)),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                try {
                    val intent = Intent("com.android.car.carlauncher.ACTION_APP_GRID").apply {
                        setPackage("com.android.car.carlauncher")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    val userHandleClass = Class.forName("android.os.UserHandle")
                    val current =
                        userHandleClass.getField("CURRENT").get(null) as android.os.UserHandle
                    val method = context.javaClass.getMethod(
                        "startActivityAsUser",
                        Intent::class.java,
                        userHandleClass
                    )
                    method.invoke(context, intent, current)
                } catch (e: Exception) {
                    android.util.Log.e("SystemBar", "Failed to launch app drawer", e)
                }
            }) {
                Text("Apps", color = Color.White)
            }
        }
    }
}

@Composable
fun LeftBarContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222)),
        contentAlignment = Alignment.Center
    ) {
        Text("HVAC", color = Color.White)
    }
}

@Composable
fun RightBarContent(onVolumeClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onVolumeClick) {
            Text("Vol", color = Color.White)
        }
    }
}

@Composable
fun VolumeDrawerOverlay(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x88000000))
            .clickable { onClose() },
        contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.3f)
                .background(Color(0xFF333333))
                .clickable { /* Consume click */ }) {
            Text(
                "Volume Controls UI Placeholder",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun AppDrawerOverlay(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xEE000000))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .background(Color(0xFF333333))
                .clickable { /* Consume click */ }) {
            Text(
                "App Drawer Grid Placeholder",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun PlaceholderBar(title: String, color: Color) {
    var showAppDrawer by remember { mutableStateOf(false) }
    var showVolumeDrawer by remember { mutableStateOf(false) }

    when {
        title.contains("TOP") -> TopBarContent()
        title.contains("BOTTOM") -> BottomBarContent(onAppDrawerClick = {
            showAppDrawer = !showAppDrawer
        })

        title.contains("LEFT") -> LeftBarContent()
        title.contains("RIGHT") -> RightBarContent(onVolumeClick = {
            showVolumeDrawer = !showVolumeDrawer
        })

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(text = title, color = Color.White)
            }
        }
    }

    if (title.contains("BOTTOM") && showAppDrawer) {
        AppDrawerOverlay(onClose = { showAppDrawer = false })
    }

    if (title.contains("RIGHT") && showVolumeDrawer) {
        VolumeDrawerOverlay(onClose = { showVolumeDrawer = false })
    }
}
