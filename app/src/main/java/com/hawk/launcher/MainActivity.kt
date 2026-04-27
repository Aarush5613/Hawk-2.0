package com.hawk.launcher

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HawkOS() }
    }
}

@Composable
fun HawkOS() {
    val context = LocalContext.current
    val sensors = remember { SensorEngine(context) } // Access our Speed/Weather logic
    
    // 1. The "Teleprompters" (State)
    var ramText by remember { mutableStateOf("SCANNING...") }
    var netSpeed by remember { mutableStateOf("0 kb/s") }
    var weather by remember { mutableStateOf("FETCHING WX...") }
    var apps by remember { mutableStateOf(listOf<AppInfo>()) }

    // 2. THE HEARTBEAT (The background loop)
    LaunchedEffect(Unit) {
        apps = getInstalledApps(context) // Load apps once at start
        
        while(true) {
            // Update RAM Stats
            val mi = android.app.ActivityManager.MemoryInfo()
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            am.getMemoryInfo(mi)
            val used = (mi.totalMem - mi.availMem) / 1048576L
            val total = mi.totalMem / 1048576L
            ramText = "$used / $total MB"

            // Update Network Speed
            netSpeed = sensors.getNetworkSpeed()

            // Update Weather (Every 10 minutes to save battery)
            if (System.currentTimeMillis() % 600000 < 1000) {
                weather = sensors.getWeatherData()
            }

            delay(500) // Pulse every half-second
        }
    }

    // 3. THE UI (The "Cyberdeck" Look)
    Row(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(modifier = Modifier.weight(1f).padding(16.dp)) {
            // System Status Header
            Text("HAWK_OS v1.0", color = Color(0xFF00FF41), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            Text("[RAM] $ramText", color = Color(0xFF00FF41), fontFamily = FontFamily.Monospace)
            Text("[NET] $netSpeed", color = Color(0xFF00FF41), fontFamily = FontFamily.Monospace)
            Text("[WX ] $weather", color = Color(0xFF00FF41), fontFamily = FontFamily.Monospace)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // The App List (Niagara Style)
            LazyColumn {
                items(apps) { app ->
                    Text(
                        text = "> ${app.label.uppercase()}",
                        color = Color.White,
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .clickable { launchApp(context, app.packageName) },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                }
            }
        }
        
        // Right-side Alpha-Bar (Design only for now)
        Column(modifier = Modifier.width(20.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ".forEach { 
                Text(it.toString(), color = Color.DarkGray, fontSize = 9.sp) 
            }
        }
    }
}

    Row(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // MAIN TERMINAL (Left Side)
        Column(modifier = Modifier.weight(1f).padding(16.dp)) {
            Text("HAWK_OS > STATUS: ONLINE", color = Color(0xFF00FF41), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Text("[SYS] $ramText", color = Color(0xFF00FF41), fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(20.dp))
            
            // THE APP LIST (Niagara Style - Text Only)
            LazyColumn {
                items(apps) { app ->
                    Text(
                        text = "> ${app.label.uppercase()}",
                        color = Color.White,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable { launchApp(context, app.packageName) },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // NIAGARA SCROLLER (Right Edge)
        Column(
            modifier = Modifier.width(30.dp).fillMaxHeight().background(Color(0xFF0A0A0A)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ".forEach { char ->
                Text(char.toString(), color = Color(0xFF444444), fontSize = 10.sp)
            }
        }
    }
}

data class AppInfo(val label: String, val packageName: String)

fun getInstalledApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    val mainIntent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
    return pm.queryIntentActivities(mainIntent, 0).map {
        AppInfo(it.loadLabel(pm).toString(), it.activityInfo.packageName)
    }.sortedBy { it.label }
}

fun launchApp(context: Context, packageName: String) {
    val intent = context.packageManager.getLaunchIntentForPackage(packageName)
    context.startActivity(intent)
}
