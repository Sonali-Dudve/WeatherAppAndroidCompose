package com.spdfs.weatherappandroidcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spdfs.weatherappandroidcompose.dao.CityDao
import com.spdfs.weatherappandroidcompose.db.ObjectBox
import com.spdfs.weatherappandroidcompose.ui.SignUpScreen
import com.spdfs.weatherappandroidcompose.ui.theme.WeatherAppAndroidComposeTheme
import com.spdfs.weatherappandroidcompose.ui.theme.splash_gradient_1
import com.spdfs.weatherappandroidcompose.ui.theme.splash_gradient_2
import com.spdfs.weatherappandroidcompose.ui.theme.splash_gradient_3
import io.objectbox.BoxStore

class MainActivity : ComponentActivity() {

    private lateinit var store: BoxStore
    private lateinit var cityDao: CityDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ObjectBox.init(this)
        setContent {
            WeatherAppAndroidComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationGraph(innerPadding)
                }
            }
        }

//        val intent = Intent(this, WeatherAppScreen::class.java)
//        startActivity(intent)

    }
}

@Composable
fun MainScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Greeting("Hello, here you can check the weather of any city")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(title: String, endIconVisible: Boolean, intent: Intent) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(onClick = {

            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            if (endIconVisible)
                IconButton(
                    onClick = {
                        val intent = Intent(context, CityWeatherHistoryScreen()::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Weather info"
                    )
                }
        }

    )
}

@Composable
fun Greeting(msg: String, modifier: Modifier = Modifier) {
    Text(
        text = msg,
        modifier = modifier.padding(10.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WeatherAppAndroidComposeTheme {
        MainScreen(PaddingValues(8.dp))
    }
}