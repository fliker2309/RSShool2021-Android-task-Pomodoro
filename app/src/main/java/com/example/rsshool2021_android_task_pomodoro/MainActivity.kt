package com.example.rsshool2021_android_task_pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.view.isInvisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rsshool2021_android_task_pomodoro.`interface`.TimerListener
import com.example.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import com.example.rsshool2021_android_task_pomodoro.model.Timer
import com.example.rsshool2021_android_task_pomodoro.recycler.TimerAdapter

class MainActivity : AppCompatActivity(), TimerListener {

    private lateinit var binding: ActivityMainBinding

    private val stopwatches = mutableListOf<Timer>()
    private val stopwatchAdapter = TimerAdapter(this)
    private var nextId = 0
    private var startMs: Long? = null

    private var minutes: EditText? = null
    private var seconds: EditText? = null

    private var min: Long? = null
    private var sec: Long? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }

        minutes = binding.fieldMinutes
        seconds = binding.fieldSeconds

        enableAddTimerButton()
        minutes?.doAfterTextChanged {
            min = minutes?.text.toString().toLongOrNull()
            enableAddTimerButton()
        }
        seconds?.doAfterTextChanged {
            sec = seconds?.text.toString().toLongOrNull()
            enableAddTimerButton()
        }

        initListeners()
    }

    private fun enableAddTimerButton() {
        binding.addNewTimerButton.isEnabled = isCorrectInput(min, sec)
    }

    private fun isCorrectInput(min: Long?, sec: Long?): Boolean {
        return min != null || sec != null
    }

    private fun initListeners() {

        binding.addNewTimerButton.setOnClickListener {

             startMs = min?.let {
                min!! * 1000L * 60L
            } ?: 0L

            startMs!! += sec?.let {
                sec!! * 1000L
            } ?: 0L

            changeTimer(it.id, startMs!!, false, startMs, false)

        }
    }

    override fun start(id: Int, startMs: Long) {
        changeTimer(id, startMs, true, startMs, false)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false, currentMs, false)
    }

    override fun delete(id: Int) {
        stopwatches.remove(stopwatches.find { it.id == id })
        stopwatchAdapter.submitList(stopwatches.toList())
    }

    private fun changeTimer(
        id: Int,
        startMs: Long,
        isStarted: Boolean,
        currentMs: Long?,
        isFinished: Boolean
    ) {
        val newTimers = mutableListOf<Timer>()
        stopwatches.forEach {
            if (it.id == id) {
                newTimers.add(
                    Timer(
                        it.id,
                        it.startMs,
                        isStarted,
                        currentMs ?: it.currentMs,
                        isFinished
                    )
                )
            } else {
                newTimers.add(it)
            }
        }
        stopwatchAdapter.submitList(newTimers)
        stopwatches.clear()
        stopwatches.addAll(newTimers)
    }
}