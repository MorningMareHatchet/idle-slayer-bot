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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var toggleServiceButton: Button
    private lateinit var clickerButton: Button

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

        // Кнопка для отправки сигнала кликеру прямо из приложения
        clickerButton = Button(this).apply {
            text = "⚡ Сделать тестовый клик в игре"
            setOnClickListener {
                if (IdleSlayerBotService.isInstanceRunning()) {
                    // Отправляем команду службе кликнуть в центр экрана
                    val intent = Intent(this@MainActivity, IdleSlayerBotService::class.java).apply {
                        action = "ACTION_CLICK"
                        putExtra("x", 500f)
                        putExtra("y", 1200f)
                    }
                    startService(intent)
                    Toast.makeText(this@MainActivity, "Клик отправлен!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Сначала включите службу доступности!", Toast.LENGTH_LONG).show()
                }
            }
        }

        layout.addView(titleTextView)
        layout.addView(statusTextView)
        layout.addView(toggleServiceButton)
        layout.addView(clickerButton)

        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        if (IdleSlayerBotService.isInstanceRunning()) {
            statusTextView.text = "Статус: Служба работает ✅"
            statusTextView.setTextColor(Color.GREEN)
        } else {
            statusTextView.text = "Статус: Служба выключена ❌"
            statusTextView.setTextColor(Color.RED)
        }
    }
}
