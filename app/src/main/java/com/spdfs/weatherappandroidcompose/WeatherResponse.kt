package com.spdfs.weatherappandroidcompose

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val name: String,
    val sys : Sys

)

data class Main(
    val temp: Double,
    val humidity: Double,
    val pressure: Double

)
data class Wind(
    val speed: Double,
    val deg : Double

)

data class Weather(
    val description: String,
    val icon: String
)
data class Sys(
    val country: String
)

