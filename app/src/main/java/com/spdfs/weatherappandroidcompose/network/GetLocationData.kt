package com.spdfs.weatherappandroidcompose.network

import android.os.AsyncTask
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class GetLocationData(private val callback: LocationDataCallback) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String {
        val city = params[0]
        val apiUrl = "https://nominatim.openstreetmap.org/search?q=$city&format=json"
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val inputStream = connection.inputStream
        return inputStream.bufferedReader().use { it.readText() }
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        parseJsonResponse(result)
    }

    private fun parseJsonResponse(response: String) {
        try {
            val jsonArray = JSONArray(response)

            val locationData = mutableListOf<Array<String>>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                val name = jsonObject.getString("display_name")
                val lat = jsonObject.getString("lat")
                val lon = jsonObject.getString("lon")

                android.util.Log.d("TESTAPI","Name: $name, Lat: $lat, Lon: $lon")
                locationData.add(arrayOf("$name", "$lat", "$lon"))
            }
            callback.onLocationDataFetched(locationData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
