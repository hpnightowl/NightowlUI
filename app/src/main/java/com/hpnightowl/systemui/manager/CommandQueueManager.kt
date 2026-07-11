package com.hpnightowl.systemui.manager

import android.app.ITransientNotificationCallback
import android.content.ComponentName
import android.graphics.drawable.Icon
import android.hardware.biometrics.IBiometricContextListener
import android.hardware.biometrics.IBiometricSysuiReceiver
import android.hardware.biometrics.PromptInfo
import android.hardware.fingerprint.IUdfpsHbmListener
import android.media.INearbyMediaDevicesProvider
import android.media.MediaRoute2Info
import android.os.Bundle
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.os.ServiceManager
import android.util.Log
import android.view.InsetsVisibilities
import com.android.internal.statusbar.IAddTileResultCallback
import com.android.internal.statusbar.IStatusBar
import com.android.internal.statusbar.IStatusBarService
import com.android.internal.statusbar.IUndoMediaTransferCallback
import com.android.internal.statusbar.StatusBarIcon

class CommandQueueManager(private val systemBarManager: SystemBarManager) : IStatusBar.Stub() {

    fun register() {
        val barService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"))
        barService?.registerStatusBar(this)
        Log.d("CommandQueueManager", "Registered with IStatusBarService")
    }


    override fun setIcon(p0: String?, p1: StatusBarIcon?): Unit {
        Log.d("CommandQueueManager", "Invoked: setIcon")
    }

    override fun removeIcon(p0: String?): Unit {
        Log.d("CommandQueueManager", "Invoked: removeIcon")
    }

    override fun disable(displayId: Int, state1: Int, state2: Int): Unit {
        val immersive = state1 != 0
        Log.d(
            "CommandQueueManager", "disable called on displayId=$displayId state1=$state1"
        )

        systemBarManager.updateConfig(displayId) { config ->
            config.copy(
                topEnabled = !immersive,
                bottomEnabled = !immersive,
                leftEnabled = !immersive,
                rightEnabled = !immersive
            )
        }
    }

    override fun animateExpandNotificationsPanel(): Unit {
        Log.d("CommandQueueManager", "Invoked: animateExpandNotificationsPanel")
    }

    override fun animateExpandSettingsPanel(p0: String?): Unit {
        Log.d("CommandQueueManager", "Invoked: animateExpandSettingsPanel")
    }

    override fun animateCollapsePanels(): Unit {
        Log.d("CommandQueueManager", "Invoked: animateCollapsePanels")
    }

    override fun togglePanel(): Unit {
        Log.d("CommandQueueManager", "Invoked: togglePanel")
    }

    override fun showWirelessChargingAnimation(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: showWirelessChargingAnimation")
    }

    override fun setImeWindowStatus(p0: Int, p1: IBinder?, p2: Int, p3: Int, p4: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: setImeWindowStatus")
    }

    override fun setWindowState(p0: Int, p1: Int, p2: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: setWindowState")
    }

    override fun showRecentApps(p0: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: showRecentApps")
    }

    override fun hideRecentApps(p0: Boolean, p1: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: hideRecentApps")
    }

    override fun toggleRecentApps(): Unit {
        Log.d("CommandQueueManager", "Invoked: toggleRecentApps")
    }

    override fun toggleSplitScreen(): Unit {
        Log.d("CommandQueueManager", "Invoked: toggleSplitScreen")
    }

    override fun preloadRecentApps(): Unit {
        Log.d("CommandQueueManager", "Invoked: preloadRecentApps")
    }

    override fun cancelPreloadRecentApps(): Unit {
        Log.d("CommandQueueManager", "Invoked: cancelPreloadRecentApps")
    }

    override fun showScreenPinningRequest(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: showScreenPinningRequest")
    }

    override fun dismissKeyboardShortcutsMenu(): Unit {
        Log.d("CommandQueueManager", "Invoked: dismissKeyboardShortcutsMenu")
    }

    override fun toggleKeyboardShortcutsMenu(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: toggleKeyboardShortcutsMenu")
    }

    override fun appTransitionPending(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: appTransitionPending")
    }

    override fun appTransitionCancelled(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: appTransitionCancelled")
    }

