package com.example.ualachallenge.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ualachallenge.domain.model.City
import com.example.ualachallenge.domain.model.CityWithFavorite
import com.example.ualachallenge.domain.usecase.GetCitiesWithFavoritesUseCase
import com.example.ualachallenge.domain.usecase.SearchCitiesUseCase
import com.example.ualachallenge.domain.usecase.ToggleFavoriteUseCase
import com.example.ualachallenge.ui.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val getCitiesWithFavoritesUseCase: GetCitiesWithFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val _showFavorites = MutableStateFlow(false)
    val showFavorites: StateFlow<Boolean> = _showFavorites.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val cities: StateFlow<List<CityWithFavorite>> =
        getCitiesWithFavoritesUseCase()
            .combine(_showFavorites) { cities, showFavorites ->
                if (showFavorites) cities.filter { it.isFavorite } else cities
            }
            .mapLatest { cities ->
                _screenState.value =
                    if (cities.isNotEmpty()) {
                        ScreenState.Success
                    } else {
                        ScreenState.Error("No cities found")
                    }
                cities
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    val searchResults: StateFlow<List<CityWithFavorite>> = _searchQuery
        .debounce(300)
        .combine(getCitiesWithFavoritesUseCase()) { query, citiesWithFavorites ->
            if (query.isBlank()) emptyList()
            else {
                val matchingCities = searchCitiesUseCase(query)
                matchingCities.map { city ->
                    val isFavorite =
                        citiesWithFavorites.any { it.city.id == city.id && it.isFavorite }
                    CityWithFavorite(city, isFavorite)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity.asStateFlow()

    fun toggleFavorite(city: City) {
        viewModelScope.launch {
            toggleFavoriteUseCase(city.id)
        }
    }

    fun setShowFavorites(show: Boolean) {
        _showFavorites.value = show
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCity(city: City?) {
        _selectedCity.value = city
    }
}
