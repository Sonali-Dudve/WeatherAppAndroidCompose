package com.spdfs.weatherappandroidcompose.dao

import com.spdfs.weatherappandroidcompose.db.CityEntity
import io.objectbox.BoxStore

class CityDao(private val boxStore: BoxStore) {

    fun saveCity(city: CityEntity) {
        val cityBox = boxStore.boxFor(CityEntity::class.java)
        val alreadyExist = cityBox.all.find { it.cityName == city.cityName }
        if (alreadyExist == null) cityBox.put(city)
    }

    fun getAllCities(): List<CityEntity> {
        return boxStore.boxFor(CityEntity::class.java).all
    }

    fun removeAllCities() {
        return boxStore.boxFor(CityEntity::class.java).removeAll()
    }
}