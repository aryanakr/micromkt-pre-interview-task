package com.aryanakbarpour.micromktinterviewtest

import android.app.Application
import com.aryanakbarpour.micromktinterviewtest.data.remote.PriceApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BitCoinPriceApp : Application() {

    val retrofit: PriceApiService = Retrofit.Builder()
        .baseUrl("https://api.coindesk.com/v1/bpi/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PriceApiService::class.java)
}