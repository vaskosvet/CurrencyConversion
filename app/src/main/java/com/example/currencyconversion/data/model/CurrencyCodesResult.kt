package com.example.currencyconversion.data.model

import com.google.gson.annotations.SerializedName

data class CurrencyCodesResult(
    val result: String,
    @SerializedName("supported_codes")
    val supportedCodes: List<List<String>>
)