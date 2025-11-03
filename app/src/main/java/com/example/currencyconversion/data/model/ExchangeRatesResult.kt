package com.example.currencyconversion.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeRatesResult(
    val result: String,
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double>
)