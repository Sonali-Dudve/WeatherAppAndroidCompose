package com.spdfs.weatherappandroidcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private val apiKey = "0eeb4ce49778178c6780f5748911754d"


    fun getWeatherData( lat : Double,  lon : Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeather(
                    lat = lat,
                    lon = lon,
                    apiKey = apiKey
                )
                _weatherState.value = WeatherState.Success(response)
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }
}

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weatherResponse: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}
