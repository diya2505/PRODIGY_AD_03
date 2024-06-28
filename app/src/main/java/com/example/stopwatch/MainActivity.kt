package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var startTime: Long = 0L
    private var timeInMilliseconds: Long = 0L
    private var timeSwapBuff: Long = 0L
    private var updateTime: Long = 0L

    private val handler = Handler()
    private val updateTimerThread = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updateTime = timeSwapBuff + timeInMilliseconds

            val secs = (updateTime / 1000).toInt()
            val mins = secs / 60
            val milliseconds = (updateTime % 1000).toInt()

            binding.timerText.text = String.format("%02d:%02d:%03d", mins, secs % 60, milliseconds)
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            it.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.button_click))
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(updateTimerThread, 0)
            binding.startButton.visibility = View.GONE
            binding.pauseButton.visibility = View.VISIBLE
            binding.resetButton.visibility = View.VISIBLE
        }

        binding.pauseButton.setOnClickListener {
            it.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.button_click))
            timeSwapBuff += timeInMilliseconds
            handler.removeCallbacks(updateTimerThread)
            binding.startButton.visibility = View.VISIBLE
            binding.pauseButton.visibility = View.GONE
        }

        binding.resetButton.setOnClickListener {
            it.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.button_click))
            startTime = 0L
            timeInMilliseconds = 0L
            timeSwapBuff = 0L
            updateTime = 0L
            binding.timerText.text = "00:00:00.000"
            handler.removeCallbacks(updateTimerThread)
            binding.startButton.visibility = View.VISIBLE
            binding.pauseButton.visibility = View.GONE
            binding.resetButton.visibility = View.GONE
        }
    }
}
