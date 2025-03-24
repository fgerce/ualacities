package com.example.ualachallenge.di

import com.example.ualachallenge.data.local.FavoritesDataStore
import com.example.ualachallenge.data.repository.CitiesRepository
import com.example.ualachallenge.domain.search.CitySearchTrie
import com.example.ualachallenge.domain.usecase.GetCitiesWithFavoritesUseCase
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
    fun provideCitySearchTrie(): CitySearchTrie {
        return CitySearchTrie()
    }

    @Provides
    @Singleton
    fun provideSearchCitiesUseCase(trie: CitySearchTrie): SearchCitiesUseCase {
        return SearchCitiesUseCase(trie)
    }

    @Provides
    @Singleton
    fun provideGetCitiesWithFavoritesUseCase(citiesRepository: CitiesRepository): GetCitiesWithFavoritesUseCase {
        return GetCitiesWithFavoritesUseCase(citiesRepository)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(dataStore: FavoritesDataStore): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(dataStore)
    }
}
