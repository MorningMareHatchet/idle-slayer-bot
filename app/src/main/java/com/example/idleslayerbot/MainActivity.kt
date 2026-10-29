package com.example.idleslayerbot

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var toggleServiceButton: Button
    private lateinit var showPanelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Простой и удобный интерфейс прямо в коде без сложных XML-файлов макета
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            gravity = android.view.Gravity.CENTER_HORIZONTAL
        }

        val titleTextView = TextView(this).apply {
            text = "Idle Slayer Bot"
            textSize = 24f
            setTypeface(null, android.graphics.Typeface.BOLD)
            gravity = android.view.Gravity.CENTER
            setPadding(0, 0, 0, 32)
        }

        statusTextView = TextView(this).apply {
            text = "Статус: Проверка..."
            textSize = 16f
            gravity = android.view.Gravity.CENTER
            setPadding(0, 0, 0, 48)
        }

        toggleServiceButton = Button(this).apply {
            text = "Настройки доступности"
            setOnClickListener {
                // Открываем системные настройки спец. возможностей для включения службы
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }
        }

        showPanelButton = Button(this).apply {
            text = "Показать плавающую панель"
            setOnClickListener {
                // Отправляем сигнал службе показать плавающее окно
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
        // Проверяем, активна ли служба доступности
        val serviceRunning = IdleSlayerBotService.isInstanceRunning()
        if (serviceRunning) {
            statusTextView.text = "Статус: Работает ✅"
            statusTextView.setTextColor(android.graphics.Color.GREEN)
            toggleServiceButton.text = "Выключить / Настройки службы"
        } else {
            statusTextView.text = "Статус: Остановлен ❌\nВключите службу в настройках"
            statusTextView.setTextColor(android.graphics.Color.RED)
            toggleServiceButton.text = "Включить службу доступности"
        }
    }
}
