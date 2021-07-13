package com.example.rsshool2021_android_task_pomodoro.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding
import com.example.rsshool2021_android_task_pomodoro.model.Stopwatch

class StopwatchViewHolder(private val binding: StopwatchItemBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(stopwatch: Stopwatch) {
        binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()
    }

    private fun Long.displayTime(): String {
        if (this <= 0L) return START_TIME

        val h = this / 1000 / 3600
        val m = this / 1000 % 3600 / 60
        val s = this / 1000 % 60

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }


    private companion object {
        private const val START_TIME = "00:00:00"
    }
}