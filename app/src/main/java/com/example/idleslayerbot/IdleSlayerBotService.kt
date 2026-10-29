package com.example.idleslayerbot

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class IdleSlayerBotService : AccessibilityService() {

    companion object {
        private var instance: IdleSlayerBotService? = null
        fun isInstanceRunning(): Boolean = instance != null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "ACTION_SHOW_PANEL") {
            // Здесь будет вызов показа панели, если требуется
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Обязательный метод для AccessibilityService
    }

    override fun onInterrupt() {
        // Обязательный метод для AccessibilityService
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
