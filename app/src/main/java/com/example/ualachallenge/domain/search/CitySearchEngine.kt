package com.example.ualachallenge.domain.search

import com.example.ualachallenge.domain.model.City

class CitySearchEngine {
    private val root = EfficientTrieNode()
    private val cities = mutableMapOf<Int, City>()

    fun initialize(cities: Map<Int, City>) {
        this.cities.clear()
        root.children.clear()
        root.cityIds.clear()
        val sortedCities = cities.values.sortedWith (
            compareBy(
                { it.name.lowercase() },
                { it.country.lowercase() }
            )
        )

        sortedCities.forEach { city ->
            this.cities[city.id] = city
            insert(city)
        }
    }

    private fun insert(city: City) {
        val name = city.name.lowercase()
        var node = root

        for (char in name) {
            node = node.children.getOrPut(char) { EfficientTrieNode() }
            node.cityIds.add(city.id)
        }
    }

    fun search(query: String): List<City> {
        if (query.isEmpty()) return cities.values.toList()

        var node = root
        for (char in query.lowercase()) {
            node = node.children[char] ?: return emptyList()
        }

        return node.cityIds.toList().mapNotNull { cities[it] }
    }


    private class EfficientTrieNode {
        val children: CharArrayMap<EfficientTrieNode> = CharArrayMap()
        val cityIds: IntList = IntList()
    }

    private class CharArrayMap<V> {
        private var keys: CharArray = CharArray(2)
        private var values: Array<Any?> = arrayOfNulls(2)
        private var size = 0

        operator fun get(key: Char): V? {
            val index = keys.binarySearch(key, 0, size)
            return if (index >= 0) values[index] as V else null
        }

        fun getOrPut(key: Char, defaultValue: () -> V): V {
            val index = keys.binarySearch(key, 0, size)
            if (index >= 0) return values[index] as V
            insertSorted(-index - 1, key, defaultValue())
            return values[-index - 1] as V
        }

        private fun insertSorted(index: Int, key: Char, value: V) {
            if (size == keys.size) {
                keys = keys.copyOf(size * 2)
                values = values.copyOf(size * 2)
            }
            System.arraycopy(keys, index, keys, index + 1, size - index)
            System.arraycopy(values, index, values, index + 1, size - index)
            keys[index] = key
            values[index] = value
            size++
        }

        fun clear() {
            keys = CharArray(2)
            values = arrayOfNulls(2)
            size = 0
        }
    }

    private class IntList {
        private var array = IntArray(10)
        private var size = 0

        fun add(value: Int) {
            if (size == array.size) {
                array = array.copyOf(size * 2)
            }
            array[size++] = value
        }

        fun clear() {
            array = IntArray(10)
            size = 0
        }

        fun toList(): List<Int> = array.asList().subList(0, size)
    }
}
