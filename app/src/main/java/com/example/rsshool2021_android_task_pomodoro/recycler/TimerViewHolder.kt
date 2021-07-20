package com.example.rsshool2021_android_task_pomodoro.recycler

import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.`interface`.TimerListener
import com.example.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding
import com.example.rsshool2021_android_task_pomodoro.model.Timer

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener
) : RecyclerView.ViewHolder(binding.root) {

    private var timer: CountDownTimer? = null

    fun bind(timer: Timer) {

        binding.timerClock.text = timer.currentMs.displayTime()
        binding.progressBarCircular.setPeriod(timer.startMs)

        if (timer.isStarted) {
            startTimer(timer)
        } else {
            stopTimer()
        }

        if (timer.isFinished){
            binding.progressBarCircular.setCurrent(0L)
        } else{
            binding.progressBarCircular.setCurrent(timer.currentMs)
        }

        initButtonsListener(timer)
    }


    private fun initButtonsListener(timer: Timer) {

        binding.timerStartStopBtn.setOnClickListener {
            if (timer.isStarted) {
                listener.stop(timer.id, timer.currentMs)
            } else {
                listener.start(timer.id, timer.startMs)
            }
        }

        binding.deleteTimerButton.setOnClickListener {
            listener.delete(timer.id)
        }
    }

    private fun startTimer(timer: Timer) {

        if(timer.currentMs <= 0L) return
        this.timer?.cancel()
        this.timer = getCountDownTimer(timer)
        this.timer?.start()

        binding.blinkingIndicator.isVisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
        binding.timerStartStopBtn.text = "STOP"
    }

    private fun stopTimer() {

        timer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
        binding.timerStartStopBtn.text = "START"
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {
        return object : CountDownTimer(PERIOD, UNIT_TEN_MS) {
            val interval = UNIT_TEN_MS

            override fun onTick(p0: Long) {
                timer.currentMs -= interval
                binding.timerClock.text = timer.currentMs.displayTime()
            }

            override fun onFinish() {
                binding.timerClock.text = timer.currentMs.displayTime()
            }
        }
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
        private const val UNIT_TEN_MS = 10L
        private const val PERIOD = 1000L * 60L * 60L * 24L //day
    }
}