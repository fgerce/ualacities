package com.example.ualachallenge.domain.usecase

import com.example.ualachallenge.domain.model.City
import com.example.ualachallenge.domain.search.CitySearchEngine

class SearchCitiesUseCase(private val engine: CitySearchEngine) {

    operator fun invoke(query: String): List<City> {
        return engine.search(query)
    }

    fun initializeCities(cities: Map<Int, City>) {
        engine.initialize(cities)
    }
}
