package com.example.rsshool2021_android_task_pomodoro.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rsshool2021_android_task_pomodoro.`interface`.StopwatchListener
import com.example.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding
import com.example.rsshool2021_android_task_pomodoro.model.Stopwatch

class StopwatchAdapter(
    private val listener: StopwatchListener
) : ListAdapter<Stopwatch, StopwatchViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopwatchViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = StopwatchItemBinding.inflate(layoutInflater, parent, false)
        return StopwatchViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: StopwatchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Stopwatch>() {

            override fun areItemsTheSame(oldItem: Stopwatch, newItem: Stopwatch): Boolean {
                return oldItem.id == newItem.id
            }

            override fun getChangePayload(oldItem: Stopwatch, newItem: Stopwatch) = Any()

            override fun areContentsTheSame(oldItem: Stopwatch, newItem: Stopwatch): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }
        }
    }

}