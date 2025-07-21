package com.spdfs.weatherappandroidcompose

import androidx.navigation.NavController

class Navigator(
    private val navController: NavController
) {

    fun navigate(route: String) {
        navController.navigate(route = route)
    }
}