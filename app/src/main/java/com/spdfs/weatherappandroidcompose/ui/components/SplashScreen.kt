package com.spdfs.weatherappandroidcompose.ui.components

import android.content.Intent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spdfs.weatherappandroidcompose.Navigator
import com.spdfs.weatherappandroidcompose.R
import com.spdfs.weatherappandroidcompose.WeatherAppScreen

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = 24.dp, bottom = 36.dp)
            .background(colorResource(R.color.white))
            .border(6.dp, Color.Black, shape = RectangleShape)
            .fillMaxSize(1f),
        contentAlignment = Alignment.TopStart
    ) {
        AnimatedSplashScreen(navController = navController)
    }
}

@Composable
fun AnimatedSplashScreen(navController: NavHostController) {
    var scale by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        animate(0f, 1f, 0f, animationSpec = tween(2000, easing = LinearOutSlowInEasing)) { value, _ ->
            scale = value
        }
    }

    SplashScreenContent(navController = navController, scale = scale)
}

@Composable
fun SplashScreenContent(navController: NavHostController, scale: Float) {
    val context = LocalContext.current


    Column(
        modifier = Modifier.scale(scale)
    ) {
        BackgroundImage()
        WeatherInformationText()
        Spacer(modifier = Modifier.height(10.dp))
        ElevatedButton(
            onClick = {
                val navigator = Navigator(navController)
                navController.popBackStack()
                navigator.navigate("main screen")

                val intent = Intent(context, WeatherAppScreen::class.java)
                context.startActivity(intent)

            },
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 10.dp)
                .align(Alignment.CenterHorizontally)
                .size(220.dp, 50.dp),
            colors = ButtonColors(
                containerColor = Color.Yellow,
                contentColor = colorResource(R.color.purple_700),
                disabledContentColor = Color.Black,
                disabledContainerColor = Color.Green
            ),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp, Color.Yellow)
        ) {
            Text(
                text = "Get Started",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun WeatherInformationText() {
    Text(
        text = "Find the weather in your City!",
        color = colorResource(R.color.blue_700),
        style = MaterialTheme.typography.displaySmall,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(10.dp, 10.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = "It's pleasure to keep update about current weather",
        color = colorResource(R.color.text_gray),
        style = MaterialTheme.typography.titleMedium,
        fontFamily = FontFamily.SansSerif,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(12.dp, 12.dp)
    )
}

@Composable
fun BackgroundImage() {
    Image(
        painter = painterResource(R.drawable.splash_logo),
        modifier = Modifier.size(500.dp, 350.dp),
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        contentDescription = "logo",
    )
}