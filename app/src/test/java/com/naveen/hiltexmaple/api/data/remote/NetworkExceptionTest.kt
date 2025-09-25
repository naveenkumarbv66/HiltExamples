package com.naveen.hiltexmaple.api.data.remote

import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.Assert.*
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkExceptionTest {

    @Test
    fun `handleNetworkException should return TimeoutError for timeout IOException`() {
        // Given
        val timeoutException = SocketTimeoutException("timeout occurred")

        // When
        val result = handleNetworkException(timeoutException)

        // Then
        assertTrue(result is NetworkException.TimeoutError)
        assertEquals("Request timeout. Please try again.", result.message)
    }

    @Test
    fun `handleNetworkException should return NoInternetError for network IOException`() {
        // Given
        val networkException = UnknownHostException("network error occurred")

        // When
        val result = handleNetworkException(networkException)

        // Then
        assertTrue(result is NetworkException.NoInternetError)
        assertEquals("No internet connection. Please check your network.", result.message)
    }

    @Test
    fun `handleNetworkException should return NetworkError for other IOException`() {
        // Given
        val ioException = IOException("Connection refused")

        // When
        val result = handleNetworkException(ioException)

        // Then
        assertTrue(result is NetworkException.NetworkError)
        assertEquals("Network error: Connection refused", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for 400 Bad Request`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 400
        every { httpException.message() } returns "Bad Request"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(400, (result as NetworkException.HttpError).code)
        assertEquals("Bad Request - Invalid data provided", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for 401 Unauthorized`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 401
        every { httpException.message() } returns "Unauthorized"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(401, (result as NetworkException.HttpError).code)
        assertEquals("Unauthorized - Authentication required", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for 403 Forbidden`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 403
        every { httpException.message() } returns "Forbidden"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(403, (result as NetworkException.HttpError).code)
        assertEquals("Forbidden - Access denied", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for 404 Not Found`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 404
        every { httpException.message() } returns "Not Found"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(404, (result as NetworkException.HttpError).code)
        assertEquals("Not Found - Resource not found", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for 422 Unprocessable Entity`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 422
        every { httpException.message() } returns "Unprocessable Entity"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(422, (result as NetworkException.HttpError).code)
        assertEquals("Unprocessable Entity - Validation failed", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for 429 Too Many Requests`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 429
        every { httpException.message() } returns "Too Many Requests"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(429, (result as NetworkException.HttpError).code)
        assertEquals("Too Many Requests - Rate limit exceeded", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for 500 Internal Server Error`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 500
        every { httpException.message() } returns "Internal Server Error"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(500, (result as NetworkException.HttpError).code)
        assertEquals("Internal Server Error - Server error occurred", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for 502 Bad Gateway`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 502
        every { httpException.message() } returns "Bad Gateway"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(502, (result as NetworkException.HttpError).code)
        assertEquals("Bad Gateway - Server temporarily unavailable", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for 503 Service Unavailable`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 503
        every { httpException.message() } returns "Service Unavailable"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(503, (result as NetworkException.HttpError).code)
        assertEquals("Service Unavailable - Server maintenance", result.message)
    }

    @Test
    fun `handleNetworkException should return HttpError for unknown HTTP codes`() {
        // Given
        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 418
        every { httpException.message() } returns "I'm a teapot"

        // When
        val result = handleNetworkException(httpException)

        // Then
        assertTrue(result is NetworkException.HttpError)
        assertEquals(418, (result as NetworkException.HttpError).code)
        assertEquals("HTTP Error 418: I'm a teapot", result.message)
    }

    @Test
    fun `handleNetworkException should return UnknownError for other exceptions`() {
        // Given
        val unknownException = RuntimeException("Something went wrong")

        // When
        val result = handleNetworkException(unknownException)

        // Then
        assertTrue(result is NetworkException.UnknownError)
        assertEquals("Unknown error: Something went wrong", result.message)
    }

    @Test
    fun `NetworkException subclasses should have correct message property`() {
        // Given
        val httpError = NetworkException.HttpError(404, "Not found")
        val networkError = NetworkException.NetworkError("Network issue")
        val timeoutError = NetworkException.TimeoutError("Timeout occurred")
        val noInternetError = NetworkException.NoInternetError("No internet")
        val unknownError = NetworkException.UnknownError("Unknown issue")

        // Then
        assertEquals("Not found", httpError.message)
        assertEquals("Network issue", networkError.message)
        assertEquals("Timeout occurred", timeoutError.message)
        assertEquals("No internet", noInternetError.message)
        assertEquals("Unknown issue", unknownError.message)
    }

    @Test
    fun `NetworkException HttpError should have correct code property`() {
        // Given
        val httpError = NetworkException.HttpError(500, "Server error")

        // Then
        assertEquals(500, httpError.code)
        assertEquals("Server error", httpError.message)
    }
}
