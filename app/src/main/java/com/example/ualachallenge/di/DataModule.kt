package com.example.ualachallenge.di

import android.content.Context
import com.example.ualachallenge.data.local.FavoritesDataStore
import com.example.ualachallenge.data.remote.CitiesApi
import com.example.ualachallenge.data.repository.CitiesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): FavoritesDataStore {
        return FavoritesDataStore(context)
    }

    @Provides
    @Singleton
    fun provideCitiesRepository(api: CitiesApi, dataStore: FavoritesDataStore): CitiesRepository {
        return CitiesRepository(api, dataStore)
    }
}
