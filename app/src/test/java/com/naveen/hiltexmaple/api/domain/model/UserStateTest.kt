package com.naveen.hiltexmaple.api.domain.model

import com.naveen.hiltexmaple.api.data.model.User
import org.junit.Test
import org.junit.Assert.*

class UserStateTest {

    @Test
    fun `UserState Loading should be singleton`() {
        // Given
        val loading1 = UserState.Loading
        val loading2 = UserState.Loading

        // Then
        assertSame(loading1, loading2)
    }

    @Test
    fun `UserState Success should hold data`() {
        // Given
        val user = User(name = "John Doe", email = "john@example.com")
        val success = UserState.Success(user)

        // Then
        assertEquals(user, success.data)
    }

    @Test
    fun `UserState Error should hold message and error code`() {
        // Given
        val error = UserState.Error("Network error", 500)

        // Then
        assertEquals("Network error", error.message)
        assertEquals(500, error.errorCode)
    }

    @Test
    fun `UserState Error should have default null error code`() {
        // Given
        val error = UserState.Error("Simple error")

        // Then
        assertEquals("Simple error", error.message)
        assertNull(error.errorCode)
    }

    @Test
    fun `ApiResult Success should hold data`() {
        // Given
        val user = User(name = "Jane Doe", email = "jane@example.com")
        val success = ApiResult.Success(user)

        // Then
        assertEquals(user, success.data)
    }

    @Test
    fun `ApiResult Error should hold message and error code`() {
        // Given
        val error = ApiResult.Error<String>("API error", 404)

        // Then
        assertEquals("API error", error.message)
        assertEquals(404, error.errorCode)
    }

    @Test
    fun `ApiResult Error should have default null error code`() {
        // Given
        val error = ApiResult.Error<String>("Simple API error")

        // Then
        assertEquals("Simple API error", error.message)
        assertNull(error.errorCode)
    }

    @Test
    fun `ApiResult Loading should be instantiable`() {
        // Given
        val loading = ApiResult.Loading<String>()

        // Then
        assertNotNull(loading)
    }

    @Test
    fun `UiState should have default values`() {
        // Given
        val uiState = UiState<String>()

        // Then
        assertFalse(uiState.isLoading)
        assertNull(uiState.data)
        assertNull(uiState.error)
        assertNull(uiState.errorCode)
    }

    @Test
    fun `UiState should hold custom values`() {
        // Given
        val user = User(name = "Test User", email = "test@example.com")
        val uiState = UiState(
            isLoading = true,
            data = user,
            error = "Test error",
            errorCode = 400
        )

        // Then
        assertTrue(uiState.isLoading)
        assertEquals(user, uiState.data)
        assertEquals("Test error", uiState.error)
        assertEquals(400, uiState.errorCode)
    }

    @Test
    fun `UiState should support copy`() {
        // Given
        val originalState = UiState<String>(
            isLoading = true,
            error = "Original error"
        )

        // When
        val copiedState = originalState.copy(
            isLoading = false,
            data = "New data"
        )

        // Then
        assertFalse(copiedState.isLoading)
        assertEquals("New data", copiedState.data)
        assertEquals("Original error", copiedState.error)
        assertNull(copiedState.errorCode)
    }

    @Test
    fun `UiState should support equality`() {
        // Given
        val state1 = UiState(
            isLoading = false,
            data = "Test",
            error = null,
            errorCode = null
        )
        
        val state2 = UiState(
            isLoading = false,
            data = "Test",
            error = null,
            errorCode = null
        )

        // Then
        assertEquals(state1, state2)
        assertEquals(state1.hashCode(), state2.hashCode())
    }
}
