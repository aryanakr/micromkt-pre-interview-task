package com.aryanakbarpour.micromktinterviewtest.di


import com.aryanakbarpour.micromktinterviewtest.data.remote.PriceApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePriceApi(): PriceApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.coindesk.com/v1/bpi/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PriceApiService::class.java)
    }
}