package com.example.ualachallenge.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ualachallenge.data.repository.CitiesRepository
import com.example.ualachallenge.domain.model.City
import com.example.ualachallenge.domain.search.CitySearchTrie
import com.example.ualachallenge.domain.usecase.SearchCitiesUseCase
import com.example.ualachallenge.ui.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val citiesSearchTrie: CitySearchTrie
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showFavorites = MutableStateFlow(false)
    val showFavorites: StateFlow<Boolean> = _showFavorites.asStateFlow()

    private val _favorites = MutableStateFlow(setOf<String>())

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            citiesSearchTrie.initialize(citiesRepository.fetchCities())
            _cities.value = searchCitiesUseCase.execute(_searchQuery.value, _showFavorites.value, _favorites.value)
            _screenState.value = ScreenState.Success
        }
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading
            _searchQuery.value = query
            _cities.value = searchCitiesUseCase.execute(query, _showFavorites.value, setOf())
            _screenState.value = ScreenState.Success
        }
    }

    fun toggleShowFavorites() {
        _showFavorites.value = !_showFavorites.value
        onSearchQueryChanged(_searchQuery.value)
    }
}
