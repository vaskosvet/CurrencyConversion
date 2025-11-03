package com.example.currencyconversion.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currencyconversion.data.model.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectionDropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedCurrency: Currency?,
    availableCurrencies: List<Currency>,
    label: String,
    onCurrencySelected: (Currency) -> Unit
) {

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        DefaultTextField(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth(),
            value = selectedCurrency?.name ?: "",
            onValueChange = {},
            label = label,
            isForDropdown = true,
            dropdownExpanded = expanded
        )
        ExposedDropdownMenu(
            modifier = Modifier.heightIn(max = 300.dp),
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            availableCurrencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency.code) },
                    onClick = { onCurrencySelected(currency) },
                    trailingIcon = {
                        if (currency == selectedCurrency) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    }
}