    override fun appTransitionStarting(p0: Int, p1: Long, p2: Long): Unit {
        Log.d("CommandQueueManager", "Invoked: appTransitionStarting")
    }

    override fun appTransitionFinished(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: appTransitionFinished")
    }

    override fun showAssistDisclosure(): Unit {
        Log.d("CommandQueueManager", "Invoked: showAssistDisclosure")
    }

    override fun startAssist(p0: Bundle?): Unit {
        Log.d("CommandQueueManager", "Invoked: startAssist")
    }

    override fun onCameraLaunchGestureDetected(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: onCameraLaunchGestureDetected")
    }

    override fun onEmergencyActionLaunchGestureDetected(): Unit {
        Log.d("CommandQueueManager", "Invoked: onEmergencyActionLaunchGestureDetected")
    }

    override fun showPictureInPictureMenu() {
        Log.d("CommandQueueManager", "Invoked: showPictureInPictureMenu")
    }

    override fun showGlobalActionsMenu(): Unit {
        Log.d("CommandQueueManager", "Invoked: showGlobalActionsMenu")
    }

    override fun onProposedRotationChanged(p0: Int, p1: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: onProposedRotationChanged")
    }

    override fun setTopAppHidesStatusBar(p0: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: setTopAppHidesStatusBar")
    }

    override fun addQsTile(p0: ComponentName?): Unit {
        Log.d("CommandQueueManager", "Invoked: addQsTile")
    }

    override fun remQsTile(p0: ComponentName?): Unit {
        Log.d("CommandQueueManager", "Invoked: remQsTile")
    }

    override fun clickQsTile(p0: ComponentName?): Unit {
        Log.d("CommandQueueManager", "Invoked: clickQsTile")
    }

    override fun handleSystemKey(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: handleSystemKey")
    }

    override fun showPinningEnterExitToast(p0: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: showPinningEnterExitToast")
    }

    override fun showPinningEscapeToast(): Unit {
        Log.d("CommandQueueManager", "Invoked: showPinningEscapeToast")
    }

    override fun showShutdownUi(p0: Boolean, p1: String?): Unit {
        Log.d("CommandQueueManager", "Invoked: showShutdownUi")
    }

    override fun showAuthenticationDialog(
        p0: PromptInfo?,
        p1: IBiometricSysuiReceiver?,
        p2: IntArray?,
        p3: Boolean,
        p4: Boolean,
        p5: Int,
        p6: Long,
        p7: String?,
        p8: Long,
        p9: Int
    ): Unit {
        Log.d("CommandQueueManager", "Invoked: showAuthenticationDialog")
    }

    override fun onBiometricAuthenticated(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: onBiometricAuthenticated")
    }

    override fun onBiometricHelp(p0: Int, p1: String?): Unit {
        Log.d("CommandQueueManager", "Invoked: onBiometricHelp")
    }

    override fun onBiometricError(p0: Int, p1: Int, p2: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: onBiometricError")
    }

    override fun hideAuthenticationDialog(p0: Long): Unit {
        Log.d("CommandQueueManager", "Invoked: hideAuthenticationDialog")
    }

    override fun setBiometicContextListener(p0: IBiometricContextListener?): Unit {
        Log.d("CommandQueueManager", "Invoked: setBiometicContextListener")
    }

    override fun setUdfpsHbmListener(p0: IUdfpsHbmListener?): Unit {
        Log.d("CommandQueueManager", "Invoked: setUdfpsHbmListener")
    }

    override fun onDisplayReady(p0: Int): Unit {
        Log.d("CommandQueueManager", "Invoked: onDisplayReady")
    }

    override fun onRecentsAnimationStateChanged(p0: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: onRecentsAnimationStateChanged")
    }

