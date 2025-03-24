package com.example.ualachallenge.domain.usecase

import com.example.ualachallenge.data.local.FavoritesDataStore

class ToggleFavoriteUseCase(
    private val favoritesDataStore: FavoritesDataStore
) {
    suspend operator fun invoke(cityId: Int) {
        favoritesDataStore.toggleFavorite(cityId)
    }
}
