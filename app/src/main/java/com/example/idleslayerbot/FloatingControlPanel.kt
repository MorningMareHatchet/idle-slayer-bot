package com.example.idleslayerbot

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Switch

class FloatingControlPanel(private val context: Context) {

    private var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var floatingView: View? = null

    fun show() {
        if (floatingView != null) return

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 100
        }

        val inflater = LayoutInflater.from(context)
        floatingView = inflater.inflate(R.layout.floating_panel_layout, null)

        val switchAutoclicker = floatingView?.findViewById<Switch>(R.id.switchAutoclicker)
        val switchUpgrades = floatingView?.findViewById<Switch>(R.id.switchUpgrades)
        val switchChestHunt = floatingView?.findViewById<Switch>(R.id.switchChestHunt)
        val switchFullAfk = floatingView?.findViewById<Switch>(R.id.switchFullAfk)
        val btnRunChestHunt = floatingView?.findViewById<Button>(R.id.btnRunChestHunt)

        switchAutoclicker?.setOnCheckedChangeListener { _, isChecked ->
            IdleSlayerBotService.instance?.isAutoclickerActive = isChecked
        }

        switchUpgrades?.setOnCheckedChangeListener { _, isChecked ->
            IdleSlayerBotService.instance?.isAutoUpgradeActive = isChecked
        }

        switchChestHunt?.setOnCheckedChangeListener { _, isChecked ->
            IdleSlayerBotService.instance?.isChestHuntActive = isChecked
        }

        switchFullAfk?.setOnCheckedChangeListener { _, isChecked ->
            IdleSlayerBotService.instance?.isFullAfkMode = isChecked
            switchAutoclicker?.isChecked = isChecked
            switchUpgrades?.isChecked = isChecked
            switchChestHunt?.isChecked = isChecked
        }

        btnRunChestHunt?.setOnClickListener {
            IdleSlayerBotService.instance?.executeChestHuntStrategy()
        }

        windowManager.addView(floatingView, layoutParams)
    }

    fun hide() {
        floatingView?.let {
            windowManager.removeView(it)
            floatingView = null
        }
    }
}
