package com.hawk.launcher

import android.content.Context
import android.net.TrafficStats
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SensorEngine(val context: Context) {

    // 1. Get Real-Time Network Speed (kb/s)
    private var lastRxBytes: Long = TrafficStats.getTotalRxBytes()
    private var lastTime: Long = System.currentTimeMillis()

    fun getNetworkSpeed(): String {
        val currentRxBytes = TrafficStats.getTotalRxBytes()
        val currentTime = System.currentTimeMillis()
        val deltaT = (currentTime - lastTime) / 1000L
        if (deltaT <= 0) return "0 kb/s"

        val speed = (currentRxBytes - lastRxBytes) / 1024 / deltaT
        lastRxBytes = currentRxBytes
        lastTime = currentTime
        return "$speed kb/s"
    }

    // 2. Get Weather (Open-Meteo: No API Key needed!)
    suspend fun getWeatherData(): String = withContext(Dispatchers.IO) {
        try {
            // Example for Mumbai (lat: 19.07, lon: 72.87). You can automate this later!
            val response = URL("https://api.open-meteo.com/v1/forecast?latitude=19.07&longitude=72.87&current_weather=true").readText()
            val temp = response.substringAfter("\"temperature\":").substringBefore(",")
            "TEMP: ${temp}°C"
        } catch (e: Exception) {
            "TEMP: --°C"
        }
    }
}
