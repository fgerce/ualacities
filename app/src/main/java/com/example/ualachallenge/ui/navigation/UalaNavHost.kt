package com.example.ualachallenge.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ualachallenge.ui.component.CityDetailsTopBar
import com.example.ualachallenge.ui.screen.CityDetailsScreen
import com.example.ualachallenge.ui.screen.CityListScreen
import com.example.ualachallenge.ui.screen.LandscapeLayout
import com.example.ualachallenge.ui.viewModels.CitiesViewModel

@Composable
fun UalaNavHost(
    navController: NavHostController = rememberNavController(),
    citiesViewModel: CitiesViewModel = hiltViewModel(),
) {

    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }

    val configuration = LocalConfiguration.current

    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeLayout(citiesViewModel)
        }

        else -> {
            NavHost(navController, startDestination = "city_list") {
                composable("city_list") { CityListScreen(navController, citiesViewModel, false) }
                composable("city_details") {
                    Column {
                        CityDetailsTopBar(
                            title = citiesViewModel.selectedCity.collectAsState().value?.name?.uppercase()
                                ?: "NO CITY SELECTED",
                            onBackClick = { navController.popBackStack() }
                        )
                        CityDetailsScreen(citiesViewModel)
                    }
                }
            }
        }
    }
}
