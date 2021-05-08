package com.mubassyir.exercise1

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
           if(isTimerRunning){
               pauseTimer()
           } else {
               startTimer()
           }
       }
        binding.btnReset.setOnClickListener {
            resetTimer()
        }
        updateCountDownTimer()
    }

    private fun updateCountDownTimer() {
        val minutes = (timeLeftInMillis / 1000).toInt() / 60
        val seconds = (timeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String = java.lang.String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.tvCountTime.text = timeLeftFormatted
    }

    private fun resetTimer() {
        timeLeftInMillis = START_TIME_IN_MILLIS
        updateCountDownTimer()
        binding.btnReset.visibility = View.INVISIBLE
        binding.btnStartPause.visibility = View.VISIBLE
    }

    private fun startTimer() {
        binding.btnStartPause.setText(R.string.pause)
        binding.btnReset.visibility = View.INVISIBLE
        isTimerRunning = true
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownTimer()
            }
            override fun onFinish() {
                isTimerRunning = false
                binding.btnStartPause.setText(R.string.start)
                binding.btnStartPause.visibility = View.INVISIBLE
                binding.btnReset.visibility = View.VISIBLE
            }
        }.start()
    }
    private fun pauseTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
        binding.btnStartPause.setText(R.string.start)
        binding.btnReset.visibility = View.VISIBLE
    }


}