package com.example.idleslayerbot

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent

class IdleSlayerBotService : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var clickX = 500f
    private var clickY = 1200f
    private var clickInterval: Long = 100 // интервал между кликами в миллисекундах

    private val clickRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                clickAt(clickX, clickY)
                handler.postDelayed(this, clickInterval)
            }
        }
    }

    companion object {
        private var instance: IdleSlayerBotService? = null
        fun getInstance(): IdleSlayerBotService? = instance
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
            "ACTION_SINGLE_CLICK" -> {
                val x = intent.getFloatExtra("x", 500f)
                val y = intent.getFloatExtra("y", 1200f)
                clickAt(x, y)
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

    fun clickAt(x: Float, y: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val path = Path().apply {
                moveTo(x, y)
            }
            val strokeDescription = GestureDescription.StrokeDescription(path, 0, 50)
            val gestureBuilder = GestureDescription.Builder().addStroke(strokeDescription)
            dispatchGesture(gestureBuilder.build(), object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)
                }
                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)
                }
            }, null)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {
        stopAutoClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoClick()
        instance = null
    }
}
