package com.naveen.hiltexmaple.api.domain.model

sealed class UserState {
    object Loading : UserState()
    data class Success<T>(val data: T) : UserState()
    data class Error(val message: String, val errorCode: Int? = null) : UserState()
}

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val message: String, val errorCode: Int? = null) : ApiResult<T>()
    class Loading<T> : ApiResult<T>()
}

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null,
    val errorCode: Int? = null
)
