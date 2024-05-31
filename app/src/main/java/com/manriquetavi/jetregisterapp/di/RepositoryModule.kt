package com.manriquetavi.jetregisterapp.di

import com.manriquetavi.jetregisterapp.data.repository.FirestoreRepositoryImpl
import com.manriquetavi.jetregisterapp.domain.repository.FirestoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        firestoreRepositoryImpl: FirestoreRepositoryImpl
    ): FirestoreRepository
}