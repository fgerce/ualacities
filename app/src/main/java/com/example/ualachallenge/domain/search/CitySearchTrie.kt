package com.example.ualachallenge.domain.search

import com.example.ualachallenge.domain.model.City

class CitySearchTrie {
    private val root = TrieNode()

    fun initialize(cities: List<City>) {
        root.children.clear()
        cities.forEach { insert(it) }
    }

    private fun insert(city: City) {
        var node = root
        for (char in city.name.lowercase()) {
            node = node.children.computeIfAbsent(char) { TrieNode() }
        }
        node.cities.add(city)
    }

    fun search(prefix: String): List<City> {
        var node = root
        for (char in prefix.lowercase()) {
            node = node.children[char] ?: return emptyList()
        }
        return collectCities(node)
    }

    private fun collectCities(node: TrieNode): List<City> {
        val result = mutableListOf<City>()
        val stack = ArrayDeque<TrieNode>()
        stack.add(node)

        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            result.addAll(current.cities)
            stack.addAll(current.children.values)
        }

        return result.sortedBy { it.name }
    }

    private class TrieNode {
        val children = mutableMapOf<Char, TrieNode>()
        val cities = mutableListOf<City>()
    }
}