    override fun onSystemBarAttributesChanged(
        p0: Int,
        p1: Int,
        p2: Array<com.android.internal.view.AppearanceRegion>?,
        p3: Boolean,
        p4: Int,
        p5: InsetsVisibilities?,
        p6: String?,
        p7: Array<com.android.internal.statusbar.LetterboxDetails>?
    ): Unit {
        if (p5 != null) {
            try {
                val getVis = p5.javaClass.getMethod("getVisibility", Int::class.javaPrimitiveType)
                val statusVisible = getVis.invoke(p5, 0) as Boolean
                val navVisible = getVis.invoke(p5, 1) as Boolean

                Log.d(
                    "CommandQueueManager",
                    "onSystemBarAttributesChanged displayId=$p0 statusVisible=$statusVisible navVisible=$navVisible"
                )

                systemBarManager.updateConfig(p0) { config ->
                    config.copy(
                        topEnabled = statusVisible,
                        bottomEnabled = navVisible,
                        leftEnabled = navVisible,
                        rightEnabled = navVisible
                    )
                }
            } catch (e: Exception) {
                Log.e("CommandQueueManager", "Failed to parse InsetsVisibilities", e)
            }
        }
    }

    override fun showTransient(p0: Int, p1: IntArray?, p2: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: showTransient")
    }

    override fun abortTransient(p0: Int, p1: IntArray?): Unit {
        Log.d("CommandQueueManager", "Invoked: abortTransient")
    }

    override fun showInattentiveSleepWarning(): Unit {
        Log.d("CommandQueueManager", "Invoked: showInattentiveSleepWarning")
    }

    override fun dismissInattentiveSleepWarning(p0: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: dismissInattentiveSleepWarning")
    }

    override fun showToast(
        p0: Int,
        p1: String?,
        p2: IBinder?,
        p3: CharSequence?,
        p4: IBinder?,
        p5: Int,
        p6: ITransientNotificationCallback?,
        p7: Int
    ): Unit {
        Log.d("CommandQueueManager", "Invoked: showToast")
    }

    override fun hideToast(p0: String?, p1: IBinder?): Unit {
        Log.d("CommandQueueManager", "Invoked: hideToast")
    }

    override fun startTracing(): Unit {
        Log.d("CommandQueueManager", "Invoked: startTracing")
    }

    override fun stopTracing(): Unit {
        Log.d("CommandQueueManager", "Invoked: stopTracing")
    }

    override fun suppressAmbientDisplay(p0: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: suppressAmbientDisplay")
    }

    override fun requestWindowMagnificationConnection(p0: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: requestWindowMagnificationConnection")
    }

    override fun passThroughShellCommand(p0: Array<String>?, p1: ParcelFileDescriptor?): Unit {
        Log.d("CommandQueueManager", "Invoked: passThroughShellCommand")
    }

    override fun setNavigationBarLumaSamplingEnabled(p0: Int, p1: Boolean): Unit {
        Log.d("CommandQueueManager", "Invoked: setNavigationBarLumaSamplingEnabled")
    }

    override fun runGcForTest(): Unit {
        Log.d("CommandQueueManager", "Invoked: runGcForTest")
    }

    override fun requestTileServiceListeningState(p0: ComponentName?): Unit {
        Log.d("CommandQueueManager", "Invoked: requestTileServiceListeningState")
    }

    override fun requestAddTile(
        p0: ComponentName?,
        p1: CharSequence?,
        p2: CharSequence?,
        p3: Icon?,
        p4: IAddTileResultCallback?
    ): Unit {
        Log.d("CommandQueueManager", "Invoked: requestAddTile")
    }

    override fun cancelRequestAddTile(p0: String?): Unit {
        Log.d("CommandQueueManager", "Invoked: cancelRequestAddTile")
    }

    override fun updateMediaTapToTransferSenderDisplay(
        p0: Int, p1: MediaRoute2Info?, p2: IUndoMediaTransferCallback?
    ): Unit {
        Log.d("CommandQueueManager", "Invoked: updateMediaTapToTransferSenderDisplay")
    }

    override fun updateMediaTapToTransferReceiverDisplay(
        p0: Int, p1: MediaRoute2Info?, p2: Icon?, p3: CharSequence?
    ): Unit {
        Log.d(
            "CommandQueueManager", "Invoked: updateMediaTapToTransferReceiverDisplay"
        )
    }

    override fun registerNearbyMediaDevicesProvider(p0: INearbyMediaDevicesProvider?): Unit {
        Log.d("CommandQueueManager", "Invoked: registerNearbyMediaDevicesProvider")
    }

    override fun unregisterNearbyMediaDevicesProvider(p0: INearbyMediaDevicesProvider?): Unit {
        Log.d("CommandQueueManager", "Invoked: unregisterNearbyMediaDevicesProvider")
    }
}
