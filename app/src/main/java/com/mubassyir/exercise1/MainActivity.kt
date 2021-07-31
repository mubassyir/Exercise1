package com.mubassyir.exercise1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mubassyir.exercise1.CountDownIntentService.Companion.COUNTDOWN_BR
import com.mubassyir.exercise1.CountDownIntentService.Companion.EXTRA_COUNT
import com.mubassyir.exercise1.CountDownIntentService.Companion.EXTRA_STATUS
import com.mubassyir.exercise1.CountDownIntentService.Companion.isTimerRunning
import com.mubassyir.exercise1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        binding.btnStartPause.setOnClickListener {
            if (isTimerRunning){
                CountDownIntentService.onPause()
                binding.btnStartPause.text = getString(R.string.start)
                binding.btnReset.visibility = View.VISIBLE
            } else{
                Intent(this, CountDownIntentService::class.java).also {
                    startService(it)
                }
                binding.btnStartPause.text = getString(R.string.pause)
                binding.btnReset.visibility = View.INVISIBLE
            }
        }

        binding.btnReset.setOnClickListener {
            CountDownIntentService.resetTimer()
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateUI(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
        Log.i(TAG, "Unregistered broadcast receiver")
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReceiver, IntentFilter(COUNTDOWN_BR))
        Log.i(TAG, "Registered broadcast receiver")
    }

    override fun onStop() {
        try {
            unregisterReceiver(broadcastReceiver)
        } catch (e: Exception) {
        }
        super.onStop()
    }

    private fun updateUI(intent: Intent) {
        intent.extras?.let{
            binding.tvCountTime.text = intent.getStringExtra(EXTRA_COUNT)
            if(intent.getBooleanExtra(EXTRA_STATUS,false)){
                Toast.makeText(this,"Counting is Up !",Toast.LENGTH_SHORT).show()
                binding.btnStartPause.text = getString(R.string.start)
                binding.btnReset.visibility = View.VISIBLE
            }
        }
    }
}