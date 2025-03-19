package com.example.ualachallenge.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ualachallenge.ui.viewModels.CitiesViewModel

@Composable
fun LandscapeLayout(viewModel: CitiesViewModel) {

    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            CityListScreen(
                navController = null,
                viewModel,
                true
            )
        }

        Column(modifier = Modifier.weight(2f)) {
            CityDetailsScreen(viewModel)
        }
    }
}
