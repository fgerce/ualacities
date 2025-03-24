package com.example.ualachallenge.domain.usecase

import com.example.ualachallenge.data.repository.CitiesRepository
import com.example.ualachallenge.domain.model.CityWithFavorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCitiesWithFavoritesUseCase(
    private val citiesRepository: CitiesRepository
) {
    operator fun invoke(): Flow<List<CityWithFavorite>> {
        return combine(
            citiesRepository.getCities(),
            citiesRepository.getFavoriteCityIds()
        ) { cities, favoriteIds ->
            cities.map { city ->
                CityWithFavorite(
                    city = city,
                    isFavorite = favoriteIds.contains(city.id)
                )
            }
        }
    }
}
