package com.example.ualachallenge.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ualachallenge.domain.model.City
import com.example.ualachallenge.ui.component.CityMap
import com.example.ualachallenge.ui.viewModels.CitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailsScreen(viewModel: CitiesViewModel) {
    val city by viewModel.selectedCity.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(city?.name ?: "Detalles de la ciudad") })
        }
    ) { padding ->
        city.let { city ->
            Column(modifier = Modifier.padding(padding)) {
                if (city != null) {
                    Text(
                        text = "Ubicación: ${city.latitude}, ${city.longitude}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CityMap(lat = city.latitude, lon = city.longitude)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
