package com.hpnightowl.systemui.ui.bars

import android.content.Intent
import android.os.UserHandle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AirlineSeatReclineExtra
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hpnightowl.systemui.ui.state.SystemUIStateManager
import com.hpnightowl.systemui.ui.theme.ColorAccent
import com.hpnightowl.systemui.ui.theme.ColorSurfaceDark
import com.hpnightowl.systemui.ui.theme.ColorTextPrimary

@Composable
fun BottomBarContent() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorSurfaceDark)
            .padding(horizontal = 116.dp), // 100dp (side bar) + 16dp padding
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Driver HVAC
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { SystemUIStateManager.toggleClimate() }
                .padding(12.dp)
        ) {
            Text("72°", color = ColorTextPrimary, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                Icons.Filled.AcUnit,
                contentDescription = "AC",
                tint = ColorAccent,
                modifier = Modifier.size(32.dp)
            )
        }

        // Center: Navigation
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            IconButton(onClick = {
                try {
                    val intent = Intent().apply {
                        component = android.content.ComponentName(
                            "com.android.car.carlauncher",
                            "com.android.car.carlauncher.CarLauncher"
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
                    android.util.Log.e("BottomBar", "Failed to launch home", e)
                }
            }) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = ColorTextPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(onClick = {
                try {
                    val intent = Intent().apply {
                        component = android.content.ComponentName(
                            "com.android.car.carlauncher",
                            "com.android.car.carlauncher.AppGridActivity"
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
                    android.util.Log.e("BottomBar", "Failed to launch app drawer", e)
                    SystemUIStateManager.toggleAppDrawer()
                }
            }) {
                Icon(
                    Icons.Filled.Apps,
                    contentDescription = "Apps",
                    tint = ColorTextPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(onClick = { /* Maps - Ignored */ }) {
                Icon(
                    Icons.Filled.Map,
                    contentDescription = "Maps",
                    tint = ColorTextPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Right: Passenger HVAC
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { SystemUIStateManager.toggleClimate() }
                .padding(12.dp)
        ) {
            Icon(
                Icons.Filled.AirlineSeatReclineExtra,
                contentDescription = "Seat Heater",
                tint = ColorTextPrimary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("72°", color = ColorTextPrimary, fontSize = 28.sp)
        }
    }
}
