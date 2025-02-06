package com.spdfs.weatherappandroidcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class WeatherHomeScreen : ComponentActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

                Scaffold(modifier = Modifier.fillMaxHeight(1f)) { innerPadding ->
                   val weatherViewModel : WeatherViewModel = viewModel()

                    WeatherScreen(weatherViewModel)
                }

        }
    }
}


@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherState by viewModel.weatherState.collectAsState()

    when (weatherState) {
        is WeatherState.Loading -> {

        }
        is WeatherState.Success -> {
            val weatherResponse = (weatherState as WeatherState.Success).weatherResponse
            Log.d("WeatherViewModel", "Weather API Response: $weatherResponse")
            CustomCardLayout(weatherResponse)
        }
        is WeatherState.Error -> {
            val errorMessage = (weatherState as WeatherState.Error).message

        }
    }
}

@Preview
@Composable
fun CustomCardLayout(weatherResponse : WeatherResponse) {

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(16.dp)


        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    contentColor = Color.Blue
                )

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
                            Text(text = weatherResponse.name, style = TextStyle(color = Color.White,fontSize = 36.sp, fontWeight = FontWeight.Bold ))
                            Spacer(modifier = Modifier.height(40.dp))
                            Text(text = weatherResponse.sys.country ,  style = TextStyle(color = Color.Yellow,fontSize = 24.sp, fontWeight = FontWeight.Bold ))
                            Spacer(modifier = Modifier.height(40.dp),)
                            Text(text = weatherResponse.main.temp.toString() +" C ", style = TextStyle(color = Color.Yellow,fontSize = 34.sp, fontWeight = FontWeight.Bold ))
                            Spacer(modifier = Modifier.height(40.dp))
                            Text(text = weatherResponse.weather[0].description, style = TextStyle(color = Color.White,fontSize = 24.sp, fontWeight = FontWeight.Bold ))
                        }
                        Image(
                            painter = painterResource(id = R.drawable.sunrays),
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


        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(16.dp)

        ) {
            // Grid of 4 smaller cards
            Column(
                modifier = Modifier.fillMaxSize() ,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First row of 2 cards
                Row(
                    modifier = Modifier.fillMaxWidth() ,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SmallCard("Wind Speed",R.drawable.windspeed, weatherResponse.wind.speed.toString())
                    SmallCard("WindDirection",R.drawable.wind_dir ,weatherResponse.wind.deg.toString())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SmallCard("Pressure",R.drawable.clock, weatherResponse.main.pressure.toString())
                    SmallCard("Humidity", R.drawable.humidity,weatherResponse.main.humidity.toString())
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
            .height(180.dp),
        elevation =  CardDefaults.cardElevation(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    .size(40.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
            )
            Text(text = description)
             Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge)

        }
    }
}




