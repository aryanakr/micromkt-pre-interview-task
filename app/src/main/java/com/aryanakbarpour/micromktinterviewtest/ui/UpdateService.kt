package com.aryanakbarpour.micromktinterviewtest.ui

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class UpdateService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    private val timer = Timer()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Set the one minute periodic update

        val time = intent?.getDoubleExtra(TIME_EXTRA, 0.0)
        time?.let {
            timer.scheduleAtFixedRate(TimeTask(it), 0, 1000)
        }

        return START_NOT_STICKY
    }

    private inner class TimeTask(private var time:Double) : TimerTask(){
        // Updating a double (time, extra in intent) to create a tick counter
        override fun run() {
            val intent = Intent(TIMER_UPDATED)
            time++
            intent.putExtra(TIME_EXTRA, time)
            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }


    companion object {
        const val  TIMER_UPDATED = "timerUpdated"
        const val TIME_EXTRA = "timeExtra"
    }

}
