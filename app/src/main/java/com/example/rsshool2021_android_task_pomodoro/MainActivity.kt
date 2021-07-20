package com.example.rsshool2021_android_task_pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }


        /*binding.addNewTimerButton.setOnClickListener {
            stopwatches.add(Timer(nextId++, 0, false, false))
            stopwatchAdapter.submitList(stopwatches.toList())
        }*/


    }

    private fun initListeners() {

        binding.addNewTimerButton.setOnClickListener {

            val minutes = binding.fieldMinutes.text.toString()
            val seconds = binding.fieldSeconds.text.toString()

            if ((minutes).isEmpty() || seconds.isEmpty()) {
                binding.addNewTimerButton.isEnabled = false
            }
            var startMs = minutes.toLongOrNull().let {
                minutes.toLong() * 1000L * 60L
            } ?: 0L

            startMs += seconds.toLongOrNull()?.let {
                seconds.toLong() * 1000L
            } ?: 0L

        }
    }




    /*  override fun start(id: Int) {
          changeStopwatch(id,null,true)
      }

      override fun stop(id: Int, currentMs: Long) {
          changeStopwatch(id,currentMs,false)
      }
  */
    override fun delete(id: Int) {
        stopwatches.remove(stopwatches.find { it.id == id })
        stopwatchAdapter.submitList(stopwatches.toList())
    }

    /*private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<Timer>()
        stopwatches.forEach {
            if (it.id == id) {
                newTimers.add(Timer(it.id, currentMs ?: it.currentMs, isStarted))
            } else {
                newTimers.add(it)
            }
        }
        stopwatchAdapter.submitList(newTimers)
        stopwatches.clear()
        stopwatches.addAll(newTimers)
    }*/


}