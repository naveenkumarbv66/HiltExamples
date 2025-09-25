package com.naveen.hiltexmaple.dataClass

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataClassModule {

    @Provides
    @Singleton
    fun providePerson(): Person {
        return Person("Naveen", "Bangalore")
    }

}