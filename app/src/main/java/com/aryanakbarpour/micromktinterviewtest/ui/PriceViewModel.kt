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

    fun retrieveLatestPrice(){

        viewModelScope.launch {
            _status.value = APIServiceStatus.LOADING
            try{
                _latestPrice.value = priceApi.getLatestBitCoinPrice()
                _status.value = APIServiceStatus.DONE
            } catch (e: Exception) {
                _status.value = APIServiceStatus.ERROR
                _latestPrice.value = null
                e.printStackTrace()
            }
        }
    }

}



