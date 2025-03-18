package com.example.ualachallenge.domain.usecase

import com.example.ualachallenge.domain.model.City
import com.example.ualachallenge.domain.search.CitySearchTrie

class SearchCitiesUseCase(private val trie: CitySearchTrie) {
    fun execute(query: String, onlyFavorites: Boolean, favorites: Set<String>): List<City> {
        val filtered = trie.search(query)
        return if (onlyFavorites) filtered.filter { it.id.toString() in favorites } else filtered
    }
}
