package com.hpnightowl.systemui.ui.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.DoNotDisturbOn
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
import com.hpnightowl.systemui.ui.state.SystemUIStateManager
import com.hpnightowl.systemui.ui.theme.ColorAccent
import com.hpnightowl.systemui.ui.theme.ColorSurfaceDark
import com.hpnightowl.systemui.ui.theme.ColorTextPrimary
import com.hpnightowl.systemui.ui.theme.ColorTextSecondary
import com.hpnightowl.systemui.ui.theme.glassmorphism

@Composable
fun QuickSettingsOverlay(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorSurfaceDark.copy(alpha = 0.4f))
                .clickable { SystemUIStateManager.closeAll() },
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.6f)
                    .padding(top = 80.dp) // Offset below TopBar
                    .clickable { /* Consume click to avoid closing */ }
                    .glassmorphism(cornerRadius = 32)
                    .padding(32.dp)
            ) {
                Text(
                    "Quick Settings",
                    color = ColorTextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(ColorAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Wifi,
                                contentDescription = "WiFi",
                                tint = ColorTextPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(
                            "Wi-Fi",
                            color = ColorTextPrimary,
                            modifier = Modifier.padding(top = 12.dp),
                            fontSize = 18.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(ColorAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Bluetooth,
                                contentDescription = "Bluetooth",
                                tint = ColorTextPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(
                            "Bluetooth",
                            color = ColorTextPrimary,
                            modifier = Modifier.padding(top = 12.dp),
                            fontSize = 18.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(ColorSurfaceDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.DoNotDisturbOn,
                                contentDescription = "DND",
                                tint = ColorTextSecondary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(
                            "DND",
                            color = ColorTextSecondary,
                            modifier = Modifier.padding(top = 12.dp),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}
