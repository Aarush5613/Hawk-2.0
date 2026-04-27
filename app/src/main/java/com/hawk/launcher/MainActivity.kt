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
    var ramText by remember { mutableStateOf("0/0 MB") }
    var apps by remember { mutableStateOf(listOf<AppInfo>()) }

    // THE HEARTBEAT: Updates RAM every 500ms
    LaunchedEffect(Unit) {
        apps = getInstalledApps(context) // Load apps once
        while(true) {
            val mi = ActivityManager.MemoryInfo()
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.getMemoryInfo(mi)
            val used = (mi.totalMem - mi.availMem) / 1048576L
            val total = mi.totalMem / 1048576L
            ramText = "$used / $total MB"
            delay(500)
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
