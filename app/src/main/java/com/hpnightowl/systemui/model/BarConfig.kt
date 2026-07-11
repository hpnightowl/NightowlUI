package com.hpnightowl.systemui.model

data class BarConfig(
    val topEnabled: Boolean = true,
    val bottomEnabled: Boolean = true,
    val leftEnabled: Boolean = true,
    val rightEnabled: Boolean = true,
    val topZOrder: Int = 1,
    val bottomZOrder: Int = 2,
    val leftZOrder: Int = 9,
    val rightZOrder: Int = 10
)
