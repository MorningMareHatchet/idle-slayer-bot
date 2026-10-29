package com.example.idleslayerbot

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class IdleSlayerBotService : AccessibilityService() {

    var isAutoclickerActive = false
    var isAutoUpgradeActive = false
    var isChestHuntActive = false
    var isFullAfkMode = false

    private var floatingPanel: FloatingControlPanel? = null

    companion object {
        var instance: IdleSlayerBotService? = null
            private set
        fun isInstanceRunning(): Boolean = instance != null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        // Сразу пробуем показать панель при подключении службы
        showPanel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "ACTION_SHOW_PANEL") {
            showPanel()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun showPanel() {
        try {
            if (floatingPanel == null) {
                floatingPanel = FloatingControlPanel(this)
            }
            floatingPanel?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        floatingPanel?.remove()
        floatingPanel = null
        instance = null
    }
}
