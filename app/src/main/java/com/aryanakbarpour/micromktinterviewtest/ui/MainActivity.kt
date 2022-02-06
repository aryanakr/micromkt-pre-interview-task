package com.aryanakbarpour.micromktinterviewtest.ui


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.lifecycle.ViewModelProvider

import com.aryanakbarpour.micromktinterviewtest.databinding.ActivityMainBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import com.github.mikephil.charting.utils.ColorTemplate





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

        initialiseChart()

        binding.startBtn.setOnClickListener {
            startStopUpdate()
        }

        binding.gbpChartSwitch.setOnClickListener {
            setChartData()
        }
        binding.eurChartSwitch.setOnClickListener {
            setChartData()
        }
        binding.usdChartSwitch.setOnClickListener {
            setChartData()
        }

        viewModel.latestPrice.observe(this) {
            it?.let { latestPrice ->
                binding.gbpRateText.text = String.format("%.3f",latestPrice.bpi.GBP.rate_float)
                binding.usdRateText.text = String.format("%.3f",latestPrice.bpi.USD.rate_float)
                binding.eurRateText.text = String.format("%.3f",latestPrice.bpi.EUR.rate_float)

                binding.updateTimeText.text = latestPrice.time.updateduk
                binding.timerText.text = time.toString()

                setChartData()
            }
        }

        serviceIntent = Intent(applicationContext, UpdateService::class.java)
        registerReceiver(updatePrice, IntentFilter(UpdateService.TIMER_UPDATED))

    }

    private fun initialiseChart() {
        // Setup chart
        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawLabels(false)

        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.legend.isEnabled = true
        binding.lineChart.description.isEnabled = false

        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
    }

    private fun setChartData() {
        val gbpEntries: ArrayList<Entry> = ArrayList()
        val usdEntries: ArrayList<Entry> = ArrayList()
        val eurEntries: ArrayList<Entry> = ArrayList()

        for (i in viewModel.recentPricesList.indices) {
            gbpEntries.add(Entry(i.toFloat(), viewModel.recentPricesList[i].bpi.GBP.rate_float))
            usdEntries.add(Entry(i.toFloat(), viewModel.recentPricesList[i].bpi.USD.rate_float))
            eurEntries.add(Entry(i.toFloat(), viewModel.recentPricesList[i].bpi.EUR.rate_float))
        }

        val gbpLineDataSet = LineDataSet(gbpEntries, "GBP Rate")
        gbpLineDataSet.color = Color.RED
        val usdLineDataSet = LineDataSet(usdEntries, "USD Rate")
        usdLineDataSet.color = Color.GREEN
        val eurLineDataSet = LineDataSet(eurEntries, "EUR Rate")
        eurLineDataSet.color = Color.BLUE

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        if (binding.gbpChartSwitch.isChecked)
            dataSets.add(gbpLineDataSet)
        if (binding.usdChartSwitch.isChecked)
            dataSets.add(usdLineDataSet)
        if (binding.eurChartSwitch.isChecked)
            dataSets.add(eurLineDataSet)

        val data = LineData(dataSets)
        binding.lineChart.data = data

        binding.lineChart.invalidate()
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
        viewModel.clearRecentPrices()
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
