package com.example.ualachallenge.domain.usecase

import com.example.ualachallenge.data.local.FavoritesDataStore
import kotlinx.coroutines.flow.Flow

class GetFavoritesIDsUseCase(
    private val favoritesDataStore: FavoritesDataStore
) {

    operator fun invoke(): Flow<Set<Int>> {
        return favoritesDataStore.getFavoriteCityIds()
    }
}
