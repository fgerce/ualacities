package com.example.ualachallenge.domain.search

import com.example.ualachallenge.domain.model.City

/***
 *
 * # City Search Engine
 *
 * ## Enfoque para resolver el problema de búsqueda
 *
 * Para optimizar la búsqueda dentro de un conjunto de aproximadamente 200,000 ciudades, implementé una estructura `Trie` como motor de búsqueda. Este enfoque penaliza la carga inicial a cambio de ofrecer búsquedas extremadamente rápidas, asegurando una experiencia fluida para el usuario.
 *
 * ### Justificación de la estructura de datos elegida
 *
 * El `Trie` es una estructura de árbol optimizada para búsquedas basadas en prefijos. Su principal ventaja es que permite encontrar coincidencias en **O(M)**, donde **M** es la longitud de la consulta, independientemente del tamaño del conjunto de datos. En contraste, una búsqueda lineal tradicional tendría una complejidad de **O(N)**, donde **N** es el número de ciudades, lo que sería inviable en nuestro caso.
 *
 * En resumen:
 * - **Carga inicial (Construcción del Trie)**: **O(N * M)** (penalización controlada)
 * - **Búsqueda por prefijo**: **O(M)** (independiente de la cantidad total de ciudades)
 *
 * Dado que el desafío no establecía restricciones sobre el tiempo de carga inicial, se optó por maximizar la eficiencia en la búsqueda.
 *
 * ## Optimización de memoria
 *
 * Las primeras implementaciones del `Trie` presentaron problemas de consumo de memoria debido a:
 * 1. **Uso de objetos grandes**: Se estaban utilizando clases de datos personalizadas para representar los nodos del árbol, lo que generaba una alta sobrecarga de memoria.
 * 2. **Uso de tipos no optimizados**: Se empleaban tipos de datos como `Integer` en lugar de tipos primitivos.
 *
 * Para solucionar estos problemas:
 * - Se reemplazaron las estructuras de datos complejas por estructuras más compactas.
 * - Se priorizó el uso de **tipos primitivos** (`Char`, `Boolean`, `Array`) en lugar de objetos innecesarios.
 *
 * Como resultado, la nueva implementación redujo significativamente el consumo de memoria sin afectar la velocidad de búsqueda.
 *
 * ## Conclusión
 *
 * Este enfoque logra un balance óptimo entre carga inicial y velocidad de búsqueda, asegurando que la experiencia del usuario sea fluida incluso con grandes volúmenes de datos. El `Trie` permite buscar eficientemente sin depender del tamaño del dataset, y las optimizaciones en la gestión de memoria evitaron problemas de rendimiento en dispositivos con menos recursos.
 *
 */
class CitySearchEngine {
    private val root = EfficientTrieNode()
    private val cities = mutableMapOf<Int, City>()

    fun initialize(cities: Map<Int, City>) {
        this.cities.clear()
        root.children.clear()
        root.cityIds.clear()
        val sortedCities = cities.values.sortedWith(
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
