package com.example.idleslayerbot

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView

class IdleSlayerBotService : AccessibilityService() {

    var isAutoclickerActive = false
    var isFullAfkMode = false

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null

    companion object {
        var instance: IdleSlayerBotService? = null
            private set
        fun isInstanceRunning(): Boolean = instance != null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d("IdleSlayerBot", "Service connected, showing panel...")
        showFloatingPanel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "ACTION_SHOW_PANEL") {
            Log.d("IdleSlayerBot", "Received ACTION_SHOW_PANEL intent")
            showFloatingPanel()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun showFloatingPanel() {
        if (floatingView != null) {
            Log.d("IdleSlayerBot", "Panel already exists")
            return
        }

        try {
            val layoutParamsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            }

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutParamsType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = 100
                y = 200
            }

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setBackgroundColor(Color.parseColor("#E6000000"))
                setPadding(32, 32, 32, 32)
            }

            val title = TextView(this).apply {
                text = "Idle Slayer Bot"
                setTextColor(Color.WHITE)
                textSize = 14f
                setPadding(0, 0, 0, 16)
            }

            val autoclickerSwitch = Switch(this).apply {
                text = "Автокликер"
                setTextColor(Color.WHITE)
                setOnCheckedChangeListener { _, isChecked ->
                    isAutoclickerActive = isChecked
                }
            }

            val afkSwitch = Switch(this).apply {
                text = "Full AFK Режим"
                setTextColor(Color.WHITE)
                setOnCheckedChangeListener { _, isChecked ->
                    isFullAfkMode = isChecked
                }
            }

            layout.addView(title)
            layout.addView(autoclickerSwitch)
            layout.addView(afkSwitch)

            floatingView = layout
            windowManager?.addView(floatingView, params)
            Log.d("IdleSlayerBot", "Panel added successfully!")

        } catch (e: Exception) {
            Log.e("IdleSlayerBot", "Failed to show panel: ${e.message}", e)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        removePanel()
        instance = null
    }

    private fun removePanel() {
        floatingView?.let {
            try {
                windowManager?.removeView(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            floatingView = null
        }
    }
}
