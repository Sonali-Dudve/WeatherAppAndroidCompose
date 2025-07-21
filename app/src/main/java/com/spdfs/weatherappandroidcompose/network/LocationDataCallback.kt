package com.spdfs.weatherappandroidcompose.network

interface LocationDataCallback {
    fun onLocationDataFetched(locationData: List<Array<String>>)
}
