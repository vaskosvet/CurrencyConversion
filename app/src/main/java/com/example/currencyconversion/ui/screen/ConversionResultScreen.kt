package com.example.currencyconversion.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconversion.ui.composables.DefaultTextField
import com.example.currencyconversion.viewModel.CurrencyConversionViewModel

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionResultScreen(
    viewModel: CurrencyConversionViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conversion Result") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.amountText,
                onValueChange = { viewModel.onAmountTextChanged(it) },
                label = "Amount",
                placeholder = "Enter Amount"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "From: ",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${uiState.fromCurrency?.code}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    enabled = uiState.isValidConversion,
                    onClick = { viewModel.onSwapCurrenciesClicked() }
                ) {
                    Text("Swap")
                }
                Column {
                    Text(
                        text = "To: ",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${uiState.toCurrency?.code}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (uiState.isValidConversion) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "${uiState.amountText} ${uiState.fromCurrency?.code} = ${
                        String.format(
                            "%.4f",
                            uiState.conversionResult
                        )
                    } ${uiState.toCurrency?.code}"
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "1 ${uiState.fromCurrency?.code} = ${uiState.conversionRate} ${uiState.toCurrency?.code}"
            )
        }
    }
}