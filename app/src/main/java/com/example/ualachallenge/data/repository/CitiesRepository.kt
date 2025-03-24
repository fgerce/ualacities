package com.example.ualachallenge.data.repository

import com.example.ualachallenge.data.local.FavoritesDataStore
import com.example.ualachallenge.data.remote.CitiesApi
import com.example.ualachallenge.domain.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitiesRepository @Inject constructor(
    private val api: CitiesApi,
    private val favoritesDataStore: FavoritesDataStore
) {
    fun getCities(): Flow<List<City>> = flow {
        val citiesDto = api.getCities()
        val cities = citiesDto.map { dto ->
            City(
                id = dto._id,
                name = dto.name,
                country = dto.country,
                latitude = dto.coord.lat,
                longitude = dto.coord.lon,
            )
        }
        emit(cities)
    }

    fun getFavoriteCityIds(): Flow<Set<Int>> {
        return favoritesDataStore.getFavoriteCityIds()
    }
}
