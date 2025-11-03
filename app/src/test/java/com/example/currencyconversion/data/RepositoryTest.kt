package com.example.currencyconversion.data

import android.content.Context
import android.content.SharedPreferences
import com.example.currencyconversion.data.model.CurrencyCodesResult
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryTest {
    private lateinit var repository: Repository
    private lateinit var mockApi: CurrencyConversionApi
    private lateinit var mockPrefs: SharedPreferences
    private val context = mockk<Context>(relaxed = true)
    private val gson = Gson()

    @Before
    fun setup() {
        mockApi = mockk()
        mockPrefs = mockk(relaxed = true)

        every { context.getSharedPreferences(any(), any()) } returns mockPrefs
        mockkObject(RetrofitInstance)
        every { RetrofitInstance.api } returns mockApi

        repository = Repository(context)
    }

    @Test
    fun `getCurrencyCodes fetches from API and caches result`() = runBlocking {
        val apiResult = CurrencyCodesResult(
            "success",
            listOf(listOf("USD", "US Dollar"))
        )
        coEvery { mockApi.getCurrencyCodes() } returns apiResult
        every { mockPrefs.getString(any(), any()) } returns null

        val result = repository.getCurrencyCodes()

        assertEquals("success", result.result)
        assertEquals("USD", result.supportedCodes.first().first())
    }

    @Test
    fun `getCachedCurrencyCodes returns cached data`() {
        val cached = CurrencyCodesResult("success", listOf(listOf("EUR", "Euro")))
        every { mockPrefs.getString(any(), any()) } returns gson.toJson(cached)

        val result = repository.getCachedCurrencyCodes()

        assertNotNull(result)
        assertEquals("EUR", result?.supportedCodes?.first()?.first())
    }
}