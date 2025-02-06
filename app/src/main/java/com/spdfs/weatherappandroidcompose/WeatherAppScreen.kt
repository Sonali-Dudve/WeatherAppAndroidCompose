package com.spdfs.weatherappandroidcompose

import SuggestionBottomSheet
import android.os.Bundle
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spdfs.weatherappandroidcompose.network.GetLocationData
import com.spdfs.weatherappandroidcompose.network.LocationDataCallback
import com.spdfs.weatherappandroidcompose.ui.theme.WeatherAppAndroidComposeTheme


class WeatherAppScreen : ComponentActivity(), LocationDataCallback {


    private var citySuggestions by mutableStateOf<List<String>>(emptyList())
    private var showBottomSheet by mutableStateOf(false)

    lateinit var locationInfo : List<Array<String>>

    override fun onLocationDataFetched(locationData: List<Array<String>>) {
        locationInfo = locationData
        citySuggestions = locationData.map { it[0] }
        showBottomSheet = citySuggestions.isNotEmpty() 
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppAndroidComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    selectCity()
                }
            }
        }
    }


    @Preview
    @Composable
    fun selectCity() {

        var city by remember { mutableStateOf("") }
        var selectedCity by remember { mutableStateOf(-1) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Handle Back Navigation */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ic),
                        contentDescription = "Back Button"
                    )
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Choose a City",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
            }


            Spacer(modifier = Modifier.fillMaxHeight(0.1f))

            Text(
                text = "Write the name of city which you want to see the weather",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(horizontal = 32.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.3f))

            Image(
                painter = painterResource(id = R.drawable.weather_ic),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .fillMaxWidth(0.4f)
            )

            Spacer(modifier = Modifier.height(60.dp))

            BasicTextField(
                value = city,
                onValueChange = { city = it },
                textStyle = TextStyle(
                    fontSize = 22.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.3f))

            Button(
                onClick = {
                    GetLocationData(this@WeatherAppScreen).execute(city)
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Submit",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            SuggestionBottomSheet(
                isVisible = showBottomSheet,
                suggestions = citySuggestions,
                onDismiss = {
                    showBottomSheet = false
                    android.util.Log.d("SelectedCity", "${locationInfo[selectedCity][1]} ${locationInfo[selectedCity][2]}")
                            },
                onItemSelected = { city ->
                    selectedCity = city
                }
            )
        }
    }
}
