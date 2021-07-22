package com.example.rsshool2021_android_task_pomodoro.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rsshool2021_android_task_pomodoro.`interface`.TimerListener
import com.example.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding
import com.example.rsshool2021_android_task_pomodoro.model.Timer

class TimerAdapter(
    private val listener: TimerListener
) : ListAdapter<Timer, TimerViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TimerItemBinding.inflate(layoutInflater, parent, false)
        return TimerViewHolder(
            binding,
            listener,
            binding.root.context.resources
        )
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Timer>() {

            override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun getChangePayload(oldItem: Timer, newItem: Timer) = Any()

            override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }
        }
    }
}