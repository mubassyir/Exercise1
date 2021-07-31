package com.mubassyir.exercise1

import android.app.IntentService
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.lang.NullPointerException
import java.util.*

class CountDownIntentService : IntentService("CountDownTime") {

    init {
        instance = this
    }

    companion object {
        const val EXTRA_COUNT = "extra_count"
        const val EXTRA_STATUS = "extra_status"
        const val COUNTDOWN_BR = "com.mubassyir.exercise1"
        var intent: Intent? = Intent(COUNTDOWN_BR)
        private const val START_TIME_IN_MILLIS: Long = 30000
        private lateinit var instance: CountDownIntentService

        private var timeLeftInMillis = START_TIME_IN_MILLIS

        var isTimerRunning = false
        var countDownTimer: CountDownTimer? = null


        fun onPause() {
            isTimerRunning = false
            Log.d("Service", "Time paused")
            countDownTimer?.cancel()
        }
        fun resetTimer() {
            timeLeftInMillis = START_TIME_IN_MILLIS
            CountDownIntentService().updateCountDownTimer()
        }

        fun onStop() {
            isTimerRunning = false
            instance.stopSelf()
            Log.d("Service", "Service just stopped")
        }
    }



    override fun onHandleIntent(i: Intent?) {
        isTimerRunning = true
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownTimer()
            }

            override fun onFinish() {
                onStop()
                intent?.putExtra(EXTRA_STATUS, true)
                sendBroadcast(intent)
            }
        }.start()
        Looper.loop()
    }

    private fun updateCountDownTimer() {
        val minutes = (timeLeftInMillis / 1000).toInt() / 60
        val seconds = (timeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

        Log.d("Counting", timeLeftFormatted)
        try{
            intent?.putExtra(EXTRA_COUNT, timeLeftFormatted).also {
                sendBroadcast(intent)
            }
        } catch (e:NullPointerException){
            Log.e("err",e.toString())
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

