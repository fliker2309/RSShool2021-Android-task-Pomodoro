package com.example.rsshool2021_android_task_pomodoro.`interface`

interface TimerListener {

    fun start(id: Int)

    fun stop(id:Int,currentMs : Long)

    fun delete(id: Int)
}