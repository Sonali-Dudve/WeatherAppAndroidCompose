package com.spdfs.weatherappandroidcompose.dao

import com.spdfs.weatherappandroidcompose.db.WeatherEntity
import com.spdfs.weatherappandroidcompose.db.WeatherEntity_
import io.objectbox.BoxStore

class WeatherHistoryDao(private val boxStore: BoxStore) {

    fun saveWeatherHistory(weatherHistory: WeatherEntity) {
        boxStore.boxFor(WeatherEntity::class.java).put(weatherHistory)
    }

    fun getWeatherHistoryCities(city: String): List<WeatherEntity> {
        val weatherBox = boxStore.boxFor(WeatherEntity::class.java)
        return weatherBox.query().orderDesc(WeatherEntity_.__ID_PROPERTY).build().find().filter { it.city == city }
    }

    fun removeAllHistory() {
        val weatherBox = boxStore.boxFor(WeatherEntity::class.java)
        return weatherBox.removeAll()
    }
}