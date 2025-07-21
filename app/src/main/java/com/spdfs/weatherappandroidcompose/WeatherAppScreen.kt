package com.spdfs.weatherappandroidcompose

import SuggestionBottomSheet
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spdfs.weatherappandroidcompose.network.GetLocationData
import com.spdfs.weatherappandroidcompose.network.LocationDataCallback
import com.spdfs.weatherappandroidcompose.ui.theme.WeatherAppAndroidComposeTheme


class WeatherAppScreen : ComponentActivity(), LocationDataCallback {


    private var citySuggestions by mutableStateOf<List<String>>(emptyList())
    private var showBottomSheet by mutableStateOf(false)
    private var isLoading by mutableStateOf(false)
    private var isError by mutableStateOf(false)



    lateinit var locationInfo : List<Array<String>>

    override fun onLocationDataFetched(locationData: List<Array<String>>) {
        isLoading = false
        if(locationData.isNotEmpty()) {
            locationInfo = locationData
            citySuggestions = locationData.map { it[0] }
            showBottomSheet = citySuggestions.isNotEmpty()
        }else {
            isError = true
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            WeatherAppAndroidComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar =
                    { TopAppBar(
                        title = { Text("Best Weather App") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF2196F3) // Blue color
                        ),
//                        navigationIcon = {
//                            IconButton(onClick = { onBackPressed() }) {
//                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                            }
//                        }
                    )}
                    ) { innerPadding ->
                    selectCity()
                }
            }
        }
    }

    @Composable
    fun ErrorDialog() {
            AlertDialog(
                onDismissRequest = {  },
                title = { Text("Invalid Input") },
                text = { Text("The input you provided is incorrect. Please try again.") },
                confirmButton = {
                },
                dismissButton = {
                    TextButton(onClick = { isError = false }) {
                        Text("Dismiss")
                    }
                }
            )
    }



    @Composable
    fun LoadingDialog() {
        AlertDialog(
            onDismissRequest = {  },
            confirmButton = {},
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        )
    }

    @Preview
    @Composable
    fun selectCity() {
        val scrollState = rememberScrollState()

        val context = LocalContext.current


        var city by remember { mutableStateOf("") }
        var selectedCity by remember { mutableStateOf(-1) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
,            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {


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
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
            }
            if (isLoading) {
                LoadingDialog()
            }

            if(isError){
                ErrorDialog()
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Write the name of city which you want to see the weather",
                style = TextStyle(
                    fontSize = 22.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(horizontal = 32.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(100.dp))



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

            Spacer(modifier = Modifier.height(70.dp))

            Button(
                onClick = {
                    isLoading = true
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
                    val intent = Intent(context, WeatherHomeScreen::class.java).apply {
                        putExtra("latitude", locationInfo[selectedCity][1])
                        putExtra("longitude", locationInfo[selectedCity][2])
                    }
                    context.startActivity(intent)
//                    val navigator = Navigator(navController)
//                    navigator.navigate("weather info")
//                    android.util.Log.d("SelectedCity", "${locationInfo[selectedCity][1]} ${locationInfo[selectedCity][2]}")
                            },
                onItemSelected = { city ->
                    selectedCity = city
                }
            )
        }
    }
}
