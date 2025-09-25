package com.naveen.hiltexmaple.api.data.repository

import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.data.remote.ApiService
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setup() {
        apiService = mockk()
        userRepository = UserRepositoryImpl(apiService)
    }

    @Test
    fun `getUsers should return Success when API call succeeds`() = runTest {
        // Given
        val users = listOf(
            User(id = 1, name = "John Doe", email = "john@example.com"),
            User(id = 2, name = "Jane Doe", email = "jane@example.com")
        )
        coEvery { apiService.getUsers() } returns Response.success(users)

        // When
        val result = userRepository.getUsers()

        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(users, (result as ApiResult.Success).data)
    }

    @Test
    fun `getUsers should return Error when API call fails`() = runTest {
        // Given
        coEvery { apiService.getUsers() } returns Response.error(404, "Not Found".toResponseBody("text/plain".toMediaType()))

        // When
        val result = userRepository.getUsers()

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Failed to fetch users"))
        assertEquals(404, result.errorCode)
    }

    @Test
    fun `getUsers should return Error when response body is null`() = runTest {
        // Given
        coEvery { apiService.getUsers() } returns Response.success(null)

        // When
        val result = userRepository.getUsers()

        // Then
        assertTrue(result is ApiResult.Error)
        assertEquals("Empty response body", (result as ApiResult.Error).message)
    }

    @Test
    fun `getUsers should return Error when IOException occurs`() = runTest {
        // Given
        coEvery { apiService.getUsers() } throws IOException("Network error")

        // When
        val result = userRepository.getUsers()

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Network error"))
    }

    @Test
    fun `getUserById should return Success when API call succeeds`() = runTest {
        // Given
        val user = User(id = 1, name = "John Doe", email = "john@example.com")
        coEvery { apiService.getUserById(1) } returns Response.success(user)

        // When
        val result = userRepository.getUserById(1)

        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(user, (result as ApiResult.Success).data)
    }

    @Test
    fun `getUserById should return Error when user not found`() = runTest {
        // Given
        coEvery { apiService.getUserById(999) } returns Response.error(404, "Not Found".toResponseBody("text/plain".toMediaType()))

        // When
        val result = userRepository.getUserById(999)

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Failed to fetch user"))
        assertEquals(404, result.errorCode)
    }

    @Test
    fun `getUserById should return Error when response body is null`() = runTest {
        // Given
        coEvery { apiService.getUserById(1) } returns Response.success(null)

        // When
        val result = userRepository.getUserById(1)

        // Then
        assertTrue(result is ApiResult.Error)
        assertEquals("User not found", (result as ApiResult.Error).message)
    }

    @Test
    fun `createUser should return Success when API call succeeds`() = runTest {
        // Given
        val newUser = User(name = "New User", email = "new@example.com")
        val createdUser = newUser.copy(id = 1)
        coEvery { apiService.createUser(newUser) } returns Response.success(createdUser)

        // When
        val result = userRepository.createUser(newUser)

        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(createdUser, (result as ApiResult.Success).data)
    }

    @Test
    fun `createUser should return Error when API call fails`() = runTest {
        // Given
        val newUser = User(name = "New User", email = "new@example.com")
        coEvery { apiService.createUser(newUser) } returns Response.error(400, "Bad Request".toResponseBody("text/plain".toMediaType()))

        // When
        val result = userRepository.createUser(newUser)

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Failed to create user"))
        assertEquals(400, result.errorCode)
    }

    @Test
    fun `createUser should return Error when response body is null`() = runTest {
        // Given
        val newUser = User(name = "New User", email = "new@example.com")
        coEvery { apiService.createUser(newUser) } returns Response.success(null)

        // When
        val result = userRepository.createUser(newUser)

        // Then
        assertTrue(result is ApiResult.Error)
        assertEquals("Failed to create user", (result as ApiResult.Error).message)
    }

    @Test
    fun `updateUser should return Success when API call succeeds`() = runTest {
        // Given
        val user = User(id = 1, name = "Updated User", email = "updated@example.com")
        coEvery { apiService.updateUser(1, user) } returns Response.success(user)

        // When
        val result = userRepository.updateUser(1, user)

        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(user, (result as ApiResult.Success).data)
    }

    @Test
    fun `updateUser should return Error when API call fails`() = runTest {
        // Given
        val user = User(id = 1, name = "Updated User", email = "updated@example.com")
        coEvery { apiService.updateUser(1, user) } returns Response.error(404, "Not Found".toResponseBody("text/plain".toMediaType()))

        // When
        val result = userRepository.updateUser(1, user)

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Failed to update user"))
        assertEquals(404, result.errorCode)
    }

    @Test
    fun `patchUser should return Success when API call succeeds`() = runTest {
        // Given
        val user = User(id = 1, name = "Patched User", email = "patched@example.com")
        coEvery { apiService.patchUser(1, user) } returns Response.success(user)

        // When
        val result = userRepository.patchUser(1, user)

        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(user, (result as ApiResult.Success).data)
    }

    @Test
    fun `patchUser should return Error when API call fails`() = runTest {
        // Given
        val user = User(id = 1, name = "Patched User", email = "patched@example.com")
        coEvery { apiService.patchUser(1, user) } returns Response.error(422, "Unprocessable Entity".toResponseBody("text/plain".toMediaType()))

        // When
        val result = userRepository.patchUser(1, user)

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Failed to patch user"))
        assertEquals(422, result.errorCode)
    }

    @Test
    fun `deleteUser should return Success when API call succeeds`() = runTest {
        // Given
        coEvery { apiService.deleteUser(1) } returns Response.success(Unit)

        // When
        val result = userRepository.deleteUser(1)

        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(Unit, (result as ApiResult.Success).data)
    }

    @Test
    fun `deleteUser should return Error when API call fails`() = runTest {
        // Given
        coEvery { apiService.deleteUser(1) } returns Response.error(404, "Not Found".toResponseBody("text/plain".toMediaType()))

        // When
        val result = userRepository.deleteUser(1)

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Failed to delete user"))
        assertEquals(404, result.errorCode)
    }

    @Test
    fun `searchUsers should return Success when API call succeeds`() = runTest {
        // Given
        val users = listOf(
            User(id = 1, name = "John Doe", email = "john@example.com")
        )
        coEvery { apiService.searchUsers("John", null, 10) } returns Response.success(users)

        // When
        val result = userRepository.searchUsers("John", null, 10)

        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(users, (result as ApiResult.Success).data)
    }

    @Test
    fun `searchUsers should return Error when no users found`() = runTest {
        // Given
        coEvery { apiService.searchUsers("NonExistent", null, 10) } returns Response.success(null)

        // When
        val result = userRepository.searchUsers("NonExistent", null, 10)

        // Then
        assertTrue(result is ApiResult.Error)
        assertEquals("No users found", (result as ApiResult.Error).message)
    }

    @Test
    fun `searchUsers should return Error when API call fails`() = runTest {
        // Given
        coEvery { apiService.searchUsers("John", null, 10) } returns Response.error(500, "Internal Server Error".toResponseBody("text/plain".toMediaType()))

        // When
        val result = userRepository.searchUsers("John", null, 10)

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Failed to search users"))
        assertEquals(500, result.errorCode)
    }

    @Test
    fun `all methods should handle HttpException properly`() = runTest {
        // Given
        val httpException = HttpException(Response.error<Any>(401, "Unauthorized".toResponseBody("text/plain".toMediaType())))
        coEvery { apiService.getUsers() } throws httpException

        // When
        val result = userRepository.getUsers()

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Unauthorized - Authentication required"))
        assertEquals(401, result.errorCode)
    }

    @Test
    fun `all methods should handle generic exceptions properly`() = runTest {
        // Given
        coEvery { apiService.getUsers() } throws RuntimeException("Unexpected error")

        // When
        val result = userRepository.getUsers()

        // Then
        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.contains("Unknown error occurred"))
    }
}
