package com.aryanakbarpour.micromktinterviewtest.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryanakbarpour.micromktinterviewtest.data.BitCoinPrice
import com.aryanakbarpour.micromktinterviewtest.data.remote.PriceApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

enum class APIServiceStatus { LOADING, ERROR, DONE}

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val application: Application,
    private val priceApi: PriceApiService
) : ViewModel() {

    private val _status = MutableLiveData<APIServiceStatus>()
    val status: LiveData<APIServiceStatus> = _status

    private val _latestPrice = MutableLiveData<BitCoinPrice?>()
    val latestPrice: MutableLiveData<BitCoinPrice?> = _latestPrice

    private var _recentPricesList = ArrayList<BitCoinPrice>()
    val recentPricesList = _recentPricesList

    fun retrieveLatestPrice(){

        viewModelScope.launch {
            _status.value = APIServiceStatus.LOADING
            try{
                val apiResult = priceApi.getLatestBitCoinPrice()
                if (apiResult != null) {
                    if (recentPricesList.isEmpty() || recentPricesList.last().time.updated != apiResult.time.updated)
                        recentPricesList.add(apiResult)
                    _latestPrice.value = apiResult
                    _status.value = APIServiceStatus.DONE
                } else {
                    throw KotlinNullPointerException("Call from api is null")
                }

            } catch (e: Exception) {
                _status.value = APIServiceStatus.ERROR
                _latestPrice.value = null
                e.printStackTrace()
            }
        }
    }

    fun clearRecentPrices() {
        _recentPricesList.clear()
    }

}



