package com.example.ualachallenge.domain.usecase

import com.example.ualachallenge.domain.model.City
import com.example.ualachallenge.domain.search.CitySearchTrie

class SearchCitiesUseCase(private val trie: CitySearchTrie) {
    operator fun invoke(query: String): List<City> {
        return trie.search(query)
    }
}
