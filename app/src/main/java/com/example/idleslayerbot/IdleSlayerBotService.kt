package com.example.idleslayerbot

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class IdleSlayerBotService : AccessibilityService() {

    // Переменные состояния для панели управления
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
        
        // Автоматически создаем и показываем плавающую панель при включении службы
        showFloatingPanel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "ACTION_SHOW_PANEL") {
            showFloatingPanel()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showFloatingPanel() {
        if (floatingPanel == null) {
            floatingPanel = FloatingControlPanel(this)
        }
        floatingPanel?.show()
    }

    fun executeChestHuntStrategy() {
        // Логика расчета сундуков
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Основной цикл обработки событий игры
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        floatingPanel?.remove()
        floatingPanel = null
        instance = null
    }
}
