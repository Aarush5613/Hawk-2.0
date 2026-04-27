package com.hawk.launcher

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Root Container (The Terminal)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.BLACK)
            gravity = Gravity.CENTER
            setPadding(40, 40, 40, 40)
        }

        // 2. Header
        val header = TextView(this).apply {
            text = "HAWK OS v2.0\nKERNEL: STABLE\n-------------------"
            setTextColor(Color.GREEN)
            textSize = 18f
            gravity = Gravity.CENTER
        }

        // 3. Battery Stats
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
            baseContext.registerReceiver(null, filter)
        }
        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val batteryText = TextView(this).apply {
            text = "POWER: $level%"
            setTextColor(Color.GREEN)
            textSize = 24f
            setPadding(0, 20, 0, 10)
        }

        // 4. RAM Stats
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        val availableMegs = mi.availMem / 0x100000L
        
        val ramText = TextView(this).apply {
            text = "MEM_AVAIL: ${availableMegs}MB"
            setTextColor(Color.GREEN)
            textSize = 24f
            setPadding(0, 0, 0, 40)
        }

        // 5. App Drawer Button
        val launchBtn = Button(this).apply {
            text = "> ACCESS_ALL_APPS"
            setBackgroundColor(Color.TRANSPARENT)
            setTextColor(Color.GREEN)
            textSize = 20f
            // This pulls up the system's app picker for now
            setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_SET_WALLPAPER) // Testing a system trigger
                    startActivity(intent)
                    Toast.makeText(context, "Initializing App List...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Terminal Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Assemble the Terminal
        root.addView(header)
        root.addView(batteryText)
        root.addView(ramText)
        root.addView(launchBtn)

        setContentView(root)
    }
}
