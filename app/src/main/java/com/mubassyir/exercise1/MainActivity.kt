package com.mubassyir.exercise1

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mubassyir.exercise1.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity(){


    companion object {
        private const val START_TIME_IN_MILLIS: Long = 600000
    }

    private lateinit var binding: ActivityMainBinding
    private var countDownTimer: CountDownTimer? =null
    private var isTimerRunning = false
    private var timeLeftInMillis = START_TIME_IN_MILLIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

       binding.btnStartPause.setOnClickListener {
         Intent(this,CountDownIntentService::class.java).also {
             startService(it)
         }
       }

        binding.tvCountTime.text = CountDownIntentService.timeLeftFormatted
//        binding.btnReset.setOnClickListener {
//
//        }
    }
}