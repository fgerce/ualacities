package com.example.ualachallenge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ualachallenge.ui.screen.CityDetailsScreen
import com.example.ualachallenge.ui.screen.CityListScreen

@Composable
fun UalaNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "city_list") {
        composable("city_list") { CityListScreen(navController) }
        composable("city_details/{cityId}") { backStackEntry ->
            val cityId = backStackEntry.arguments?.getString("cityId")
            CityDetailsScreen(cityId)
        }
    }
}
