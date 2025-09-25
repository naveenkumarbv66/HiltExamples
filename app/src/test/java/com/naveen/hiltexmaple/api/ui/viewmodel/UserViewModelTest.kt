package com.naveen.hiltexmaple.api.ui.viewmodel

import app.cash.turbine.test
import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.model.UiState
import com.naveen.hiltexmaple.api.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mockk()
        userViewModel = UserViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUsers should emit loading then success state`() = runTest {
        // Given
        val users = listOf(
            User(id = 1, name = "John Doe", email = "john@example.com"),
            User(id = 2, name = "Jane Doe", email = "jane@example.com")
        )
        coEvery { userRepository.getUsers() } returns ApiResult.Success(users)

        // When & Then
        userViewModel.usersState.test {
            // Initial state
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertNull(initialState.data)
            assertNull(initialState.error)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(users, successState.data)
            assertNull(successState.error)
        }
    }

    @Test
    fun `loadUsers should emit loading then error state`() = runTest {
        // Given
        coEvery { userRepository.getUsers() } returns ApiResult.Error("Network error", 500)

        // When & Then
        userViewModel.usersState.test {
            // Initial state
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertNull(initialState.data)
            assertNull(initialState.error)

            // Error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.data)
            assertEquals("Network error", errorState.error)
            assertEquals(500, errorState.errorCode)
        }
    }

    @Test
    fun `getUserById should emit loading then success state`() = runTest {
        // Given
        val user = User(id = 1, name = "John Doe", email = "john@example.com")
        coEvery { userRepository.getUserById(1) } returns ApiResult.Success(user)

        // When
        userViewModel.getUserById(1)

        // Then
        userViewModel.userState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertNull(loadingState.data)
            assertNull(loadingState.error)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(user, successState.data)
            assertNull(successState.error)
        }
    }

    @Test
    fun `getUserById should emit loading then error state`() = runTest {
        // Given
        coEvery { userRepository.getUserById(1) } returns ApiResult.Error("User not found", 404)

        // When
        userViewModel.getUserById(1)

        // Then
        userViewModel.userState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.data)
            assertEquals("User not found", errorState.error)
            assertEquals(404, errorState.errorCode)
        }
    }

    @Test
    fun `createUser should emit loading then success state and refresh users list`() = runTest {
        // Given
        val newUser = User(name = "New User", email = "new@example.com")
        val createdUser = newUser.copy(id = 1)
        val users = listOf(createdUser)
        
        coEvery { userRepository.createUser(newUser) } returns ApiResult.Success(createdUser)
        coEvery { userRepository.getUsers() } returns ApiResult.Success(users)

        // When
        userViewModel.createUser(newUser)

        // Then
        userViewModel.createUserState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(createdUser, successState.data)
            assertNull(successState.error)
        }

        // Verify users list was refreshed
        userViewModel.usersState.test {
            // Skip initial state
            awaitItem()
            // Success state with refreshed data
            val successState = awaitItem()
            assertEquals(users, successState.data)
        }
    }

    @Test
    fun `createUser should emit loading then error state`() = runTest {
        // Given
        val newUser = User(name = "New User", email = "new@example.com")
        coEvery { userRepository.createUser(newUser) } returns ApiResult.Error("Validation failed", 422)

        // When
        userViewModel.createUser(newUser)

        // Then
        userViewModel.createUserState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.data)
            assertEquals("Validation failed", errorState.error)
            assertEquals(422, errorState.errorCode)
        }
    }

    @Test
    fun `updateUser should emit loading then success state and refresh users list`() = runTest {
        // Given
        val user = User(id = 1, name = "Updated User", email = "updated@example.com")
        val users = listOf(user)
        
        coEvery { userRepository.updateUser(1, user) } returns ApiResult.Success(user)
        coEvery { userRepository.getUsers() } returns ApiResult.Success(users)

        // When
        userViewModel.updateUser(1, user)

        // Then
        userViewModel.updateUserState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(user, successState.data)
            assertNull(successState.error)
        }
    }

    @Test
    fun `updateUser should emit loading then error state`() = runTest {
        // Given
        val user = User(id = 1, name = "Updated User", email = "updated@example.com")
        coEvery { userRepository.updateUser(1, user) } returns ApiResult.Error("Update failed", 400)

        // When
        userViewModel.updateUser(1, user)

        // Then
        userViewModel.updateUserState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.data)
            assertEquals("Update failed", errorState.error)
            assertEquals(400, errorState.errorCode)
        }
    }

    @Test
    fun `patchUser should emit loading then success state and refresh users list`() = runTest {
        // Given
        val user = User(id = 1, name = "Patched User", email = "patched@example.com")
        val users = listOf(user)
        
        coEvery { userRepository.patchUser(1, user) } returns ApiResult.Success(user)
        coEvery { userRepository.getUsers() } returns ApiResult.Success(users)

        // When
        userViewModel.patchUser(1, user)

        // Then
        userViewModel.updateUserState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(user, successState.data)
            assertNull(successState.error)
        }
    }

    @Test
    fun `deleteUser should emit loading then success state and refresh users list`() = runTest {
        // Given
        val users = listOf<User>()
        
        coEvery { userRepository.deleteUser(1) } returns ApiResult.Success(Unit)
        coEvery { userRepository.getUsers() } returns ApiResult.Success(users)

        // When
        userViewModel.deleteUser(1)

        // Then
        userViewModel.deleteUserState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(Unit, successState.data)
            assertNull(successState.error)
        }
    }

    @Test
    fun `deleteUser should emit loading then error state`() = runTest {
        // Given
        coEvery { userRepository.deleteUser(1) } returns ApiResult.Error("Delete failed", 404)

        // When
        userViewModel.deleteUser(1)

        // Then
        userViewModel.deleteUserState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.data)
            assertEquals("Delete failed", errorState.error)
            assertEquals(404, errorState.errorCode)
        }
    }

    @Test
    fun `searchUsers should emit loading then success state`() = runTest {
        // Given
        val users = listOf(
            User(id = 1, name = "John Doe", email = "john@example.com")
        )
        coEvery { userRepository.searchUsers("John", null, 10) } returns ApiResult.Success(users)

        // When
        userViewModel.searchUsers("John", null, 10)

        // Then
        userViewModel.searchUsersState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(users, successState.data)
            assertNull(successState.error)
        }
    }

    @Test
    fun `searchUsers should emit loading then error state`() = runTest {
        // Given
        coEvery { userRepository.searchUsers("John", null, 10) } returns ApiResult.Error("Search failed", 500)

        // When
        userViewModel.searchUsers("John", null, 10)

        // Then
        userViewModel.searchUsersState.test {
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            // Error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.data)
            assertEquals("Search failed", errorState.error)
            assertEquals(500, errorState.errorCode)
        }
    }

    @Test
    fun `clearUserError should clear error from user state`() = runTest {
        // Given
        coEvery { userRepository.getUserById(1) } returns ApiResult.Error("User not found", 404)
        userViewModel.getUserById(1)

        // Wait for error state
        userViewModel.userState.test {
            awaitItem() // Loading
            awaitItem() // Error
        }

        // When
        userViewModel.clearUserError()

        // Then
        userViewModel.userState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }

    @Test
    fun `clearCreateUserError should clear error from create user state`() = runTest {
        // Given
        val newUser = User(name = "New User", email = "new@example.com")
        coEvery { userRepository.createUser(newUser) } returns ApiResult.Error("Validation failed", 422)
        userViewModel.createUser(newUser)

        // Wait for error state
        userViewModel.createUserState.test {
            awaitItem() // Loading
            awaitItem() // Error
        }

        // When
        userViewModel.clearCreateUserError()

        // Then
        userViewModel.createUserState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }

    @Test
    fun `clearUpdateUserError should clear error from update user state`() = runTest {
        // Given
        val user = User(id = 1, name = "Updated User", email = "updated@example.com")
        coEvery { userRepository.updateUser(1, user) } returns ApiResult.Error("Update failed", 400)
        userViewModel.updateUser(1, user)

        // Wait for error state
        userViewModel.updateUserState.test {
            awaitItem() // Loading
            awaitItem() // Error
        }

        // When
        userViewModel.clearUpdateUserError()

        // Then
        userViewModel.updateUserState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }

    @Test
    fun `clearDeleteUserError should clear error from delete user state`() = runTest {
        // Given
        coEvery { userRepository.deleteUser(1) } returns ApiResult.Error("Delete failed", 404)
        userViewModel.deleteUser(1)

        // Wait for error state
        userViewModel.deleteUserState.test {
            awaitItem() // Loading
            awaitItem() // Error
        }

        // When
        userViewModel.clearDeleteUserError()

        // Then
        userViewModel.deleteUserState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }

    @Test
    fun `clearSearchUsersError should clear error from search users state`() = runTest {
        // Given
        coEvery { userRepository.searchUsers("John", null, 10) } returns ApiResult.Error("Search failed", 500)
        userViewModel.searchUsers("John", null, 10)

        // Wait for error state
        userViewModel.searchUsersState.test {
            awaitItem() // Loading
            awaitItem() // Error
        }

        // When
        userViewModel.clearSearchUsersError()

        // Then
        userViewModel.searchUsersState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }
}
