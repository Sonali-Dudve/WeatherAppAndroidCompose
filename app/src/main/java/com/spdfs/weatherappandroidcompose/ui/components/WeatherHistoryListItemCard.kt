package com.spdfs.weatherappandroidcompose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spdfs.weatherappandroidcompose.R
import com.spdfs.weatherappandroidcompose.db.WeatherEntity

@Composable
fun WeatherHistoryListItemCard(weatherHistory: WeatherEntity) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        ListItemContent(weatherHistory)
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ListItemContent(weatherHistory: WeatherEntity) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Image(
                painter = painterResource(R.drawable.weather_history_icon),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopStart,
                contentDescription = "icon",
                modifier = Modifier.size(32.dp, 32.dp)
            )

            Text(
                text = "City: ${weatherHistory.city}",
                color = colorResource(R.color.text_gray),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = "${weatherHistory.temp} C",
                color = colorResource(R.color.orange_200),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
            )

        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = "Speed: ${weatherHistory.speed}",
                color = colorResource(R.color.text_gray)
            )

            Text(
                text = "Dir: ${weatherHistory.dir}",
                color = colorResource(R.color.text_gray)
            )
            Text(
                text = "Pres: ${weatherHistory.pressure}",
                color = colorResource(R.color.text_gray),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Record Date: ${weatherHistory.date}",
            color = colorResource(R.color.blue_700),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(8.dp)
                .width(40.dp)
        )
    }
}

