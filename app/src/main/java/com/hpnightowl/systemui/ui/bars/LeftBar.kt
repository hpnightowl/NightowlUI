package com.hpnightowl.systemui.ui.bars

import android.content.Intent
import android.os.UserHandle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hpnightowl.systemui.ui.state.SystemUIStateManager
import com.hpnightowl.systemui.ui.theme.ColorSurfaceDark
import com.hpnightowl.systemui.ui.theme.ColorTextPrimary

@Composable
fun LeftBarContent() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorSurfaceDark),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(64.dp)
        ) {
            IconButton(onClick = { /* Car info */ }) {
                Icon(
                    Icons.Filled.DirectionsCar,
                    contentDescription = "Car",
                    tint = ColorTextPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(onClick = { SystemUIStateManager.toggleClimate() }) {
                Icon(
                    Icons.Filled.Air,
                    contentDescription = "Fan",
                    tint = ColorTextPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(onClick = {
                try {
                    // I will modify this with explicit intent later
                    val intent = Intent().apply {
                        component = android.content.ComponentName(
                            "com.android.car.settings",
                            "com.android.car.settings.Settings_Launcher_Homepage"
                        )
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    val amClass = Class.forName("android.app.ActivityManager")
                    val currentUser = amClass.getMethod("getCurrentUser").invoke(null) as Int
                    val userHandleClass = Class.forName("android.os.UserHandle")
                    val userHandle = userHandleClass.getMethod("of", Int::class.javaPrimitiveType)
                        .invoke(null, currentUser) as UserHandle
                    val method = context.javaClass.getMethod(
                        "startActivityAsUser",
                        Intent::class.java,
                        UserHandle::class.java
                    )
                    method.invoke(context, intent, userHandle)
                } catch (e: Exception) {
                    android.util.Log.e("LeftBar", "Failed to launch settings", e)
                }
            }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = ColorTextPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}
