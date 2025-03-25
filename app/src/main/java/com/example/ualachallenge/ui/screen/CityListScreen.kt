package com.example.ualachallenge.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ualachallenge.R
import com.example.ualachallenge.ui.component.CityItem
import com.example.ualachallenge.ui.viewModels.CitiesViewModel

@Composable
fun CityListScreen(
    navController: NavController?,
    viewModel: CitiesViewModel,
    isLandscape: Boolean,
) {
    val screenState by viewModel.screenState.collectAsState()
    val cities by viewModel.citiesResult.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCity = viewModel.selectedCity.collectAsState()
    val showFavorites by viewModel.showFavorites.collectAsState()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChanged,
                    placeholder = { Text("Filter") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    enabled = screenState != ScreenState.Loading
                )

                ToggleButton(
                    isSelected = showFavorites,
                    onToggle = { viewModel.toggleShowFavorites() },
                    enabled = screenState != ScreenState.Loading
                )
                Spacer(modifier = Modifier.height(8.dp))
                if(cities.isNotEmpty()) {
                    Text("Results count: ${cities.size}")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                when (val state = screenState) {
                    is ScreenState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }

                    }

                    is ScreenState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
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
                    }

                    is ScreenState.Success -> {
                        Column(modifier = Modifier.fillMaxSize()) {

                            LazyColumn {
                                items(cities, key = { it.city.id }) { cityWithFavorite ->
                                    CityItem(
                                        cityWithFavorite,
                                        onClick = {
                                            viewModel.setSelectedCity(cityWithFavorite.city)
                                            navController?.navigate("city_details")
                                        },
                                        isSelected = isLandscape && cityWithFavorite.city.id == selectedCity.value?.id,
                                        onFavoriteClick = { viewModel.toggleFavorite(it) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ToggleButton(isSelected: Boolean, onToggle: () -> Unit, enabled: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(
            onClick = onToggle,
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 8.dp, horizontal = 16.dp),
            enabled = enabled
        ) {
            Text(
                text = if (isSelected) "Show All" else "Show favorites",
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
