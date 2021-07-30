package com.mubassyir.exercise1

import android.app.IntentService
import android.content.Intent
import android.os.CountDownTimer
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import java.util.*

class CountDownIntentService : IntentService("CountDownTime") {

    init {
        instance = this
    }

    companion object {
        private const val START_TIME_IN_MILLIS: Long = 30000
        private lateinit var instance: CountDownIntentService

        private var timeLeftInMillis = START_TIME_IN_MILLIS

        var isTimerRunning = false
        var countDownTimer: CountDownTimer? = null

        var timeLeftFormatted : String = "00"

    }

    override fun onHandleIntent(intent: Intent?) {
        isTimerRunning = true
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000).toInt() / 60
                val seconds = (millisUntilFinished / 1000).toInt() % 60
                timeLeftFormatted = java.lang.String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                Log.d("Counting", timeLeftFormatted)
            }

            override fun onFinish() {
                isTimerRunning = false
                instance.stopSelf()
                Log.d("Service", "Service just stopped")
            }
        }.start()
        Looper.loop()
    }
}

