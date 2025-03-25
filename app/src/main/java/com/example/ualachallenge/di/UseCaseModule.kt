package com.example.ualachallenge.di

import com.example.ualachallenge.data.local.FavoritesDataStore
import com.example.ualachallenge.data.repository.CitiesRepository
import com.example.ualachallenge.domain.search.CitySearchEngine
import com.example.ualachallenge.domain.usecase.GetCitiesUseCase
import com.example.ualachallenge.domain.usecase.GetFavoritesIDsUseCase
import com.example.ualachallenge.domain.usecase.SearchCitiesUseCase
import com.example.ualachallenge.domain.usecase.ToggleFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCitySearchTrie(): CitySearchEngine {
        return CitySearchEngine()
    }

    @Provides
    @Singleton
    fun provideSearchCitiesUseCase(trie: CitySearchEngine): SearchCitiesUseCase {
        return SearchCitiesUseCase(trie)
    }

    @Provides
    @Singleton
    fun provideGetCitiesUseCase(citiesRepository: CitiesRepository): GetCitiesUseCase {
        return GetCitiesUseCase(citiesRepository)
    }

    @Provides
    @Singleton
    fun provideGetCitiesWithFavoritesUseCase(dataStore: FavoritesDataStore): GetFavoritesIDsUseCase {
        return GetFavoritesIDsUseCase(dataStore)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(dataStore: FavoritesDataStore): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(dataStore)
    }
}
