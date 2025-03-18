package com.example.ualachallenge.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ualachallenge.R
import com.example.ualachallenge.ui.component.CityItem
import com.example.ualachallenge.ui.viewModels.CitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(navController: NavController?, viewModel: CitiesViewModel) {
    val screenState by viewModel.screenState.collectAsState()
    val cities by viewModel.cities.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ciudades") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = screenState) {
                is ScreenState.Loading -> {
                    CircularProgressIndicator()
                }
                is ScreenState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_error),
                            contentDescription = stringResource(R.string.error_icon),
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.message,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                is ScreenState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TextField(
                            value = searchQuery,
                            onValueChange = viewModel::onSearchQueryChanged,
                            label = { Text("Buscar ciudad") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(cities) { city ->
                                CityItem(city, onClick = {
                                    viewModel.onSelectCity(city)
                                    navController?.navigate("city_details")
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}
