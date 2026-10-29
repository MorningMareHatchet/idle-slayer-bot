package com.example.idleslayerbot

accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.view.accessibility.AccessibilityEvent

class IdleSlayerBotService : AccessibilityService() {

    var isAutoclickerActive = false

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
        if (intent?.action == "ACTION_CLICK") {
            val x = intent.getFloatExtra("x", 500f)
            val y = intent.getFloatExtra("y", 1000f)
            clickAt(x, y)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // Метод имитации тапа по экрану (как в настоящих автокликерах)
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
    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
