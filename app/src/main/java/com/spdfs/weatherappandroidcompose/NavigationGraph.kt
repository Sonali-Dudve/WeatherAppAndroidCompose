package com.spdfs.weatherappandroidcompose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spdfs.weatherappandroidcompose.ui.components.SplashScreen

@Composable
fun NavigationGraph(innerPadding: PaddingValues) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash screen") {
        composable(route = "splash screen") {
            SplashScreen(navController = navController) // splash screen
        }

        composable(route = "main screen") {
            MainScreen(innerPadding = innerPadding) // main screen
        }
    }
}