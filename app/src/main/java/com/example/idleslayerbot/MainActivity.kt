package com.example.idleslayerbot

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
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
            setPadding(0, 0, 0, 48)
        }

        toggleServiceButton = Button(this).apply {
            text = "Настройки доступности"
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
        layout.addView(toggleServiceButton)
        layout.addView(showPanelButton)

        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        val serviceRunning = IdleSlayerBotService.isInstanceRunning()
        if (serviceRunning) {
            statusTextView.text = "Статус: Работает ✅"
            statusTextView.setTextColor(Color.GREEN)
            toggleServiceButton.text = "Служба активна"
        } else {
            statusTextView.text = "Статус: Остановлен ❌\nВключите службу в настройках"
            statusTextView.setTextColor(Color.RED)
            toggleServiceButton.text = "Включить службу доступности"
        }
    }
}
