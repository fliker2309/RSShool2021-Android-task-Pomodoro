package com.example.rsshool2021_android_task_pomodoro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsshool2021_android_task_pomodoro.`interface`.TimerListener
import com.example.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import com.example.rsshool2021_android_task_pomodoro.model.Timer
import com.example.rsshool2021_android_task_pomodoro.recycler.TimerAdapter
import com.example.rsshool2021_android_task_pomodoro.service.ForegroundService

class MainActivity : AppCompatActivity(), TimerListener, LifecycleObserver {

    private lateinit var binding: ActivityMainBinding

    private val timers = mutableListOf<Timer>()
    private val timerAdapter = TimerAdapter(this)
    private var nextId = 0

    private var minutes: EditText? = null
    private var minutesString = ""
    private var minutesMillis = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }

        //проверка на ввод значения
        minutes = binding.fieldMinutes
        enableAddTimerButton()
        minutes?.doAfterTextChanged {
            minutesString = minutes?.text.toString()
            enableAddTimerButton()
        }

        initListeners()
    }

    private fun enableAddTimerButton() {
        binding.addNewTimerButton.isEnabled = isCorrectInput(minutesString)
    }

    private fun isCorrectInput(minutesString: String): Boolean {
        return minutesString != "" && minutesString != "0" && minutesString != "00" && minutesString != "000" && minutesString != "0000"
    }

    private fun initListeners() {
        binding.addNewTimerButton.setOnClickListener {
            try {
                minutesMillis = minutesString.toLong()
                minutesMillis *= 1000L * 60
                timers.add(Timer(nextId++, minutesMillis, false, minutesMillis))
                timerAdapter.submitList(timers.toList())
            } catch (e: NumberFormatException) {
                Toast.makeText(applicationContext, "Please,delete symbol :", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
        }
    }

    override fun start(id: Int) {
        val stopTimers = mutableListOf<Timer>()
        timers.forEach {
            stopTimers.add(
                Timer(
                    it.id,
                    it.startMs,
                    false,
                    it.currentMs
                )
            )
        }
        timerAdapter.submitList(stopTimers)
        timers.clear()
        timers.addAll(stopTimers)
        changeTimer(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false)
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
    }

    private fun changeTimer(
        id: Int,
        currentMs: Long?,
        isStarted: Boolean
    ) {
        val newTimers = mutableListOf<Timer>()
        timers.forEach { timer ->
            if (timer.id == id) {
                newTimers.add(
                    Timer(
                        timer.id,
                        timer.startMs,
                        isStarted,
                        currentMs ?: timer.currentMs
                    )
                )
            } else {
                newTimers.add(timer)
            }
        }

        timerAdapter.submitList(newTimers)
        timers.clear()
        timers.addAll(newTimers)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        timers.forEach {
            if (it.isStarted) {
                val startIntent = Intent(this, ForegroundService::class.java)
                startIntent.putExtra(COMMAND_ID, COMMAND_START)
                startIntent.putExtra(STARTED_TIMER_TIME_MS, it)
                startService(startIntent)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForeground() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

    override fun onDestroy() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
        super.onDestroy()
    }

}