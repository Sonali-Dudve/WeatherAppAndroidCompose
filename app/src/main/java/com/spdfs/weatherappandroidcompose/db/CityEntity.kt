package com.spdfs.weatherappandroidcompose.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class CityEntity(
    @Id(assignable = true)
    var id: Long = 0,

    @Unique
    var cityName: String? = null
)
