package com.spdfs.weatherappandroidcompose.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spdfs.weatherappandroidcompose.R
import com.spdfs.weatherappandroidcompose.ui.theme.splash_gradient_1
import com.spdfs.weatherappandroidcompose.ui.theme.splash_gradient_2
import com.spdfs.weatherappandroidcompose.ui.theme.splash_gradient_3

class SignUpComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    splash_gradient_1,
                                    splash_gradient_2,
                                    splash_gradient_3
                                )
                            )
                        ),
                ) {
                    SignUpScreen()
                }
            }
        }
    }
}


@Composable
fun SignUpScreen() {
    Column(
    ) {
        BackgroundImage()
        Text(text = "Welcome!")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Lets create your account")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Mobile Number")
        Spacer(modifier = Modifier.height(4.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "Enter your mobile number",
            onValueChange = {},
            textStyle = MaterialTheme.typography.bodySmall,
            //leadingIcon = painterResource(R.drawable.ic_mobile_icon)
        )


        Spacer(modifier = Modifier.height(4.dp))
        Button(
            modifier = Modifier.background(Color.White),
            onClick = {}
        ) {
            Text(text = "SEND OTP")
        }
    }
}


@Preview
@Composable
fun SignUpScreenPreview() {
//    Image(
//        painter = painterResource(R.drawable.ic_splash_background),
//        modifier = Modifier.fillMaxSize(),
//        contentDescription = ""
//    )

    SignUpScreen()
}

@Composable
fun BackgroundImage() {
    Image(
        painter = painterResource(R.drawable.sun),
        contentDescription = "",
        modifier = Modifier.fillMaxWidth()
    )
}