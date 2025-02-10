package com.spdfs.weatherappandroidcompose.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class WeatherEntity(
    @Id(assignable = true)
    var id: Long = 0,
    var city: String,
    var temp: Double,
    var speed: Double,
    var dir: Int,
    var pressure: Double,
    var date: String,
)