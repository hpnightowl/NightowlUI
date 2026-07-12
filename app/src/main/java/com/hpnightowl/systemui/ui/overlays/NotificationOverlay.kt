package com.hpnightowl.systemui.ui.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Navigation
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
fun NotificationOverlay(isVisible: Boolean) {
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
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight(0.8f)
                    .padding(top = 80.dp) // Offset below TopBar
                    .clickable { /* Consume click to avoid closing */ }
                    .glassmorphism(cornerRadius = 32)
                    .padding(32.dp)
            ) {
                Text(
                    "Notifications",
                    color = ColorTextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(32.dp))
                // Notification Item 1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorSurfaceDark, RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Navigation,
                        contentDescription = "Nav",
                        tint = ColorAccent,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Column {
                        Text(
                            "Navigation",
                            color = ColorTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "Turn left in 500ft on Main St",
                            color = ColorTextSecondary,
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Notification Item 2
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorSurfaceDark, RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Message,
                        contentDescription = "Message",
                        tint = ColorAccent,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Column {
                        Text(
                            "John Doe",
                            color = ColorTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "Are we still on for the meeting?",
                            color = ColorTextSecondary,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
