package com.example.idleslayerbot

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Switch

class FloatingControlPanel(private val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var floatingView: View? = null

    fun show() {
        if (floatingView != null) return

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
            y = 100
        }

        // Простейшая программная панель управления (без XML-разметки)
        val layout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setBackgroundColor(android.graphics.Color.parseColor("#CC000000"))
            setPadding(32, 32, 32, 32)
        }

        val title = android.widget.TextView(context).apply {
            text = "Idle Slayer Bot"
            setTextColor(android.graphics.Color.WHITE)
            textSize = 14f
            setPadding(0, 0, 0, 16)
        }

        val autoclickerSwitch = Switch(context).apply {
            text = "Автокликер"
            setTextColor(android.graphics.Color.WHITE)
            setOnCheckedChangeListener { _, isChecked ->
                (context as? IdleSlayerBotService)?.isAutoclickerActive = isChecked
            }
        }

        val afkSwitch = Switch(context).apply {
            text = "Full AFK Режим"
            setTextColor(android.graphics.Color.WHITE)
            setOnCheckedChangeListener { _, isChecked ->
                (context as? IdleSlayerBotService)?.isFullAfkMode = isChecked
            }
        }

        layout.addView(title)
        layout.addView(autoclickerSwitch)
        layout.addView(afkSwitch)

        floatingView = layout
        try {
            windowManager.addView(floatingView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun remove() {
        floatingView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            floatingView = null
        }
    }
}
