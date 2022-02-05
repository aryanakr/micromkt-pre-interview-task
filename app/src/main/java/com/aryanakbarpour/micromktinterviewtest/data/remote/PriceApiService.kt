package com.aryanakbarpour.micromktinterviewtest.data.remote

import com.aryanakbarpour.micromktinterviewtest.data.BitCoinPrice
import retrofit2.http.GET

interface PriceApiService {

    @GET("currentprice.json")
    suspend fun getLatestBitCoinPrice(): BitCoinPrice
}