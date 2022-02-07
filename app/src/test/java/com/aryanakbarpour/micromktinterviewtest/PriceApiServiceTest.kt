package com.aryanakbarpour.micromktinterviewtest

import com.aryanakbarpour.micromktinterviewtest.data.remote.PriceApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PriceApiServiceTest : BaseTest() {

    private lateinit var service: PriceApiService

    @Before
    fun setup() {
        val url = mockWebServer.url("/")
        service = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PriceApiService::class.java)
    }

    @Test
    fun priceApiServiceTest() {
        enqueue("currentprice.json")
        runBlocking {
            val apiResponse = service.getLatestBitCoinPrice()

            Assert.assertNotNull(apiResponse)
            if(apiResponse!=null){
                Assert.assertNotNull("time is null", apiResponse.time)

                Assert.assertTrue("updated time is wrong", apiResponse.time.updated == "Feb 5, 2022 19:54:00 UTC")



                Assert.assertNotNull("bpi is null", apiResponse.bpi)
                Assert.assertNotNull("bpi USD is null", apiResponse.bpi.USD)


                Assert.assertTrue("USD rate is wrong", apiResponse.bpi.USD.rate == "41,584.9333")


                Assert.assertNotNull("bpi GBP is null", apiResponse.bpi.GBP)
                Assert.assertTrue("GBP rate is wrong", apiResponse.bpi.GBP.rate == "30,717.6259")

                Assert.assertNotNull("bpi EUR is null", apiResponse.bpi.EUR)
                Assert.assertTrue("EUR rate is wrong", apiResponse.bpi.EUR.rate == "36,321.4036")
            }
        }
    }
}