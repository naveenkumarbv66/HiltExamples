package com.naveen.hiltexmaple.api.domain.usecase

import app.cash.turbine.test
import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class UserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userUseCase: UserUseCase

    @Before
    fun setup() {
        userRepository = mockk()
        userUseCase = UserUseCase(userRepository)
    }

    @Test
    fun `getAllUsers should emit loading then success`() = runTest {
        // Given
        val users = listOf(
            User(id = 1, name = "John Doe", email = "john@example.com"),
            User(id = 2, name = "Jane Doe", email = "jane@example.com")
        )
        coEvery { userRepository.getUsers() } returns ApiResult.Success(users)

        // When & Then
        userUseCase.getAllUsers().test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val successResult = awaitItem()
            assertTrue(successResult is ApiResult.Success)
            assertEquals(users, (successResult as ApiResult.Success).data)
        }
    }

    @Test
    fun `getAllUsers should emit loading then error`() = runTest {
        // Given
        coEvery { userRepository.getUsers() } returns ApiResult.Error("Network error", 500)

        // When & Then
        userUseCase.getAllUsers().test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val errorResult = awaitItem()
            assertTrue(errorResult is ApiResult.Error)
            assertEquals("Network error", (errorResult as ApiResult.Error).message)
        }
    }

    @Test
    fun `getUserById should validate ID and emit error for invalid ID`() = runTest {
        // When & Then
        userUseCase.getUserById(0).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val errorResult = awaitItem()
            assertTrue(errorResult is ApiResult.Error)
            assertEquals("Invalid user ID", (errorResult as ApiResult.Error).message)
        }
    }

    @Test
    fun `getUserById should emit success for valid ID`() = runTest {
        // Given
        val user = User(id = 1, name = "John Doe", email = "john@example.com")
        coEvery { userRepository.getUserById(1) } returns ApiResult.Success(user)

        // When & Then
        userUseCase.getUserById(1).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val successResult = awaitItem()
            assertTrue(successResult is ApiResult.Success)
            assertEquals(user, (successResult as ApiResult.Success).data)
        }
    }

    @Test
    fun `createUser should validate name and emit error for empty name`() = runTest {
        // Given
        val user = User(name = "", email = "test@example.com")

        // When & Then
        userUseCase.createUser(user).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val errorResult = awaitItem()
            assertTrue(errorResult is ApiResult.Error)
            assertEquals("User name cannot be empty", (errorResult as ApiResult.Error).message)
        }
    }

    @Test
    fun `createUser should validate email and emit error for invalid email`() = runTest {
        // Given
        val user = User(name = "John Doe", email = "invalid-email")

        // When & Then
        userUseCase.createUser(user).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val errorResult = awaitItem()
            assertTrue(errorResult is ApiResult.Error)
            assertEquals("Invalid email address", (errorResult as ApiResult.Error).message)
        }
    }

    @Test
    fun `createUser should emit success for valid user`() = runTest {
        // Given
        val user = User(name = "John Doe", email = "john@example.com")
        val createdUser = user.copy(id = 1)
        coEvery { userRepository.createUser(user) } returns ApiResult.Success(createdUser)

        // When & Then
        userUseCase.createUser(user).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val successResult = awaitItem()
            assertTrue(successResult is ApiResult.Success)
            assertEquals(createdUser, (successResult as ApiResult.Success).data)
        }
    }

    @Test
    fun `updateUser should validate ID and emit error for invalid ID`() = runTest {
        // Given
        val user = User(name = "John Doe", email = "john@example.com")

        // When & Then
        userUseCase.updateUser(0, user).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val errorResult = awaitItem()
            assertTrue(errorResult is ApiResult.Error)
            assertEquals("Invalid user ID", (errorResult as ApiResult.Error).message)
        }
    }

    @Test
    fun `updateUser should emit success for valid user`() = runTest {
        // Given
        val user = User(id = 1, name = "John Doe", email = "john@example.com")
        coEvery { userRepository.updateUser(1, user) } returns ApiResult.Success(user)

        // When & Then
        userUseCase.updateUser(1, user).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val successResult = awaitItem()
            assertTrue(successResult is ApiResult.Success)
            assertEquals(user, (successResult as ApiResult.Success).data)
        }
    }

    @Test
    fun `deleteUser should validate ID and emit error for invalid ID`() = runTest {
        // When & Then
        userUseCase.deleteUser(0).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val errorResult = awaitItem()
            assertTrue(errorResult is ApiResult.Error)
            assertEquals("Invalid user ID", (errorResult as ApiResult.Error).message)
        }
    }

    @Test
    fun `deleteUser should emit success for valid ID`() = runTest {
        // Given
        coEvery { userRepository.deleteUser(1) } returns ApiResult.Success(Unit)

        // When & Then
        userUseCase.deleteUser(1).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val successResult = awaitItem()
            assertTrue(successResult is ApiResult.Success)
            assertEquals(Unit, (successResult as ApiResult.Success).data)
        }
    }

    @Test
    fun `searchUsers should emit error when no search criteria provided`() = runTest {
        // When & Then
        userUseCase.searchUsers(null, null, 10).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val errorResult = awaitItem()
            assertTrue(errorResult is ApiResult.Error)
            assertEquals("Please provide search criteria", (errorResult as ApiResult.Error).message)
        }
    }

    @Test
    fun `searchUsers should emit error for invalid limit`() = runTest {
        // When & Then
        userUseCase.searchUsers("John", null, 0).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val errorResult = awaitItem()
            assertTrue(errorResult is ApiResult.Error)
            assertEquals("Limit must be between 1 and 100", (errorResult as ApiResult.Error).message)
        }
    }

    @Test
    fun `searchUsers should emit success for valid search`() = runTest {
        // Given
        val users = listOf(User(id = 1, name = "John Doe", email = "john@example.com"))
        coEvery { userRepository.searchUsers("John", null, 10) } returns ApiResult.Success(users)

        // When & Then
        userUseCase.searchUsers("John", null, 10).test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val successResult = awaitItem()
            assertTrue(successResult is ApiResult.Success)
            assertEquals(users, (successResult as ApiResult.Success).data)
        }
    }

    @Test
    fun `getUserStatistics should emit success with calculated statistics`() = runTest {
        // Given
        val users = listOf(
            User(id = 1, name = "John Doe", email = "john@example.com", phone = "123-456-7890"),
            User(id = 2, name = "Jane Doe", email = "jane@example.com", website = "jane.com"),
            User(id = 3, name = "Bob Smith", email = "bob@example.com", phone = "987-654-3210", website = "bob.com")
        )
        coEvery { userRepository.getUsers() } returns ApiResult.Success(users)

        // When & Then
        userUseCase.getUserStatistics().test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val successResult = awaitItem()
            assertTrue(successResult is ApiResult.Success)
            val stats = (successResult as ApiResult.Success).data
            assertEquals(3, stats.totalUsers)
            assertEquals(3, stats.usersWithEmail)
            assertEquals(2, stats.usersWithPhone)
            assertEquals(2, stats.usersWithWebsite)
        }
    }

    @Test
    fun `getUserStatistics should emit error when repository fails`() = runTest {
        // Given
        coEvery { userRepository.getUsers() } returns ApiResult.Error("Repository error", 500)

        // When & Then
        userUseCase.getUserStatistics().test {
            val loadingResult = awaitItem()
            assertTrue(loadingResult is ApiResult.Loading)

            val errorResult = awaitItem()
            assertTrue(errorResult is ApiResult.Error)
            assertEquals("Repository error", (errorResult as ApiResult.Error).message)
        }
    }
}
