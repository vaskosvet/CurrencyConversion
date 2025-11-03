package com.example.currencyconversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconversion.ui.screen.ConversionResultScreen
import com.example.currencyconversion.ui.screen.CurrencySelectionScreen
import com.example.currencyconversion.ui.theme.CurrencyConversionTheme
import com.example.currencyconversion.viewModel.CurrencyConversionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConversionTheme {
                CurrencyConversionApp()
            }
        }
    }
}

@Composable
fun CurrencyConversionApp() {
    val navController = rememberNavController()
    val viewModel: CurrencyConversionViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "currency_selection_screen"
    ) {
        composable("currency_selection_screen") {
            CurrencySelectionScreen(
                viewModel = viewModel,
                onCurrenciesSelected = {
                    navController.navigate("conversion_result_screen")
                }
            )
        }
        composable("conversion_result_screen") {
            ConversionResultScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
