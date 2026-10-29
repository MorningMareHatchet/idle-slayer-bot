package com.example.idleslayerbot

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import kotlin.random.Random

class IdleSlayerBotService : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())

    var isAutoclickerActive = false
    var isAutoUpgradeActive = false
    var isChestHuntActive = false
    var isFullAfkMode = false

    companion object {
        var instance: IdleSlayerBotService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        startEngine()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {
        stopAll()
    }

    private fun stopAll() {
        isAutoclickerActive = false
        isAutoUpgradeActive = false
        isChestHuntActive = false
        isFullAfkMode = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun startEngine() {
        runMainLoop()
    }

    private fun runMainLoop() {
        handler.post(object : Runnable {
            override fun run() {
                if (isAutoclickerActive || isFullAfkMode) {
                    performRandomizedJump()
                }
                val nextDelay = Random.nextLong(850, 1150)
                handler.postDelayed(this, nextDelay)
            }
        })

        handler.post(object : Runnable {
            override fun run() {
                if ((isAutoUpgradeActive || isFullAfkMode) && !isChestHuntActive) {
                    executeUpgradeRoutine()
                }
                handler.postDelayed(this, 15000)
            }
        })
    }

    private fun performRandomizedJump() {
        val rx = 2200f + Random.nextFloat() * 30f - 15f
        val ry = 900f + Random.nextFloat() * 30f - 15f
        val duration = Random.nextLong(50, 80)
        performTap(rx, ry, duration)
    }

    private fun executeUpgradeRoutine() {
        performTap(120f, 120f, 50)
        handler.postDelayed({
            performTap(400f, 320f, 50)
        }, 400)
        handler.postDelayed({
            performTap(2050f, 500f, 50)
        }, 800)
        handler.postDelayed({
            performTap(1210f, 950f, 50)
        }, 1200)
    }

    fun executeChestHuntStrategy() {
        if (!isChestHuntActive && !isFullAfkMode) return

        val startX = 810f
        val startY = 260f
        val stepX = 200f
        val stepY = 140f

        val chests = mutableListOf<Pair<Float, Float>>()
        for (row in 0 until 4) {
            for (col in 0 until 5) {
                chests.add(Pair(startX + col * stepX, startY + row * stepY))
            }
        }

        val shuffledChests = chests.shuffled(Random(System.currentTimeMillis()))

        shuffledChests.forEachIndexed { index, chest ->
            handler.postDelayed({
                if (isChestHuntActive || isFullAfkMode) {
                    val cx = chest.first + Random.nextFloat() * 20f - 10f
                    val cy = chest.second + Random.nextFloat() * 20f - 10f
                    performTap(cx, cy, 60)
                }
            }, (index * 450).toLong())
        }
    }

    private fun performTap(x: Float, y: Float, duration: Long) {
        val path = Path().apply { moveTo(x, y) }
        val stroke = GestureDescription.StrokeDescription(path, 0, duration)
        val builder = GestureDescription.Builder().addStroke(stroke)
        dispatchGesture(builder.build(), null, null)
    }
}
