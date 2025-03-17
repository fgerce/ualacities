package com.example.ualachallenge.data.repository

import com.example.ualachallenge.data.remote.CitiesApi
import com.example.ualachallenge.domain.model.City
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitiesRepository @Inject constructor(
    private val api: CitiesApi
) {
    suspend fun fetchCities(): List<City> {
        return api.getCities().map { dto ->
            City(
                id = dto._id,
                name = dto.name,
                country = dto.country,
                latitude = dto.coord.lat,
                longitude = dto.coord.lon
            )
        }
    }
}
