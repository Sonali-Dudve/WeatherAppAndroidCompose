package com.spdfs.weatherappandroidcompose

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spdfs.weatherappandroidcompose.dao.CityDao
import com.spdfs.weatherappandroidcompose.dao.WeatherHistoryDao
import com.spdfs.weatherappandroidcompose.db.ObjectBox
import com.spdfs.weatherappandroidcompose.db.WeatherEntity
import com.spdfs.weatherappandroidcompose.ui.components.SelectCityDropdown
import com.spdfs.weatherappandroidcompose.ui.components.WeatherHistoryList


class CityWeatherHistoryScreen : ComponentActivity() {

    private var cities: MutableList<String> = mutableListOf()
    private var weatherHistoryData: List<WeatherEntity> = listOf()
    private lateinit var cityDao: CityDao
    private lateinit var weatherHistoryDao: WeatherHistoryDao


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        cities = mutableListOf("Select City")

        val store = ObjectBox.boxStore
        cityDao = CityDao(store)
        weatherHistoryDao = WeatherHistoryDao(store)

        val allCities = cityDao.getAllCities()
        val intent = Intent(this, WeatherHomeScreen::class.java)

        setContent {

            Scaffold(modifier = Modifier.fillMaxHeight(1f)) { innerPadding ->

                var selectedCity by remember { mutableStateOf(cities.first()) }

                Surface(
                    shape = RectangleShape
                ) {
                    Column {
                        Toolbar("Weather History", false, intent)
                        SelectCityDropdown(
                            cities = allCities,
                            onCitySelected = {
                                selectedCity = it
                                // update history data according to selected city
                                weatherHistoryData = weatherHistoryDao.getWeatherHistoryCities(selectedCity)
                            },
                            selectedCity = selectedCity
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        WeatherHistoryList(weatherHistoryData)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun weatherHistoryData() {
    val weatherHistoryList = mutableListOf<WeatherEntity>()

    weatherHistoryList.add(1, WeatherEntity(1, "Pune", temp = 34.22, date = "12/02/24", dir = 23, speed = 150.2, pressure = 45.4))
    WeatherHistoryList(weatherHistoryList)
}