package com.example.idleslayerbot

import android.content.Intent
import android.content.pm.PackageManager
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
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var toggleServiceButton: Button
    private lateinit var requestShizukuButton: Button
    private lateinit var startAutoButton: Button
    private lateinit var stopAutoButton: Button

    private val shizukuPermissionListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Shizuku разрешение получено! ✅", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Shizuku разрешение отклонено ❌", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Shizuku.addRequestPermissionResultListener(shizukuPermissionListener)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val titleTextView = TextView(this).apply {
            text = "Idle Slayer Bot (Shizuku)"
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

        requestShizukuButton = Button(this).apply {
            text = "0. Запросить доступ Shizuku"
            setOnClickListener {
                if (Shizuku.pingBinder()) {
                    if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                        Shizuku.requestPermission(0)
                    } else {
                        Toast.makeText(this@MainActivity, "Shizuku уже разрешен!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Запустите приложение Shizuku!", Toast.LENGTH_LONG).show()
                }
            }
        }

        toggleServiceButton = Button(this).apply {
            text = "1. Настройки доступности"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        startAutoButton = Button(this).apply {
            text = "▶ Запустить автокликер"
            setOnClickListener {
                if (IdleSlayerBotService.isInstanceRunning()) {
                    val intent = Intent(this@MainActivity, IdleSlayerBotService::class.java).apply {
                        action = "ACTION_START_AUTO"
                        putExtra("x", 500f)
                        putExtra("y", 1200f)
                        putExtra("interval", 80L)
                    }
                    startService(intent)
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
                Toast.makeText(this@MainActivity, "Автокликер остановлен", Toast.LENGTH_SHORT).show()
            }
        }

        layout.addView(titleTextView)
        layout.addView(statusTextView)
        layout.addView(requestShizukuButton)
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
            statusTextView.text = "Статус: Служба отключена ❌"
            statusTextView.setTextColor(Color.RED)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Shizuku.removeRequestPermissionResultListener(shizukuPermissionListener)
    }
}
