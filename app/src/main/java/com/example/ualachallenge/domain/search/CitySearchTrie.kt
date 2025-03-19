package com.example.ualachallenge.domain.search

import com.example.ualachallenge.domain.model.City

/**
 * CitySearchTrie es una implementación de un Trie (árbol de prefijos) para la búsqueda eficiente de ciudades
 * basadas en prefijos. Esta estructura de datos permite realizar búsquedas rápidas por prefijo y organiza las
 * ciudades de manera eficiente, lo que mejora tanto el rendimiento como la experiencia de usuario en escenarios
 * como autocompletado y filtrado de datos.
 *
 * Justificación de la Eficiencia:
 * 1. **Búsquedas Rápidas por Prefijo**: La complejidad de la búsqueda en un Trie es O(P), donde P es la longitud
 *    del prefijo que se está buscando. Esto es significativamente más eficiente que una búsqueda en una lista
 *    de ciudades, que tendría una complejidad O(N), donde N es el número total de ciudades. Esto es crucial cuando
 *    se requiere realizar búsquedas rápidas mientras el usuario escribe, como en un sistema de autocompletado.
 *
 * 2. **Optimización de Espacio**: El Trie organiza las ciudades de forma jerárquica, basándose en los caracteres
 *    de sus nombres. Los nodos que comparten prefijos se almacenan de manera compartida, lo que reduce la redundancia
 *    de datos y optimiza el uso del espacio. Por ejemplo, varias ciudades que comienzan con "B" comparten el mismo
 *    nodo para esa letra, evitando duplicaciones innecesarias.
 *
 * 3. **Ordenación Automática**: Las ciudades recuperadas durante la búsqueda se ordenan automáticamente
 *    sin necesidad de aplicar un algoritmo de ordenación adicional. Esto mejora la experiencia del usuario al
 *    presentar las ciudades de manera ordenada alfabéticamente sin un procesamiento extra.
 *
 * 4. **Escalabilidad**: A medida que se agregan más ciudades, el Trie sigue siendo eficiente. La estructura mantiene
 *    un rendimiento óptimo incluso con grandes cantidades de datos, lo que es ideal para aplicaciones que necesitan
 *    manejar un número elevado de ciudades sin perder rendimiento en las búsquedas.
 */
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
