package com.example.ualachallenge.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ualachallenge.ui.component.CityMap
import com.example.ualachallenge.ui.viewModels.CitiesViewModel

@Composable
fun CityDetailsScreen(viewModel: CitiesViewModel) {
    val city by viewModel.selectedCity.collectAsState()
    Scaffold { padding ->
        city.let { city ->
            Column(modifier = Modifier.padding(padding)) {
                if (city != null) {
                    CityMap(lat = city.latitude, lon = city.longitude)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No city selected", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
