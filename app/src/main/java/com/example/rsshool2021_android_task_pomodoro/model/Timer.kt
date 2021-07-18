package com.example.rsshool2021_android_task_pomodoro.model

data class Timer(
    val id: Int,
    var currentMs: Long,
    var startMs: Long,
    var isStarted: Boolean = false,
    var isFinished: Boolean = false
)
