package com.example.currencyconversion.data

import com.example.currencyconversion.data.model.CurrencyCodesResult
import com.example.currencyconversion.data.model.ExchangeRatesResult
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyConversionApi {
    @GET("codes")
    suspend fun getCurrencyCodes(): CurrencyCodesResult

    @GET("latest/{base}")
    suspend fun getExchangeRates(
        @Path("base") baseCurrency: String
    ): ExchangeRatesResult
}