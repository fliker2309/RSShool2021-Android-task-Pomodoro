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
import com.example.rsshool2021_android_task_pomodoro.*
import kotlinx.coroutines.*

class ForegroundService : Service() {

    private var isServiceStarted = false
    private var notificationManager: NotificationManager? = null
    private var job: Job? = null

    private val builder by lazy {
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

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        processCommand(intent)
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun processCommand(intent: Intent?){
        when(intent?.extras?.getString(COMMAND_ID)?: INVALID){
            COMMAND_START -> {
                val startTime = intent?.extras?.getLong(STARTED_TIMER_TIME_MS) ?: return
                commandStart(startTime)
            }
            COMMAND_STOP -> commandStop()
            INVALID -> return
        }

    }

    private fun commandStart(startTime: Long){
        if(isServiceStarted) {
            return
        }
        Log.i("TAG", "commandStart()")
        try {
            moveToStartedState()
            startForegrounAndShowNotification()
            continueTimer(startTime)
        } finally {
            isServiceStarted = true
        }
    }

    private fun commandStop(){
        if (!isServiceStarted){
            return
        }
        Log.i("TAG", "commandStop()")
        try {
            job?.cancel()
            stopForeground(true)
            stopSelf()
        } finally {
            isServiceStarted = false
        }
    }

    private fun continueTimer(startTime: Long){
        job = CoroutineScope.launch()
    }

}