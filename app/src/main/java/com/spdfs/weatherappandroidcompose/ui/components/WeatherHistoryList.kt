package com.spdfs.weatherappandroidcompose.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spdfs.weatherappandroidcompose.db.CityEntity
import com.spdfs.weatherappandroidcompose.db.WeatherEntity

@Composable
fun SelectCityDropdown(
    cities: List<CityEntity>,
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        TextField(
            value = selectedCity,
            modifier = Modifier
                .fillMaxWidth(1f)
                .clickable(onClick = { expanded = !expanded }),
            onValueChange = {},
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                        contentDescription = "drop down"
                    )
                }
            },
            readOnly = true
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            cities.forEach { cityEntity ->
                DropdownMenuItem(
                    onClick = {
                        cityEntity.cityName?.let { onCitySelected(it) }
                        expanded = false
                    }) {
                    cityEntity.cityName?.let {
                        Text(
                            text = it
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherHistoryList(weatherHistory: List<WeatherEntity>) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(
            items = weatherHistory
        ) {
            WeatherHistoryListItemCard(it)
        }
    }
}