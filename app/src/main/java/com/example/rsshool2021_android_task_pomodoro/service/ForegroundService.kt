package com.example.rsshool2021_android_task_pomodoro.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.rsshool2021_android_task_pomodoro.*
import com.example.rsshool2021_android_task_pomodoro.model.Timer
import kotlinx.coroutines.*

class ForegroundService : Service() {

    private var isServiceStarted = false
    private var notificationManager: NotificationManager? = null
    private var job: Job? = null
    private var timer : Timer? = null

    private val builder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("My Timer")
            .setGroup("Timer")
            .setGroupSummary(false)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(getPendingIntent())
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
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
                 timer = intent?.extras?.getSerializable(STARTED_TIMER_TIME_MS)  as Timer ?: return
                commandStart(timer!!)
            }
            COMMAND_STOP -> commandStop()
            INVALID -> return
        }

    }

    private fun commandStart(timer: Timer){
        if(isServiceStarted) {
            return
        }
        Log.i("TAG", "commandStart()")
        try {
            if(timer.currentMs.toInt() !=0){
            moveToStartedState()
            startForegroundAndShowNotification()
            continueTimer(timer)
            }
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

    private fun continueTimer(timer: Timer){
        job = CoroutineScope(Dispatchers.Main).launch{
            while (timer.currentMs >=0){
                if(timer.currentMs.toInt() <=0) stopForeground(true)
                notificationManager?.notify(
                    NOTIFICATION_ID,
                    getNotification(
                        (timer.currentMs).displayTime()
                    )
                )
                Log.d("TAG","Timer current ms: ${timer.currentMs}")
                timer.currentMs -= INTERVAL
                delay(INTERVAL)
                if (timer.currentMs.toInt() <= 0) {
                    commandStop()
                }

            }
        }
    }

    private fun moveToStartedState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, ForegroundService::class.java))
        } else {
            startService(Intent(this, ForegroundService::class.java))
        }
    }

    private fun startForegroundAndShowNotification() {
        createChannel()
        val notification = getNotification("content")
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun getNotification(content: String) = builder.setContentText(content).build()

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "timer"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, channelName, importance
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    private fun getPendingIntent(): PendingIntent? {
        val resultIntent = Intent(this, MainActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {

        return object : CountDownTimer(timer.currentMs, UNIT_TEN_MS) {

            override fun onTick(millisUntilFinished: Long) {
                timer.currentMs = millisUntilFinished
                timer.currentMs.displayTime()
            }
            override fun onFinish() {

                stopSelf()
            }
        }
    }
}