package com.hawk.launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Create the DrawerLayout (The Gesture Controller)
        val drawerLayout = DrawerLayout(this)
        drawerLayout.setBackgroundColor(Color.BLACK)

        // 2. The Main Screen (Home)
        val mainScreen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
        }
        val homeText = TextView(this).apply {
            text = "HAWK OS 2.0\n[SWIPE FROM LEFT FOR APPS]"
            setTextColor(Color.GREEN)
            textSize = 20f
            gravity = Gravity.CENTER
        }
        mainScreen.addView(homeText)

        // 3. The Slide-in Menu (App List)
        val appDrawerMenu = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.argb(230, 0, 0, 0)) // Semi-transparent black
            setPadding(30, 60, 30, 30)
            layoutParams = DrawerLayout.LayoutParams(800, DrawerLayout.LayoutParams.MATCH_PARENT).apply {
                gravity = Gravity.START // This enables the LEFT swipe
            }
        }

        val header = TextView(this).apply {
            text = "HAWK OS > ALL_APPS_SCAN\n--------------------------"
            setTextColor(Color.GREEN)
            textSize = 16f
            setPadding(0, 0, 0, 30)
        }
        appDrawerMenu.addView(header)

        // 4. Scrollable Container for All Apps
        val scrollView = ScrollView(this)
        val listContainer = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

        // 5. Automatic App Scanner (Fetches ALL launchable apps)
        val intent = Intent(Intent.ACTION_MAIN, null).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
        val allApps = packageManager.queryIntentActivities(intent, 0)
        
        // Sort apps alphabetically
        allApps.sortBy { it.loadLabel(packageManager).toString().lowercase() }

        for (app in allApps) {
            val appName = app.loadLabel(packageManager).toString()
            val packageName = app.activityInfo.packageName

            val appButton = Button(this).apply {
                text = "> RUN $appName"
                setTextColor(Color.GREEN)
                setBackgroundColor(Color.TRANSPARENT)
                textSize = 14f
                gravity = Gravity.START
                setPadding(10, 20, 10, 20)
                
                setOnClickListener {
                    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                    if (launchIntent != null) {
                        startActivity(launchIntent)
                        drawerLayout.closeDrawer(Gravity.START)
                    }
                }
            }
            listContainer.addView(appButton)
        }

        scrollView.addView(listContainer)
        appDrawerMenu.addView(scrollView)

        // Assemble the UI
        drawerLayout.addView(mainScreen)
        drawerLayout.addView(appDrawerMenu)

        setContentView(drawerLayout)
    }
}
