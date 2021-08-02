package com.mubassyir.exercise1

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import java.lang.NullPointerException
import java.util.*
class CountDownIntentService : IntentService("CountDownTime") {

    init {
        instance = this
    }

    companion object {
        const val EXTRA_COUNT = "extra_count"
        const val EXTRA_STATUS = "extra_status"

        private const val CHANNEL_ID = "id"
        private const val NOTIFICATION_NAME = "notification_name"
        private const val NOTIFICATION_ID = 3

        private const val TAG = "CountDownIntentService"
        const val COUNTDOWN_BR = "com.mubassyir.exercise1"
        var intent: Intent? = Intent(COUNTDOWN_BR)
        private const val START_TIME_IN_MILLIS: Long = 30000
        private lateinit var instance: CountDownIntentService

        private var timeLeftInMillis = START_TIME_IN_MILLIS

        var isTimerRunning = false
        var countDownTimer: CountDownTimer? = null


        fun onPause() {
            isTimerRunning = false
            countDownTimer?.cancel()
            Log.i(TAG, "Time paused")
        }

        fun resetTimer() {
            timeLeftInMillis = START_TIME_IN_MILLIS
            CountDownIntentService().updateCountDownTimer()
            Log.i(TAG, "Timer reset")
        }

        fun onStop() {
            isTimerRunning = false
            instance.stopSelf()
            Log.d(TAG, "Service just stopped")
        }
    }


    override fun onHandleIntent(i: Intent?) {
        Log.d(TAG, "Service started")
        isTimerRunning = true
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownTimer()
            }

            override fun onFinish() {
                onStop()
                timeLeftInMillis = START_TIME_IN_MILLIS
                showNotification()
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
        Log.d(TAG, timeLeftFormatted)
        try {
            intent?.putExtra(EXTRA_COUNT, timeLeftFormatted).also {
                sendBroadcast(intent)
            }
        } catch (e: NullPointerException) {
            Log.e(TAG, e.toString())
        }
    }

    private fun showNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.logo_eduwork)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.logo_eduwork))
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(resources.getString(R.string.notif_text))
                .setSubText(resources.getString(R.string.notif_title))
                .setSound(soundUri)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                    NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)

        Log.i(TAG, "Notification called")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}


