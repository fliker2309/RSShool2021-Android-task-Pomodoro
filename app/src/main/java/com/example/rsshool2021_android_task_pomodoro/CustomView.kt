package com.example.rsshool2021_android_task_pomodoro

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity.FILL
import android.view.View
import androidx.annotation.AttrRes

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val periodMs = 0L
    private val currentMs = 0L
    private var color = 0
    private var style = FILL
    private val paint = Paint()

    init {
        if (attrs != null){
            val styledAttrs = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomView,
                defStyleAttr,
                0
            )
        }

    }



}


