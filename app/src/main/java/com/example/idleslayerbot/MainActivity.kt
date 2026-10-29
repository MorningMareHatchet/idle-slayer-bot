package com.example.idleslayerbot

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var toggleServiceButton: Button
    private lateinit var showPanelButton: Button
    private lateinit var overlayPermissionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val titleTextView = TextView(this).apply {
            text = "Idle Slayer Bot"
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 32)
        }

        statusTextView = TextView(this).apply {
            text = "Статус: Проверка..."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 32)
        }

        // Кнопка для выдачи разрешения поверх других окон
        overlayPermissionButton = Button(this).apply {
            text = "1. Разрешить поверх окон"
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                }
            }
        }

        toggleServiceButton = Button(this).apply {
            text = "2. Включить службу доступности"
            setOnClickListener {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }
        }

        showPanelButton = Button(this).apply {
            text = "Показать плавающую панель"
            setOnClickListener {
                val intent = Intent(this@MainActivity, IdleSlayerBotService::class.java).apply {
                    action = "ACTION_SHOW_PANEL"
                }
                startService(intent)
            }
        }

        layout.addView(titleTextView)
        layout.addView(statusTextView)
        layout.addView(overlayPermissionButton)
        layout.addView(toggleServiceButton)
        layout.addView(showPanelButton)

        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        
        // Проверяем разрешение поверх окон
        val hasOverlay = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }

        val serviceRunning = IdleSlayerBotService.isInstanceRunning()

        if (serviceRunning && hasOverlay) {
            statusTextView.text = "Статус: Все готово и работает ✅"
            statusTextView.setTextColor(Color.GREEN)
        } else if (!hasOverlay) {
            statusTextView.text = "Статус: Нет разрешения поверх окон ⚠️\nНажмите кнопку 1"
            statusTextView.setTextColor(Color.YELLOW)
        } else {
            statusTextView.text = "Статус: Служба выключена ❌\nВключите пункт 2"
            statusTextView.setTextColor(Color.RED)
        }
    }
}
