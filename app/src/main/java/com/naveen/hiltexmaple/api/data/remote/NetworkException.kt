package com.naveen.hiltexmaple.api.data.remote

import java.io.IOException

sealed class NetworkException : Exception() {
    data class HttpError(val code: Int, override val message: String) : NetworkException()
    data class NetworkError(override val message: String) : NetworkException()
    data class UnknownError(override val message: String) : NetworkException()
    data class TimeoutError(override val message: String) : NetworkException()
    data class NoInternetError(override val message: String) : NetworkException()
}

fun handleNetworkException(throwable: Throwable): NetworkException {
    return when (throwable) {
        is IOException -> {
            if (throwable.message?.contains("timeout", ignoreCase = true) == true) {
                NetworkException.TimeoutError("Request timeout. Please try again.")
            } else if (throwable.message?.contains("network", ignoreCase = true) == true) {
                NetworkException.NoInternetError("No internet connection. Please check your network.")
            } else {
                NetworkException.NetworkError("Network error: ${throwable.message}")
            }
        }
        is retrofit2.HttpException -> {
            val errorCode = throwable.code()
            val errorMessage = when (errorCode) {
                400 -> "Bad Request - Invalid data provided"
                401 -> "Unauthorized - Authentication required"
                403 -> "Forbidden - Access denied"
                404 -> "Not Found - Resource not found"
                422 -> "Unprocessable Entity - Validation failed"
                429 -> "Too Many Requests - Rate limit exceeded"
                500 -> "Internal Server Error - Server error occurred"
                502 -> "Bad Gateway - Server temporarily unavailable"
                503 -> "Service Unavailable - Server maintenance"
                else -> "HTTP Error $errorCode: ${throwable.message()}"
            }
            NetworkException.HttpError(errorCode, errorMessage)
        }
        else -> NetworkException.UnknownError("Unknown error: ${throwable.message}")
    }
}
