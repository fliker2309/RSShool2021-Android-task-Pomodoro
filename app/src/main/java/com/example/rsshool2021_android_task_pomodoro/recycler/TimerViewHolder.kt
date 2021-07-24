package com.example.rsshool2021_android_task_pomodoro.recycler

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.R
import com.example.rsshool2021_android_task_pomodoro.`interface`.TimerListener
import com.example.rsshool2021_android_task_pomodoro.model.Timer
import com.example.rsshool2021_android_task_pomodoro.utils.UNIT_TEN_MS
import com.example.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding
import com.example.rsshool2021_android_task_pomodoro.utils.displayTime

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root),LifecycleObserver {

    private var countDownTimer: CountDownTimer? = null

    fun bind(timer: Timer) {

        setIsRecyclable(true)

        if (timer.isFinished) {
            with(binding) {
                timerClock.text = "00:00:00"
                timerItem.setCardBackgroundColor(resources.getColor(R.color.teal_200))
                deleteTimerButton.setBackgroundColor(resources.getColor(R.color.teal_200))
                timerStartStopBtn.isClickable = false
                timerStartStopBtn.text = "ENDED"
                progressBarCircular.setPeriod(timer.startMs)
                progressBarCircular.setCurrent(timer.startMs - timer.currentMs)
                deleteTimerButton.setOnClickListener {
                    stopTimer(timer)
                    timer.isStarted = false
                    timerItem.setCardBackgroundColor(resources.getColor(R.color.white))
                    deleteTimerButton.setBackgroundColor(resources.getColor(R.color.white))
                    listener.delete(timer.id)
                }
            }
        } else
            with(binding) {
                timerItem.setCardBackgroundColor(resources.getColor(R.color.white))
                deleteTimerButton.setBackgroundColor(resources.getColor(R.color.white))
                timerClock.text = timer.currentMs.displayTime()
                timerStartStopBtn.text = "START"
                progressBarCircular.setPeriod(timer.startMs)
                if (timer.currentMs >= timer.startMs) {
                    progressBarCircular.setCurrent(0)
                } else progressBarCircular.setCurrent(timer.startMs - timer.currentMs)

                if (timer.isStarted) {
                    startTimer(timer)
                    binding.timerStartStopBtn.text = "STOP"
                } else {
                    stopTimer(timer)
                    binding.timerStartStopBtn.text = "START"
                }
                initButtonsListener(timer)
            }
          }

    private fun initButtonsListener(timer: Timer) {
        binding.timerStartStopBtn.setOnClickListener {
            if (timer.isStarted) {
                listener.stop(timer.id, timer.currentMs)
            } else {
                listener.start(timer.id)
            }
        }

        binding.deleteTimerButton.setOnClickListener {
            setIsRecyclable(true)
            stopTimer(timer)
            timer.isStarted = false
            binding.timerItem.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.deleteTimerButton.setBackgroundColor(resources.getColor(R.color.white))
            listener.delete(timer.id)
            Log.d("TAG","Delete timer ${timer.id}")
        }
    }

    private fun startTimer(timer: Timer) {
        setIsRecyclable(false)
        binding.timerStartStopBtn.text = "STOP"
        binding.timerItem.setCardBackgroundColor(resources.getColor(R.color.white))
        countDownTimer?.cancel()
        countDownTimer = getCountDownTimer(timer)
        countDownTimer?.start()
        binding.blinkingIndicator.isVisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer(timer: Timer) {
        countDownTimer?.cancel()
        binding.blinkingIndicator.isVisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
        binding.timerStartStopBtn.text = "START"
        Log.d("TAG", " Stop timer id = ${timer.id}")
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {
        return object : CountDownTimer(timer.currentMs, UNIT_TEN_MS) {

            override fun onTick(millisToFinish: Long) {
                timer.currentMs = millisToFinish
                binding.timerClock.text = millisToFinish.displayTime()
                binding.progressBarCircular.setCurrent(timer.startMs - timer.currentMs)
            }

            override fun onFinish() {
                setIsRecyclable(false)
                binding.timerItem.setCardBackgroundColor(resources.getColor(R.color.teal_200))
                binding.deleteTimerButton.setBackgroundColor(resources.getColor(R.color.teal_200))
                binding.progressBarCircular.setCurrent(timer.startMs - timer.currentMs)
                binding.blinkingIndicator.isVisible = false
                binding.timerStartStopBtn.isClickable = false
                binding.timerStartStopBtn.text = "ENDED"
                timer.isStarted = false
                timer.isFinished = true
                (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
                Log.d("TAG", " Finish timer id = ${timer.id} + timer isFinished = ${timer.isFinished}")
            }
        }
    }
}