package com.example.ualachallenge.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ualachallenge.ui.component.CityItem
import com.example.ualachallenge.ui.viewModels.CityListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(navController: NavController, viewModel: CityListViewModel = hiltViewModel()) {

    val cities by viewModel.cities.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ciudades") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Buscar ciudad") },
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn {
                items(cities) { city ->
                    CityItem(city, onClick = {
                        navController.navigate("city_details/${city.id}")
                    })
                }
            }
        }
    }
}
