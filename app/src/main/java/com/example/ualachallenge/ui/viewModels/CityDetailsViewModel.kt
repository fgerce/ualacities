package com.example.ualachallenge.ui.viewModels

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ualachallenge.data.repository.CitiesRepository
import com.example.ualachallenge.domain.model.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CityDetailsViewModel @Inject constructor(
    private val repository: CitiesRepository,
) : ViewModel() {

    private val _city = MutableStateFlow<City?>(null)
    val city: StateFlow<City?> = _city.asStateFlow()

    fun loadCity(cityId: String) {
        viewModelScope.launch {
            val cities = repository.fetchCities()
            _city.value = cities.find { it.id.toString() == cityId }
        }
    }
}
