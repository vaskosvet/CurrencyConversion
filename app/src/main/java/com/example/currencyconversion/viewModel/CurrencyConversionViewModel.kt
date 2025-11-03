package com.example.currencyconversion.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconversion.data.Repository
import com.example.currencyconversion.data.model.Currency
import com.example.currencyconversion.data.model.CurrencyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CurrencyConversionViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()
    private val repository = Repository(application.applicationContext)

    init {
        fetchCurrencyCodes()
    }

    fun onSwapCurrenciesClicked() {
        swapCurrencies()
    }

    fun swapCurrencies() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val amount = currentState.amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                return@launch
            }
            val newFromCurrency = currentState.toCurrency
            val newToCurrency = currentState.fromCurrency
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            try {
                newFromCurrency?.let {
                    val result = repository.getExchangeRates(it.code)
                    if (result.result.equals("success", true)) {
                        val newExchangeRate = result.conversionRates[newToCurrency?.code]
                        _uiState.update { state ->
                            state.copy(
                                fromCurrency = newFromCurrency,
                                toCurrency = newToCurrency,
                                conversionRate = newExchangeRate,
                                conversionResult = newExchangeRate?.times(amount),
                                isLoading = false
                            )
                        }
                    } else {
                        _uiState.update { state ->
                            state.copy(isLoading = false)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    fun convertCurrency(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val amount = currentState.amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                return@launch
            }
            _uiState.update {
                it.copy(isLoading = true)
            }

            try {
                currentState.fromCurrency?.let { currency ->
                    val result = repository.getExchangeRates(currency.code)
                    if (result.result.equals("success", true)) {
                        val newExchangeRate = result.conversionRates[currentState.toCurrency?.code]
                        _uiState.update { state ->
                            state.copy(
                                conversionRate = newExchangeRate,
                                conversionResult = newExchangeRate?.times(amount),
                                isLoading = false
                            )
                        }
                        onSuccess()
                    } else {
                        _uiState.update { state ->
                            state.copy(isLoading = false)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }

        }

    }

    fun onAmountTextChanged(amountText: String) {
        if (amountText.all { it.isDigit() || it == '.' }) {
            val parts = amountText.split(".")

            val isValid = when (parts.size) {
                1 -> true
                2 -> parts[1].length <= 2
                else -> false
            }
            if (isValid) {
                updateAmountText(amountText)
            }
        }
    }

    fun updateAmountText(amountText: String) {
        _uiState.update { state ->
            val amount = amountText.toDoubleOrNull()
            val isActive = amount?.let {
                it > 0 && state.fromCurrency != null && state.toCurrency != null
            } ?: false


            state.copy(
                amountText = amountText.trim(),
                isValidConversion = isActive,
                conversionResult = amount?.let { state.conversionRate?.times(it) }
                    ?: state.conversionRate,
            )
        }
    }

    fun updateFromCurrency(currency: Currency) {
        _uiState.update { state ->
            val isActive = state.amountText.toDoubleOrNull()?.let {
                it > 0 && state.toCurrency != null
            } ?: false

            state.copy(
                fromCurrency = currency,
                isValidConversion = isActive
            )
        }
    }

    fun updateToCurrency(currency: Currency) {
        _uiState.update { state ->
            val isActive = state.amountText.toDoubleOrNull()?.let {
                it > 0 && state.fromCurrency != null
            } ?: false

            state.copy(
                toCurrency = currency,
                isValidConversion = isActive
            )
        }
    }

    private fun fetchCurrencyCodes() {
        viewModelScope.launch {
            try {
                val result = repository.getCurrencyCodes()
                result.supportedCodes.map { currencyList ->
                    Currency(
                        name = currencyList[1],
                        code = currencyList.first()
                    )
                }.also { mappedList ->
                    _uiState.update {
                        it.copy(
                            availableCurrencies = mappedList,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }
}