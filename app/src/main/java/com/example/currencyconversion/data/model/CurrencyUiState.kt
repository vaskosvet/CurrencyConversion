package com.example.currencyconversion.data.model

data class CurrencyUiState(
    val isLoading: Boolean = false,
    val availableCurrencies: List<Currency> = listOf(),
    val fromCurrency: Currency? = null,
    val toCurrency: Currency? = null,
    val amountText: String = "",
    val conversionResult: Double? = null,
    val conversionRate: Double? = null,
    val isValidConversion: Boolean = false,
)
