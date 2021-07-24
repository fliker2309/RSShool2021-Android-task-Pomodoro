package com.example.rsshool2021_android_task_pomodoro.model

import java.io.Serializable

data class Timer(
    val id: Int,
    var startMs: Long,
    var isStarted: Boolean = false,
    var currentMs: Long,
    var isFinished: Boolean = false
) : Serializable
