package com.naveen.hiltexmaple.api.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.model.UiState
import com.naveen.hiltexmaple.api.domain.usecase.UserUseCase
import com.naveen.hiltexmaple.api.domain.usecase.UserStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel demonstrating Use Case pattern
 * This shows how to use use cases in ViewModels with proper state management
 */
@HiltViewModel
class UseCaseViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    // Users list state
    private val _usersState = MutableStateFlow(UiState<List<User>>())
    val usersState: StateFlow<UiState<List<User>>> = _usersState.asStateFlow()

    // Statistics state
    private val _statisticsState = MutableStateFlow(UiState<UserStatistics>())
    val statisticsState: StateFlow<UiState<UserStatistics>> = _statisticsState.asStateFlow()

    // Search state
    private val _searchState = MutableStateFlow(UiState<List<User>>())
    val searchState: StateFlow<UiState<List<User>>> = _searchState.asStateFlow()

    // Create user state
    private val _createUserState = MutableStateFlow(UiState<User>())
    val createUserState: StateFlow<UiState<User>> = _createUserState.asStateFlow()

    // Delete user state
    private val _deleteUserState = MutableStateFlow(UiState<Unit>())
    val deleteUserState: StateFlow<UiState<Unit>> = _deleteUserState.asStateFlow()

    init {
        loadUsers()
        loadStatistics()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = _usersState.value.copy(isLoading = true, error = null)
            
            userUseCase.getAllUsers().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _usersState.value = UiState(
                            isLoading = false,
                            data = result.data,
                            error = null
                        )
                    }
                    is ApiResult.Error -> {
                        _usersState.value = UiState(
                            isLoading = false,
                            data = null,
                            error = result.message,
                            errorCode = result.errorCode
                        )
                    }
                    is ApiResult.Loading -> {
                        _usersState.value = _usersState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _statisticsState.value = _statisticsState.value.copy(isLoading = true, error = null)
            
            userUseCase.getUserStatistics().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _statisticsState.value = UiState(
                            isLoading = false,
                            data = result.data,
                            error = null
                        )
                    }
                    is ApiResult.Error -> {
                        _statisticsState.value = UiState(
                            isLoading = false,
                            data = null,
                            error = result.message,
                            errorCode = result.errorCode
                        )
                    }
                    is ApiResult.Loading -> {
                        _statisticsState.value = _statisticsState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun createUser(name: String, email: String, phone: String? = null, website: String? = null) {
        viewModelScope.launch {
            _createUserState.value = _createUserState.value.copy(isLoading = true, error = null)
            
            val newUser = User(
                name = name,
                email = email,
                phone = phone,
                website = website
            )
            
            userUseCase.createUser(newUser).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _createUserState.value = UiState(
                            isLoading = false,
                            data = result.data,
                            error = null
                        )
                        // Refresh users list and statistics
                        loadUsers()
                        loadStatistics()
                    }
                    is ApiResult.Error -> {
                        _createUserState.value = UiState(
                            isLoading = false,
                            data = null,
                            error = result.message,
                            errorCode = result.errorCode
                        )
                    }
                    is ApiResult.Loading -> {
                        _createUserState.value = _createUserState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            _deleteUserState.value = _deleteUserState.value.copy(isLoading = true, error = null)
            
            userUseCase.deleteUser(id).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _deleteUserState.value = UiState(
                            isLoading = false,
                            data = result.data,
                            error = null
                        )
                        // Refresh users list and statistics
                        loadUsers()
                        loadStatistics()
                    }
                    is ApiResult.Error -> {
                        _deleteUserState.value = UiState(
                            isLoading = false,
                            data = null,
                            error = result.message,
                            errorCode = result.errorCode
                        )
                    }
                    is ApiResult.Loading -> {
                        _deleteUserState.value = _deleteUserState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun searchUsers(name: String?, email: String?, limit: Int = 10) {
        viewModelScope.launch {
            _searchState.value = _searchState.value.copy(isLoading = true, error = null)
            
            userUseCase.searchUsers(name, email, limit).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _searchState.value = UiState(
                            isLoading = false,
                            data = result.data,
                            error = null
                        )
                    }
                    is ApiResult.Error -> {
                        _searchState.value = UiState(
                            isLoading = false,
                            data = null,
                            error = result.message,
                            errorCode = result.errorCode
                        )
                    }
                    is ApiResult.Loading -> {
                        _searchState.value = _searchState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun getUserById(id: Int) {
        viewModelScope.launch {
            userUseCase.getUserById(id).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        // Handle single user result
                        // You could add a single user state if needed
                    }
                    is ApiResult.Error -> {
                        // Handle error
                    }
                    is ApiResult.Loading -> {
                        // Handle loading
                    }
                }
            }
        }
    }

    fun updateUser(id: Int, name: String, email: String, phone: String? = null, website: String? = null) {
        viewModelScope.launch {
            val updatedUser = User(
                id = id,
                name = name,
                email = email,
                phone = phone,
                website = website
            )
            
            userUseCase.updateUser(id, updatedUser).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        // Refresh users list and statistics
                        loadUsers()
                        loadStatistics()
                    }
                    is ApiResult.Error -> {
                        // Handle error
                    }
                    is ApiResult.Loading -> {
                        // Handle loading
                    }
                }
            }
        }
    }

    // Clear error states
    fun clearUsersError() {
        _usersState.value = _usersState.value.copy(error = null)
    }

    fun clearStatisticsError() {
        _statisticsState.value = _statisticsState.value.copy(error = null)
    }

    fun clearSearchError() {
        _searchState.value = _searchState.value.copy(error = null)
    }

    fun clearCreateUserError() {
        _createUserState.value = _createUserState.value.copy(error = null)
    }

    fun clearDeleteUserError() {
        _deleteUserState.value = _deleteUserState.value.copy(error = null)
    }
}
