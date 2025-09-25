package com.naveen.hiltexmaple.api.data.model

import org.junit.Test
import org.junit.Assert.*

class ApiResponseTest {

    @Test
    fun `ApiResponse should create instance with all properties`() {
        // Given
        val testData = "Test Data"
        val apiResponse = ApiResponse(
            data = testData,
            message = "Success",
            status = "OK",
            code = 200
        )

        // Then
        assertEquals(testData, apiResponse.data)
        assertEquals("Success", apiResponse.message)
        assertEquals("OK", apiResponse.status)
        assertEquals(200, apiResponse.code)
    }

    @Test
    fun `ApiResponse should create instance with null properties`() {
        // Given
        val apiResponse = ApiResponse<String>()

        // Then
        assertNull(apiResponse.data)
        assertNull(apiResponse.message)
        assertNull(apiResponse.status)
        assertNull(apiResponse.code)
    }

    @Test
    fun `ApiResponse should support equality`() {
        // Given
        val response1 = ApiResponse(
            data = "Test",
            message = "Success",
            status = "OK",
            code = 200
        )
        
        val response2 = ApiResponse(
            data = "Test",
            message = "Success",
            status = "OK",
            code = 200
        )

        // Then
        assertEquals(response1, response2)
        assertEquals(response1.hashCode(), response2.hashCode())
    }

    @Test
    fun `ApiResponse should support copy`() {
        // Given
        val originalResponse = ApiResponse(
            data = "Test",
            message = "Success",
            status = "OK",
            code = 200
        )

        // When
        val copiedResponse = originalResponse.copy(message = "Updated")

        // Then
        assertEquals("Test", copiedResponse.data)
        assertEquals("Updated", copiedResponse.message)
        assertEquals("OK", copiedResponse.status)
        assertEquals(200, copiedResponse.code)
    }

    @Test
    fun `ErrorResponse should create instance with all properties`() {
        // Given
        val errorResponse = ErrorResponse(
            error = "Validation Error",
            message = "Invalid input data",
            statusCode = 400,
            timestamp = "2024-01-01T00:00:00Z"
        )

        // Then
        assertEquals("Validation Error", errorResponse.error)
        assertEquals("Invalid input data", errorResponse.message)
        assertEquals(400, errorResponse.statusCode)
        assertEquals("2024-01-01T00:00:00Z", errorResponse.timestamp)
    }

    @Test
    fun `ErrorResponse should create instance with null properties`() {
        // Given
        val errorResponse = ErrorResponse()

        // Then
        assertNull(errorResponse.error)
        assertNull(errorResponse.message)
        assertNull(errorResponse.statusCode)
        assertNull(errorResponse.timestamp)
    }

    @Test
    fun `ErrorResponse should support equality`() {
        // Given
        val error1 = ErrorResponse(
            error = "Test Error",
            message = "Test Message",
            statusCode = 500
        )
        
        val error2 = ErrorResponse(
            error = "Test Error",
            message = "Test Message",
            statusCode = 500
        )

        // Then
        assertEquals(error1, error2)
        assertEquals(error1.hashCode(), error2.hashCode())
    }

    @Test
    fun `ErrorResponse should support copy`() {
        // Given
        val originalError = ErrorResponse(
            error = "Test Error",
            message = "Test Message",
            statusCode = 500
        )

        // When
        val copiedError = originalError.copy(statusCode = 400)

        // Then
        assertEquals("Test Error", copiedError.error)
        assertEquals("Test Message", copiedError.message)
        assertEquals(400, copiedError.statusCode)
    }
}
