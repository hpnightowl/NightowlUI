package com.hpnightowl.systemui.ui.bars

import android.widget.TextClock
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.hpnightowl.systemui.ui.state.SystemUIStateManager
import com.hpnightowl.systemui.ui.theme.ColorAccent
import com.hpnightowl.systemui.ui.theme.ColorSurfaceDark
import com.hpnightowl.systemui.ui.theme.ColorTextPrimary

@Composable
fun TopBarContent() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorSurfaceDark)
            .padding(horizontal = 116.dp), // 100dp (side bar) + 16dp padding
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Profile
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { /* Handle Profile */ }
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ColorAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = "Profile", tint = ColorTextPrimary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "Driver 1",
                color = ColorTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Right: Status Icons & Clock
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { SystemUIStateManager.toggleQuickSettings() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.NetworkCell,
                    contentDescription = "Cellular",
                    tint = ColorTextPrimary,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                Icon(
                    Icons.Filled.Wifi,
                    contentDescription = "WiFi",
                    tint = ColorTextPrimary,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                Icon(
                    Icons.Filled.Bluetooth,
                    contentDescription = "Bluetooth",
                    tint = ColorTextPrimary,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { SystemUIStateManager.toggleNotification() }
                    .padding(8.dp)
            ) {
                AndroidView(
                    factory = { ctx ->
                        TextClock(ctx).apply {
                            format12Hour = "h:mm a"
                            format24Hour = "HH:mm"
                            textSize = 28f
                            setTextColor(android.graphics.Color.WHITE)
                        }
                    }
                )
            }
        }
    }
}
