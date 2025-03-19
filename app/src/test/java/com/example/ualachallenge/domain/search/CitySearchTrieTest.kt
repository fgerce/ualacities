package com.example.ualachallenge.domain.search

import com.example.ualachallenge.domain.model.City
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CitySearchTrieTest {
    private lateinit var sut: CitySearchTrie

    private val city1 = City(1, "Buenos Aires", "Argentina", -34.61, -58.38)
    private val city2 = City(2, "New York", "United States", 40.71, -74.01)
    private val city3 = City(3, "London", "United Kingdom", 51.51, -0.13)
    private val city4 = City(4, "Barcelona", "Spain", 41.38, 2.18)
    private val city5 = City(5, "Bangkok", "Thailand", 13.76, 100.51)

    @Before
    fun setUp() {
        sut = CitySearchTrie()
        sut.initialize(listOf(city1, city2, city3, city4, city5))
    }

    @Test
    fun `test search returns cities sorted alphabetically`() {
        val result = sut.search("b")

        assertEquals("Bangkok", result[0].name)
        assertEquals("Barcelona", result[1].name)
        assertEquals("Buenos Aires", result[2].name)

        assertEquals(result.size, 3)
    }

    @Test
    fun `test search returns empty list when no match`() {
        val result = sut.search("Miami")

        assertEquals(0, result.size)
    }

    @Test
    fun `test search returns cities with exact prefix match`() {
        val result = sut.search("bar")

        assertEquals(1, result.size)
        assertEquals("Barcelona", result[0].name)
    }

    @Test
    fun `test initialize resets previous data`() {
        sut.initialize(listOf(city1))

        val result = sut.search("b")

        assertEquals(1, result.size)
        assertEquals("Buenos Aires", result[0].name)

        sut.initialize(listOf())

        val emptyResult = sut.search("b")

        assertEquals(0, emptyResult.size)
    }

    @Test
    fun `test search returns all cities when empty prefix`() {
        val result = sut.search("")

        assertEquals("Bangkok", result[0].name)
        assertEquals("Barcelona", result[1].name)
        assertEquals("Buenos Aires", result[2].name)
        assertEquals("London", result[3].name)
        assertEquals("New York", result[4].name)
    }

    @Test
    fun `test search returns cities with case insensitivity`() {
        val result = sut.search("BaR")

        assertEquals(1, result.size)
        assertEquals("Barcelona", result[0].name)
    }

    @Test
    fun `test search returns nothing when invalid input`() {
        val result = sut.search("@'  ñ{}")

        assertEquals(0, result.size)
    }


}
