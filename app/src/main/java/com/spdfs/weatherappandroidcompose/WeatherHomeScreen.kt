package com.spdfs.weatherappandroidcompose

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spdfs.weatherappandroidcompose.dao.CityDao
import com.spdfs.weatherappandroidcompose.dao.WeatherHistoryDao
import com.spdfs.weatherappandroidcompose.db.CityEntity
import com.spdfs.weatherappandroidcompose.db.ObjectBox
import com.spdfs.weatherappandroidcompose.db.WeatherEntity
import com.spdfs.weatherappandroidcompose.utils.DateUtil

class WeatherHomeScreen : ComponentActivity(){

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Weather Info", style = TextStyle(color = Color.Black,fontSize = 20.sp, fontWeight = FontWeight.Bold )) },
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        navigationIcon = {
                            IconButton(onClick = {
                                val intent = Intent(this
                                    , WeatherAppScreen::class.java)
                                this.startActivity(intent)
                            }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                val intent = Intent(context, CityWeatherHistoryScreen::class.java)
                                context.startActivity(intent)

                                /* Handle right icon action */ }) {
                                Icon(Icons.Filled.Search, contentDescription = "Settings")
                            }
                        },
                        modifier = Modifier.height(80.dp)  // Adjust height to make it broader
                    )
                },
                content = { paddingValues ->
                    // Apply padding to start the UI below the toolbar
                    Column(modifier = Modifier.padding(paddingValues)) {
                        val weatherViewModel : WeatherViewModel = viewModel()
                        weatherViewModel.getWeatherData(LocationInfo.latitude!!.toDouble(), LocationInfo.longitude!!.toDouble())
                        WeatherScreen(weatherViewModel)
                    }
                }
            )
        }






        }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherState by viewModel.weatherState.collectAsState()
    val boxStore = ObjectBox.boxStore
    val cityDao = CityDao(boxStore)
    val weatherHistoryDao = WeatherHistoryDao(boxStore)


    when (weatherState) {
        is WeatherState.Loading -> {

        }
        is WeatherState.Success -> {
            val weatherResponse = (weatherState as WeatherState.Success).weatherResponse
            Log.d("WeatherViewModel", "Weather API Response: $weatherResponse")
            cityDao.saveCity(CityEntity(cityName = weatherResponse.name))
            weatherHistoryDao.saveWeatherHistory(
                WeatherEntity(
                    city = weatherResponse.name,
                    temp = weatherResponse.main.temp,
                    dir = weatherResponse.wind.deg.toInt(),
                    speed = weatherResponse.wind.speed,
                    pressure = weatherResponse.main.pressure,
                    date = DateUtil.getCurrentTime()
                )
            )
            CustomCardLayout(weatherResponse)
        }
        is WeatherState.Error -> {
            val errorMessage = (weatherState as WeatherState.Error).message

        }
    }
}


@Preview
@Composable
fun CustomCardLayout(weatherResponse: WeatherResponse) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(contentColor = Color.Blue)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.dark_blue)),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .weight(1f),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = weatherResponse.name,
                            style = TextStyle(color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = weatherResponse.sys.country,
                            style = TextStyle(color = colorResource(id = R.color.text_yellow), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = weatherResponse.main.temp.toString() + " C ",
                            style = TextStyle(color = colorResource(id = R.color.text_yellow), fontSize = 34.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = weatherResponse.weather[0].description,
                            style = TextStyle(color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.sun),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(100.dp)
                            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                    )
                }
            }
        }

        // Second box that holds the smaller cards
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Grid of smaller cards
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First row of 2 cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SmallCard("Wind Speed", R.drawable.windspeed, weatherResponse.wind.speed.toString())
                    SmallCard("Wind Direction", R.drawable.wind_dir, weatherResponse.wind.deg.toString())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SmallCard("Pressure", R.drawable.clock, weatherResponse.main.pressure.toString())
                    SmallCard("Humidity", R.drawable.humidity, weatherResponse.main.humidity.toString())
                }
            }
        }
    }
}





@Composable
fun SmallCard(title: String,imageResorceId: Int, description: String) {
    Card(
        modifier = Modifier
            .width(180.dp)  // Make the cards a bit smaller
            .height(180.dp)

           ,
        elevation =  CardDefaults.cardElevation(20.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
                .padding(16.dp)

            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageResorceId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
            )

            Text(
                text = description,
                style = TextStyle(color = colorResource(id = R.color.text_yellow), fontSize = 34.sp, fontWeight = FontWeight.Bold)
            )
             Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = TextStyle(color = Color.Blue, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )


        }
    }
}




