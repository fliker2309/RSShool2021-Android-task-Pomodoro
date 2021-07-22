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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsshool2021_android_task_pomodoro.`interface`.TimerListener
import com.example.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import com.example.rsshool2021_android_task_pomodoro.model.Timer
import com.example.rsshool2021_android_task_pomodoro.recycler.TimerAdapter
import com.example.rsshool2021_android_task_pomodoro.service.COMMAND_ID
import com.example.rsshool2021_android_task_pomodoro.service.COMMAND_START
import com.example.rsshool2021_android_task_pomodoro.service.COMMAND_STOP
import com.example.rsshool2021_android_task_pomodoro.service.STARTED_TIMER_TIME_MS

class MainActivity : AppCompatActivity(), TimerListener, LifecycleObserver  {

    private lateinit var binding: ActivityMainBinding

    private val timers = mutableListOf<Timer>()
    private val timerAdapter = TimerAdapter(this)
    private var nextId = 0

    private var minutes: EditText? = null
    private var mins = ""
    private var minutesMillis = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }
        //проверка на ввод значения
        minutes = binding.fieldMinutes
        enableAddTimerButton()
        minutes?.doAfterTextChanged {
            mins = minutes?.text.toString()
            enableAddTimerButton()
        }

        initListeners()
    }

    private fun enableAddTimerButton() {
        binding.addNewTimerButton.isEnabled = isCorrectInput(mins)
    }

    private fun isCorrectInput(mins: String): Boolean {
        return mins != ""
    }

    private fun initListeners() {
        binding.addNewTimerButton.setOnClickListener {
            try {
                minutesMillis = mins.toLong()
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
    fun onAppBackgrounded(){
        timers.forEach {
            if(it.isStarted){
                val intent = Intent(this, ForegroundService::class.java)
                intent.putExtra(COMMAND_ID, COMMAND_START)
                intent.putExtra(STARTED_TIMER_TIME_MS, it)
                startService(intent)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForeground(){
       val intent = Intent(this,ForegroundService::class.java)
        intent.putExtra(COMMAND_ID,COMMAND_STOP)
        startService(intent)
    }

}