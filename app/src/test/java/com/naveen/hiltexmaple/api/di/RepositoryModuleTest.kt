package com.naveen.hiltexmaple.api.di

import com.naveen.hiltexmaple.api.data.repository.UserRepositoryImpl
import com.naveen.hiltexmaple.api.domain.repository.UserRepository
import io.mockk.mockk
import org.junit.Test
import org.junit.Assert.*

class RepositoryModuleTest {

    @Test
    fun `UserRepositoryImpl should implement UserRepository interface`() {
        // Given
        val mockApiService = mockk<com.naveen.hiltexmaple.api.data.remote.ApiService>()
        val userRepository = UserRepositoryImpl(mockApiService)

        // Then
        assertNotNull(userRepository)
        assertTrue(userRepository is UserRepository)
        assertTrue(userRepository is UserRepositoryImpl)
    }

    @Test
    fun `UserRepositoryImpl should be instantiable`() {
        // Given
        val mockApiService = mockk<com.naveen.hiltexmaple.api.data.remote.ApiService>()

        // When
        val userRepository = UserRepositoryImpl(mockApiService)

        // Then
        assertNotNull(userRepository)
    }
}
