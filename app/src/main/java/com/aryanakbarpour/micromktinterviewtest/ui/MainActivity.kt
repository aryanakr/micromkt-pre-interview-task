package com.aryanakbarpour.micromktinterviewtest.ui


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

import com.aryanakbarpour.micromktinterviewtest.R
import com.aryanakbarpour.micromktinterviewtest.UpdateService
import com.aryanakbarpour.micromktinterviewtest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


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


        binding.startBtn.setOnClickListener {
            startStopUpdate()
        }

        viewModel.latestPrice.observe(this) {
            println("oversations here")
            it?.let { latestPrice ->
                binding.gbpRateText.text = latestPrice.bpi.GBP.rate
                binding.usdRateText.text = latestPrice.bpi.USD.rate
                binding.eurRateText.text = time.toString()

                binding.updateTimeText.text = latestPrice.time.updated
            }
        }

        serviceIntent = Intent(applicationContext, UpdateService::class.java)
        registerReceiver(updatePrice, IntentFilter(UpdateService.TIMER_UPDATED))

    }

    private val updatePrice: BroadcastReceiver = object: BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?) {
            print("on Recieve lnlnlnlnl")
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
        binding.startBtn.text = "Start"
        updateStarted = false
    }



}
