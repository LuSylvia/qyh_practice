package com.example.module_common.utils

import android.os.CountDownTimer

abstract class SecondsCountDownTimer(totalSeconds: Long) : CountDownTimer(totalSeconds * 1000L, 1000L) {
    var currentSeconds = totalSeconds
        private set

    override fun onTick(millisUntilFinished: Long) {
        --currentSeconds
        if (currentSeconds < 0) currentSeconds = 0
        onSeconds(currentSeconds)
    }

    override fun onFinish() {
        currentSeconds = 0
        onSeconds(currentSeconds)
    }

    abstract fun onSeconds(seconds: Long)
}