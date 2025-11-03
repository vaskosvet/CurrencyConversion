package com.example.currencyconversion.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currencyconversion.ui.composables.CurrencySelectionDropdown
import com.example.currencyconversion.ui.composables.DefaultTextField
import com.example.currencyconversion.viewModel.CurrencyConversionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectionScreen(
    viewModel: CurrencyConversionViewModel,
    onCurrenciesSelected: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var fromDropdownEnabled by remember { mutableStateOf(false) }
    var toDropdownEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Convert currencies") }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.amountText,
                onValueChange = { viewModel.onAmountTextChanged(it) },
                label = "Amount",
                placeholder = "Enter Amount"
            )

            CurrencySelectionDropdown(
                expanded = fromDropdownEnabled,
                onExpandedChange = { fromDropdownEnabled = it },
                selectedCurrency = uiState.fromCurrency,
                availableCurrencies = uiState.availableCurrencies,
                onCurrencySelected = { currency ->
                    viewModel.updateFromCurrency(currency)
                    fromDropdownEnabled = false
                },
                label = "From"
            )

            CurrencySelectionDropdown(
                expanded = toDropdownEnabled,
                onExpandedChange = { toDropdownEnabled = it },
                selectedCurrency = uiState.toCurrency,
                availableCurrencies = uiState.availableCurrencies,
                onCurrencySelected = { currency ->
                    viewModel.updateToCurrency(currency)
                    toDropdownEnabled = false
                },
                label = "To"
            )

            Button(
                modifier = Modifier.fillMaxWidth(.66f),
                enabled = uiState.isValidConversion,
                onClick = { viewModel.convertCurrency(onCurrenciesSelected) }
            ) {
                Text("Convert")
            }
        }
    }
}