package com.example.currencyconversion.data

import android.content.Context
import androidx.core.content.edit
import com.example.currencyconversion.data.model.CurrencyCodesResult
import com.example.currencyconversion.data.model.ExchangeRatesResult
import com.google.gson.Gson

class Repository(context: Context) {
    companion object {
        const val SUPPORTED_CODES_CACHE = "supported_codes_cache"
        const val CURRENCY_CODES = "currency_codes"
    }

    private val api = RetrofitInstance.api

    private val preferences =
        context.getSharedPreferences(SUPPORTED_CODES_CACHE, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val cachedExchangeRates = mutableMapOf<String, Map<String, Double>>()

    suspend fun getCurrencyCodes(): CurrencyCodesResult {
        val cached = getCachedCurrencyCodes()
        cached?.let {
            return it
        }
        return try {
            val result = api.getCurrencyCodes()
            cacheResult(result)
            result
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getExchangeRates(baseCurrency: String): ExchangeRatesResult {
        val cachedRates = cachedExchangeRates[baseCurrency]
        return if (cachedRates != null) {
            ExchangeRatesResult(
                result = "success",
                baseCode = baseCurrency,
                conversionRates = cachedRates
            )
        } else {
            try {
                val result = api.getExchangeRates(baseCurrency)
                cachedExchangeRates[baseCurrency] = result.conversionRates
                result
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getCachedCurrencyCodes(): CurrencyCodesResult? {
        val cachedResult = preferences.getString(CURRENCY_CODES, null) ?: return null
        return gson.fromJson(cachedResult, CurrencyCodesResult::class.java)
    }


    private fun cacheResult(result: CurrencyCodesResult) {
        preferences.edit {
            putString(CURRENCY_CODES, gson.toJson(result))
        }
    }
}