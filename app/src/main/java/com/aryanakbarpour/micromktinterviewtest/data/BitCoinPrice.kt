package com.aryanakbarpour.micromktinterviewtest.data

import com.google.gson.annotations.SerializedName

data class BitCoinPrice(
    @SerializedName("time")
    val time: PTime,
    val disclaimer: String,
    val chartName: String,
    @SerializedName("bpi")
    val bpi: Bpi
)

data class PTime(
    val updated: String,
    val updatedISO: String,
    val updateduk: String
)

data class Bpi(
    @SerializedName("USD")
    val USD: currencyRate,
    @SerializedName("GBP")
    val GBP: currencyRate,
    @SerializedName("EUR")
    val EUR: currencyRate
)

data class currencyRate(
    val code: String,
    val symbol: String,
    val rate: String,
    val description: String,
    val rate_float: Float
)

