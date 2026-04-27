package com.hawk.launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Main Container
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.BLACK)
            setPadding(30, 50, 30, 30)
        }

        // 2. Terminal Header
        val header = TextView(this).apply {
            text = "HAWK OS > DIRECTORY_SCAN\n--------------------------"
            setTextColor(Color.GREEN)
            textSize = 18f
            gravity = Gravity.CENTER
        }
        root.addView(header)

        // 3. Scrollable Area for Apps
        val scrollView = ScrollView(this)
        val appContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        // 4. Fetch All Installed Apps
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        val pkgManager = packageManager
        val appList: List<ResolveInfo> = pkgManager.queryIntentActivities(mainIntent, 0)

        // 5. Build the List
        for (app in appList) {
            val appName = app.loadLabel(pkgManager).toString()
            val pkgName = app.activityInfo.packageName

            val btn = Button(this).apply {
                text = "> RUN $appName"
                setTextColor(Color.GREEN)
                setBackgroundColor(Color.TRANSPARENT)
                textSize = 16f
                gravity = Gravity.START or Gravity.CENTER_VERTICAL
                setPadding(20, 20, 20, 20)
                
                setOnClickListener {
                    val launchIntent = pkgManager.getLaunchIntentForPackage(pkgName)
                    if (launchIntent != null) {
                        startActivity(launchIntent)
                    } else {
                        Toast.makeText(context, "ERR: ACCESS_DENIED", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            appContainer.addView(btn)
        }

        scrollView.addView(appContainer)
        root.addView(scrollView)
        setContentView(root)
    }
}
