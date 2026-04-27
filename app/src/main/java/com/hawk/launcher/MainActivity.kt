package com.hawk.launcher

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Setup the Root Layout (The Container)
        val rootLayout = LinearLayout(this)
        rootLayout.orientation = LinearLayout.VERTICAL
        rootLayout.setBackgroundColor(Color.BLACK)
        rootLayout.gravity = Gravity.CENTER
        rootLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        // 2. Setup the "Matrix" Title
        val titleView = TextView(this)
        titleView.text = "HAWK OS 2.0\nSYSTEM ONLINE"
        titleView.setTextColor(Color.GREEN)
        titleView.textSize = 32f
        titleView.gravity = Gravity.CENTER
        titleView.setPadding(0, 0, 0, 50)
        
        // 3. Setup a Simple Interaction (Fixes the "it" and "Context" errors)
        titleView.setOnClickListener { view ->
            val context: Context = view.context
            Toast.makeText(context, "Scanning System...", Toast.LENGTH_SHORT).show()
            
            // Example of a safe Intent call
            // val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com"))
            // context.startActivity(intent)
        }

        // 4. Assemble the UI
        rootLayout.addView(titleView)
        setContentView(rootLayout)
    }
}
