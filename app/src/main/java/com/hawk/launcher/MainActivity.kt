package com.hawk.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HawkTerminal()
        }
    }
}

@Composable
fun HawkTerminal() {
    var ramInfo by remember { mutableStateOf("Scanning RAM...") }
    
    // Cyberdeck Update Loop (500ms)
    LaunchedEffect(Unit) {
        while(true) {
            val mi = android.app.ActivityManager.MemoryInfo()
            // In a real app, you'd pass context here to get real RAM stats
            ramInfo = "RAM: USED/TOTAL | CPU: 12% | NET: 140kb/s" 
            delay(500)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "HAWK_OS v1.0 ALPHA",
                color = Color(0xFF00FF41),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
            Text(
                text = ramInfo,
                color = Color(0xFF00FF41),
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(20.dp))
            
            // Terminal Input Simulation
            Text(
                text = "hawk@moto:~$ _",
                color = Color.White,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
