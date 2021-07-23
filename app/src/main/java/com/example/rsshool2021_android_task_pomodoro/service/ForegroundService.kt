package com.example.rsshool2021_android_task_pomodoro.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.rsshool2021_android_task_pomodoro.CHANNEL_ID
import com.example.rsshool2021_android_task_pomodoro.R
import kotlinx.coroutines.*

class ForegroundService  : Service(){

    private var isServiceStarted = false
    private var notificationManager : NotificationManager? = null
    private var job: Job? = null

    private val builder by lazy{
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Simple Timer")
            .setGroup("Timer")
            .setGroupSummary(false)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(getPendingIntent())
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
            .build()
    }





    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }



}