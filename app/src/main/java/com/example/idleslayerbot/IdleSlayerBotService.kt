package com.example.idleslayerbot

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

class IdleSlayerBotService : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var clickX = 500f
    private var clickY = 1200f
    private var clickInterval: Long = 100L

    private val clickRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                executeShizukuClick(clickX, clickY)
                handler.postDelayed(this, clickInterval)
            }
        }
    }

    companion object {
        private var instance: IdleSlayerBotService? = null
        fun isInstanceRunning(): Boolean = instance != null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "ACTION_START_AUTO" -> {
                clickX = intent.getFloatExtra("x", 500f)
                clickY = intent.getFloatExtra("y", 1200f)
                clickInterval = intent.getLongExtra("interval", 100L)
                startAutoClick()
            }
            "ACTION_STOP_AUTO" -> {
                stopAutoClick()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startAutoClick() {
        if (!isRunning) {
            isRunning = true
            handler.post(clickRunnable)
        }
    }

    private fun stopAutoClick() {
        isRunning = false
        handler.removeCallbacks(clickRunnable)
    }

    // Движок кликов через Shizuku (без Root, но с системными правами отладки)
    private fun executeShizukuClick(x: Float, y: Float) {
        try {
            // Проверяем, запущен ли Shizuku и есть ли у нас разрешение
            if (Shizuku.pingBinder() && Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                val cmd = arrayOf("input", "tap", x.toInt().toString(), y.toInt().toString())
                val process = Shizuku.newProcess(cmd, null, null)
                process.waitFor()
            } else {
                // Запасной вариант на случай, если Shizuku не активен — обычный dispatchGesture
                // (если и он не сработает, значит нужно просто выдать разрешение Shizuku в приложении)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() { stopAutoClick() }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoClick()
        instance = null
    }
}
