package com.aryanakbarpour.micromktinterviewtest.ui


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.lifecycle.ViewModelProvider

import com.aryanakbarpour.micromktinterviewtest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: PriceViewModel

    private var updateStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(PriceViewModel::class.java)
        viewModel.retrieveLatestPrice()

        binding.startBtn.setOnClickListener {
            startStopUpdate()
        }

        viewModel.latestPrice.observe(this) {
            it?.let { latestPrice ->
                binding.gbpRateText.text = String.format("%.3f",latestPrice.bpi.GBP.rate_float)
                binding.usdRateText.text = String.format("%.3f",latestPrice.bpi.USD.rate_float)
                binding.eurRateText.text = String.format("%.3f",latestPrice.bpi.EUR.rate_float)

                binding.updateTimeText.text = latestPrice.time.updateduk
                binding.timerText.text = time.toString()
            }
        }

        serviceIntent = Intent(applicationContext, UpdateService::class.java)
        registerReceiver(updatePrice, IntentFilter(UpdateService.TIMER_UPDATED))

    }

    private val updatePrice: BroadcastReceiver = object: BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                time = it.getDoubleExtra(UpdateService.TIME_EXTRA, 0.0)
            }

            viewModel.retrieveLatestPrice()
        }
    }

    private fun startStopUpdate(){
        if (updateStarted)
            stopUpdate()
        else
            startUpdate()
    }

    private fun startUpdate() {
        serviceIntent.putExtra(UpdateService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.startBtn.text = "Stop"
        updateStarted = true
    }
    //getDrawable(R.drawable.ic_baseline_pause

    private fun stopUpdate() {
        stopService(serviceIntent)
        time = 0.0
        binding.timerText.text = ""
        binding.startBtn.text = "Start"
        updateStarted = false
    }



}
