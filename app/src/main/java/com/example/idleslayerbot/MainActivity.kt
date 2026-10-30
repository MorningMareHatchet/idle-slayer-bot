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
    private lateinit var startAutoButton: Button
    private lateinit var stopAutoButton: Button
    private var isAutoclking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val titleTextView = TextView(this).apply {
            text = "Idle Slayer Bot (AutoClicker)"
            textSize = 22f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }

        statusTextView = TextView(this).apply {
            text = "Статус: Проверка..."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 32)
        }

        toggleServiceButton = Button(this).apply {
            text = "1. Настройки доступности"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        startAutoButton = Button(this).apply {
            text = "▶ Запустить автокликер (цикл)"
            setOnClickListener {
                if (IdleSlayerBotService.isInstanceRunning()) {
                    val intent = Intent(this@MainActivity, IdleSlayerBotService::class.java).apply {
                        action = "ACTION_START_AUTO"
                        putExtra("x", 500f)    // Координата X по экрану
                        putExtra("y", 1200f)   // Координата Y по экрану
                        putExtra("interval", 80L) // Задержка в мс (скорость кликов)
                    }
                    startService(intent)
                    isAutoclking = true
                    Toast.makeText(this@MainActivity, "Автокликер запущен!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Включите службу доступности (п. 1)!", Toast.LENGTH_LONG).show()
                }
            }
        }

        stopAutoButton = Button(this).apply {
            text = "⏹ Остановить автокликер"
            setBackgroundColor(Color.RED)
            setOnClickListener {
                val intent = Intent(this@MainActivity, IdleSlayerBotService::class.java).apply {
                    action = "ACTION_STOP_AUTO"
                }
                startService(intent)
                isAutoclking = false
                Toast.makeText(this@MainActivity, "Автокликер остановлен", Toast.LENGTH_SHORT).show()
            }
        }

        layout.addView(titleTextView)
        layout.addView(statusTextView)
        layout.addView(toggleServiceButton)
        layout.addView(startAutoButton)
        layout.addView(stopAutoButton)

        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        if (IdleSlayerBotService.isInstanceRunning()) {
            statusTextView.text = "Статус: Служба доступности активна ✅"
            statusTextView.setTextColor(Color.GREEN)
        } else {
            statusTextView.text = "Статус: Служба отключена ❌\nВключите пункт 1"
            statusTextView.setTextColor(Color.RED)
        }
    }
}
