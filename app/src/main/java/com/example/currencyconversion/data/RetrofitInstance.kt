package com.example.currencyconversion.data

import com.example.currencyconversion.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val url
        get() = "${BuildConfig.BASE_URL}${BuildConfig.API_KEY}/"

    val api: CurrencyConversionApi by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyConversionApi::class.java)
    }
}
