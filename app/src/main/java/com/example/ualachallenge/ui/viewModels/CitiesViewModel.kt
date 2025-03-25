package com.example.ualachallenge.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ualachallenge.domain.model.City
import com.example.ualachallenge.domain.model.CityWithFavorite
import com.example.ualachallenge.domain.usecase.GetCitiesUseCase
import com.example.ualachallenge.domain.usecase.GetFavoritesIDsUseCase
import com.example.ualachallenge.domain.usecase.SearchCitiesUseCase
import com.example.ualachallenge.domain.usecase.ToggleFavoriteUseCase
import com.example.ualachallenge.ui.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val getFavoritesIDsUseCase: GetFavoritesIDsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val _showFavorites = MutableStateFlow(false)
    val showFavorites: StateFlow<Boolean> = _showFavorites.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity.asStateFlow()

    private val _citiesResult = MutableStateFlow<List<CityWithFavorite>>(emptyList())
    val citiesResult: StateFlow<List<CityWithFavorite>> = _citiesResult.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())

    init {
        loadInitialData()
        setupSearchObserver()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val (cities, favorites) = withContext(Dispatchers.IO) {
                    val citiesDeferred = async { getCitiesUseCase() }
                    val favoritesDeferred = async { getFavoritesIDsUseCase().first() }
                    Pair(citiesDeferred.await(), favoritesDeferred.await())
                }

                withContext(Dispatchers.Default) {
                    searchCitiesUseCase.initializeCities(cities)
                }
                _favoriteIds.value = favorites

                updateCitiesResults("", favorites, false)

                _screenState.value = ScreenState.Success
            } catch (e: Exception) {
                _screenState.value = ScreenState.Error(e.message ?: "Error loading cities")
            }
        }
    }

    private fun setupSearchObserver() {
        viewModelScope.launch {
            combine(
                _searchQuery,
                _showFavorites,
                _favoriteIds,
                ::Triple
            ).collect { (query, showFavorites, favoriteIds) ->
                updateCitiesResults(query, favoriteIds, showFavorites)
            }
        }
    }

    private fun updateCitiesResults(query: String, favoriteIds: Set<Int>, showFavorites: Boolean) {
        val cities = searchCitiesUseCase(query).map { city ->
            CityWithFavorite(city, favoriteIds.contains(city.id))
        }
        _citiesResult.value = if (showFavorites) cities.filter { it.isFavorite } else cities
    }

    fun toggleFavorite(city: City) {
        viewModelScope.launch {
            toggleFavoriteUseCase(city.id)
            _favoriteIds.value = getFavoritesIDsUseCase().first()
        }
    }

    fun toggleShowFavorites() {
        _showFavorites.value = !_showFavorites.value
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCity(city: City?) {
        _selectedCity.value = city
    }
}
