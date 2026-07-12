package com.hpnightowl.systemui.ui.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun ClimateOverlay(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorSurfaceDark.copy(alpha = 0.4f))
                .clickable { SystemUIStateManager.closeAll() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .clickable { /* Consume click to avoid closing */ }
                    .glassmorphism(cornerRadius = 32)
                    .padding(32.dp)
            ) {
                Text(
                    "Climate Controls",
                    color = ColorTextPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(48.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Driver
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Up",
                            tint = ColorAccent,
                            modifier = Modifier.size(64.dp)
                        )
                        Text("72°", color = ColorTextPrimary, fontSize = 48.sp)
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Down",
                            tint = ColorAccent,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    // Fan
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.Air,
                            contentDescription = "Fan",
                            tint = ColorTextPrimary,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Speed 3", color = ColorTextSecondary, fontSize = 24.sp)
                    }
                    // Passenger
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Up",
                            tint = ColorAccent,
                            modifier = Modifier.size(64.dp)
                        )
                        Text("72°", color = ColorTextPrimary, fontSize = 48.sp)
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Down",
                            tint = ColorAccent,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }
        }
    }
}
