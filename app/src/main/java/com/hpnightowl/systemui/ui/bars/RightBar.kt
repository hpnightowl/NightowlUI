package com.hpnightowl.systemui.ui.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.hpnightowl.systemui.ui.theme.ColorAccent
import com.hpnightowl.systemui.ui.theme.ColorSurfaceDark
import com.hpnightowl.systemui.ui.theme.ColorTextPrimary
import kotlin.math.max
import kotlin.math.min

@Composable
fun RightBarContent() {
    var volume by remember { mutableFloatStateOf(0.6f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorSurfaceDark)
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            IconButton(onClick = { volume = min(1f, volume + 0.1f) }) {
                Icon(
                    Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Volume Up",
                    tint = ColorTextPrimary,
                    modifier = Modifier.size(56.dp)
                )
            }

            // Custom Thick Vertical Slider
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(400.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { change, dragAmount ->
                            change.consume()
                            val height = size.height
                            val delta = -dragAmount / height
                            volume = (volume + delta).coerceIn(0f, 1f)
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val height = size.height
                            val newValue = 1f - (offset.y / height)
                            volume = newValue.coerceIn(0f, 1f)
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(volume)
                        .background(ColorAccent)
                        .align(Alignment.BottomCenter)
                )
            }

            IconButton(onClick = { volume = max(0f, volume - 0.1f) }) {
                Icon(
                    Icons.AutoMirrored.Filled.VolumeDown,
                    contentDescription = "Volume Down",
                    tint = ColorTextPrimary,
                    modifier = Modifier.size(56.dp)
                )
            }
        }
    }
}
