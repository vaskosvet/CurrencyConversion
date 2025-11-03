package com.example.currencyconversion.viewModel

import android.app.Application
import com.example.currencyconversion.data.Repository
import com.example.currencyconversion.data.model.Currency
import com.example.currencyconversion.data.model.CurrencyCodesResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkConstructor
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ViewModelTest {
    private lateinit var viewModel: CurrencyConversionViewModel
    private lateinit var repository: Repository
    private val dispatcher = StandardTestDispatcher()
    private val application = mockk<Application>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        mockkConstructor(Repository::class)
        coEvery { anyConstructed<Repository>().getCurrencyCodes() } returns CurrencyCodesResult(
            "success",
            listOf(listOf("USD", "US Dollar"), listOf("EUR", "Euro"))
        )
        viewModel = CurrencyConversionViewModel(application)
        dispatcher.scheduler.advanceUntilIdle()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCurrencyCodes updates the available currencies`() = runTest {
        val state = viewModel.uiState.value
        assertEquals(2, state.availableCurrencies.size)
    }

    @Test
    fun `onAmountTextChanged updates state correctly`() = runTest {
        viewModel.onAmountTextChanged("123.45")
        val state = viewModel.uiState.first()
        Assert.assertEquals("123.45", state.amountText)
        Assert.assertFalse(state.isValidConversion)
    }

    @Test
    fun `updateFromCurrency sets fromCurrency and updates isValidConversion`() = runTest {
        val currency = Currency("US Dollar", "USD")
        viewModel.updateFromCurrency(currency)
        val state = viewModel.uiState.first()
        Assert.assertEquals(currency, state.fromCurrency)
    }

    @Test
    fun `updateToCurrency sets toCurrency and updates isValidConversion`() = runTest {
        val currency = Currency("Euro", "EUR")
        viewModel.updateToCurrency(currency)
        val state = viewModel.uiState.first()
        Assert.assertEquals(currency, state.toCurrency)
    }
}