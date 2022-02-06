package com.aryanakbarpour.micromktinterviewtest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aryanakbarpour.micromktinterviewtest.R
import com.aryanakbarpour.micromktinterviewtest.data.remote.PriceApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}