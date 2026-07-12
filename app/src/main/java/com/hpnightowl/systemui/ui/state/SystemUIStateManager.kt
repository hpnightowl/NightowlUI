package com.hpnightowl.systemui.ui.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SystemUIStateManager {
    private val _isClimateOpen = MutableStateFlow(false)
    val isClimateOpen: StateFlow<Boolean> = _isClimateOpen

    private val _isQuickSettingsOpen = MutableStateFlow(false)
    val isQuickSettingsOpen: StateFlow<Boolean> = _isQuickSettingsOpen

    private val _isNotificationOpen = MutableStateFlow(false)
    val isNotificationOpen: StateFlow<Boolean> = _isNotificationOpen

    private val _isAppDrawerOpen = MutableStateFlow(false)

    private val _isVolumeDrawerOpen = MutableStateFlow(false)

    fun toggleClimate() {
        closeAllExcept(climate = true)
        _isClimateOpen.value = !_isClimateOpen.value
    }

    fun toggleQuickSettings() {
        closeAllExcept(qs = true)
        _isQuickSettingsOpen.value = !_isQuickSettingsOpen.value
    }

    fun toggleNotification() {
        closeAllExcept(notif = true)
        _isNotificationOpen.value = !_isNotificationOpen.value
    }

    fun toggleAppDrawer() {
        closeAllExcept(app = true)
        _isAppDrawerOpen.value = !_isAppDrawerOpen.value
    }

    fun closeAll() {
        _isClimateOpen.value = false
        _isQuickSettingsOpen.value = false
        _isNotificationOpen.value = false
        _isAppDrawerOpen.value = false
        _isVolumeDrawerOpen.value = false
    }

    private fun closeAllExcept(
        climate: Boolean = false,
        qs: Boolean = false,
        notif: Boolean = false,
        app: Boolean = false,
        vol: Boolean = false
    ) {
        if (!climate) _isClimateOpen.value = false
        if (!qs) _isQuickSettingsOpen.value = false
        if (!notif) _isNotificationOpen.value = false
        if (!app) _isAppDrawerOpen.value = false
        if (!vol) _isVolumeDrawerOpen.value = false
    }
}
