package com.example.ualachallenge.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "favorites")

class FavoritesDataStore(private val context: Context) {

    private val FAVORITES_KEY = stringSetPreferencesKey("favorite_cities_ids")

    fun getFavoriteCityIds(): Flow<Set<Int>> {
        return context.dataStore.data.map { preferences ->
            preferences[FAVORITES_KEY]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        }
    }

    suspend fun toggleFavorite(cityId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            val updatedFavorites = if (currentFavorites.contains(cityId.toString())) {
                currentFavorites - cityId.toString()
            } else {
                currentFavorites + cityId.toString()
            }
            preferences[FAVORITES_KEY] = updatedFavorites
        }
    }
}
