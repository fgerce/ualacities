package com.example.ualachallenge.domain.search

import com.example.ualachallenge.domain.model.City
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CitySearchTrieTest {
    private lateinit var sut: CitySearchEngine

    private val city1 = City(1, "Buenos Aires", "Argentina", -34.61, -58.38)
    private val city2 = City(2, "New York", "United States", 40.71, -74.01)
    private val city3 = City(3, "London", "United Kingdom", 51.51, -0.13)
    private val city4 = City(4, "Barcelona", "Spain", 41.38, 2.18)
    private val city5 = City(5, "Bangkok", "Thailand", 13.76, 100.51)
    private val city6 = City(6, "Valencia", "Spain", 39.47, -0.38)
    private val city7 = City(7, "Valencia", "Colombia", 8.25, -76.15)

    @Before
    fun setUp() {
        sut = CitySearchEngine()
        sut.initialize(
            mapOf(
                city1.id to city1,
                city2.id to city2,
                city3.id to city3,
                city4.id to city4,
                city5.id to city5,
                city6.id to city6,
                city7.id to city7
            )
        )
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
    fun `test search returns cities sorted alphabetically by city and by country`() {
        val result = sut.search("v")

        assertEquals("Valencia", result[0].name)
        assertEquals("Valencia", result[1].name)

        assertEquals("Colombia", result[0].country)
        assertEquals("Spain", result[1].country)

        assertEquals(result.size, 2)
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
        sut.initialize(
            mapOf(
                city1.id to city1
            )
        )

        val result = sut.search("b")
        assertEquals(1, result.size)
        assertEquals("Buenos Aires", result[0].name)

        sut.initialize(mapOf())

        val emptyResult = sut.search("b")

        assertEquals(0, emptyResult.size)
    }

    @Test
    fun `test search returns all cities when empty prefix`() {
        val result = sut.search("")

        assertEquals(7, result.size)
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

    @Test
    fun `test search handles cities with special characters`() {
        val city1 = City(1, "San José", "Costa Rica", 9.93, -84.08)
        val city2 = City(2, "São Paulo", "Brazil", -23.55, -46.63)
        val city3 = City(3, "Los Angeles", "United States", 34.05, -118.24)

        sut.initialize(
            mapOf(
                city1.id to city1,
                city2.id to city2,
                city3.id to city3
            )
        )

        val result = sut.search("s")

        assertEquals("San José", result[0].name)
        assertEquals("São Paulo", result[1].name)
    }

    @Test
    fun `test search handles cities with prefix names`() {
        val city1 = City(1, "San", "Spain", 40.42, -3.70)
        val city2 = City(2, "San Francisco", "United States", 37.77, -122.42)
        sut.initialize(
            mapOf(
                city1.id to city1,
                city2.id to city2
            )
        )
        val result = sut.search("san")

        assertEquals(2, result.size)
        assertEquals("San", result[0].name)
        assertEquals("San Francisco", result[1].name)
    }

    @Test
    fun `test search handles case insensitivity`() {
        val city1 = City(1, "PARIS", "France", 48.86, 2.35)
        val city2 = City(2, "paris", "United States", 33.66, -95.55)
        sut.initialize(
            mapOf(
                city1.id to city1,
                city2.id to city2
            )
        )
        val result = sut.search("par")

        assertEquals(2, result.size)
        assertEquals("France", result[0].country)
        assertEquals("United States", result[1].country)
    }

    @Test
    fun `test search handles numeric city names`() {
        val city1 = City(18, "123", "Fictional Country", 0.0, 0.0)
        sut.initialize(
            mapOf(
                city1.id to city1
            )
        )
        val result = sut.search("12")

        assertEquals(1, result.size)
        assertEquals("123", result[0].name)
    }

    @Test
    fun `test search handles very long city names`() {
        val city1 = City(
            19,
            "Llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch",
            "Wales",
            53.22,
            -4.20
        )
        sut.initialize(
            mapOf(
                city1.id to city1
            )
        )
        val result = sut.search("llan")

        assertEquals(1, result.size)
        assertEquals("Llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch", result[0].name)
    }

    @Test
    fun `test search handles cities with substring names`() {
        val city1 = City(20, "Rio", "Brazil", -22.91, -43.21)
        val city2 = City(21, "Rio de Janeiro", "Brazil", -22.91, -43.21)
        sut.initialize(
            mapOf(
                city1.id to city1,
                city2.id to city2
            )
        )
        val result = sut.search("rio")

        assertEquals(2, result.size)
        assertEquals("Rio", result[0].name)
        assertEquals("Rio de Janeiro", result[1].name)
    }
}
