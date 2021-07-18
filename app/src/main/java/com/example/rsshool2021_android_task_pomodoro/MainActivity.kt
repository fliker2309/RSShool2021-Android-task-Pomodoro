package com.example.rsshool2021_android_task_pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
/*private fun initListeners(){
    val startMs = binding.fieldTimer.text.toString().toLongOrNull().run {

    }
}*/
    /*override fun onStart() {
        super.onStart()
        createNotificationChannel()
    }*/

  /*  private fun createNotificationChannel() {
        val notificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        val channel = NotificationChannelCompat
            .Builder(//TODO)
                . setName (//TODO)
            .setDescription(//TODO)
                . build ()
    }*/

    /* override fun onSaveInstanceState(outState: Bundle) {
         super.onSaveInstanceState(outState)
         outState.putAll(Bundle)
     }

     override fun onRestoreInstanceState(savedInstanceState: Bundle) {
         super.onRestoreInstanceState(savedInstanceState)
     }*/

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