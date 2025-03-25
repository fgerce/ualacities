package com.example.ualachallenge.data.repository

import com.example.ualachallenge.data.remote.CitiesApi
import com.example.ualachallenge.data.remote.NetworkUtils
import com.example.ualachallenge.domain.model.City
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitiesRepository @Inject constructor(
    private val api: CitiesApi,
    private val networkUtils: NetworkUtils
) {
    suspend fun getCities(): Map<Int, City> {
        if(!networkUtils.isNetworkAvailable()) {
            throw NoInternetException("No internet connection")
        }
        return api.getCities().associate { dto ->
            dto._id to City(
                id = dto._id,
                name = dto.name,
                country = dto.country,
                latitude = dto.coord.lat,
                longitude = dto.coord.lon,
            )
        }
    }
}

class NoInternetException(message: String) : IOException(message)
