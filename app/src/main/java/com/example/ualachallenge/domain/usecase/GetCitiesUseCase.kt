package com.example.ualachallenge.domain.usecase

import com.example.ualachallenge.data.repository.CitiesRepository
import com.example.ualachallenge.domain.model.City

class GetCitiesUseCase(
    private val citiesRepository: CitiesRepository,
) {
    suspend operator fun invoke(): Map<Int, City> {
        return citiesRepository.getCities()
    }
}
