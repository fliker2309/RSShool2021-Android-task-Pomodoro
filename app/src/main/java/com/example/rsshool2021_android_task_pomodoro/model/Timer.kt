package com.example.rsshool2021_android_task_pomodoro.model

data class Timer(
    val id: Int,
    var startMs: Long,
    var isStarted: Boolean = false,
    var currentMs: Long
)
