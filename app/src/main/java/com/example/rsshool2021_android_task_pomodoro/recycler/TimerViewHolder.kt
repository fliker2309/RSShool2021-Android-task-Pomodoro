package com.example.rsshool2021_android_task_pomodoro.recycler

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.R
import com.example.rsshool2021_android_task_pomodoro.`interface`.TimerListener
import com.example.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding
import com.example.rsshool2021_android_task_pomodoro.model.Timer
import com.example.rsshool2021_android_task_pomodoro.service.UNIT_TEN_MS
import com.example.rsshool2021_android_task_pomodoro.service.displayTime

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    private var countDownTimer: CountDownTimer? = null

    fun bind(timer: Timer) {
        binding.timerClock.text = timer.currentMs.displayTime()
        binding.timerStartStopBtn.text = "START"
        binding.progressBarCircular.setPeriod(timer.startMs)
        setIsRecyclable(true)

        if (timer.currentMs >= timer.startMs) {
            binding.progressBarCircular.setCurrent(0)
        } else binding.progressBarCircular.setCurrent(timer.startMs - timer.currentMs)

        if (timer.isStarted) {
            startTimer(timer)
        } else {
            stopTimer(timer)
        }

        initButtonsListener(timer)
    }


    private fun initButtonsListener(timer: Timer) {
        binding.timerStartStopBtn.setOnClickListener {
            if (timer.isStarted) {
                listener.stop(timer.id, timer.currentMs)
                binding.timerStartStopBtn.text = "START"
            } else {
                listener.start(timer.id)
                binding.timerStartStopBtn.text = "STOP"
            }
        }

        binding.deleteTimerButton.setOnClickListener {
            setIsRecyclable(true)
            stopTimer(timer)
            timer.isStarted = false
            listener.delete(timer.id)
        }
    }

    private fun startTimer(timer: Timer) {
        setIsRecyclable(false)

        countDownTimer?.cancel()
        countDownTimer = getCountDownTimer(timer)
        countDownTimer?.start()

        binding.blinkingIndicator.isVisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
        binding.timerStartStopBtn.text = "STOP"
    }

    private fun stopTimer(timer: Timer) {
        countDownTimer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
        binding.timerStartStopBtn.text = "START"
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {
        return object : CountDownTimer(timer.currentMs, UNIT_TEN_MS) {

            override fun onTick(millisToFinish: Long) {
                timer.currentMs = millisToFinish
                binding.timerClock.text = millisToFinish.displayTime()
                binding.progressBarCircular.setCurrent(timer.startMs - timer.currentMs)
            }

            override fun onFinish() {
             /*   binding.timerItem.setCardBackgroundColor(resources.getColor(R.color.teal_700))*/
                binding.progressBarCircular.setCurrent(timer.startMs - timer.currentMs)
                binding.blinkingIndicator.isVisible = false
                binding.timerStartStopBtn.isClickable = false
                setIsRecyclable(true)
                (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
            }
        }
    }
}