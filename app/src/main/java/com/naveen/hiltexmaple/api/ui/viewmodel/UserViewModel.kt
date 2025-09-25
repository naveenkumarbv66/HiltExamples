package com.naveen.hiltexmaple.api.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.model.UiState
import com.naveen.hiltexmaple.api.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Users list state
    private val _usersState = MutableStateFlow(UiState<List<User>>())
    val usersState: StateFlow<UiState<List<User>>> = _usersState.asStateFlow()

    // Single user state
    private val _userState = MutableStateFlow(UiState<User>())
    val userState: StateFlow<UiState<User>> = _userState.asStateFlow()

    // Create user state
    private val _createUserState = MutableStateFlow(UiState<User>())
    val createUserState: StateFlow<UiState<User>> = _createUserState.asStateFlow()

    // Update user state
    private val _updateUserState = MutableStateFlow(UiState<User>())
    val updateUserState: StateFlow<UiState<User>> = _updateUserState.asStateFlow()

    // Delete user state
    private val _deleteUserState = MutableStateFlow(UiState<Unit>())
    val deleteUserState: StateFlow<UiState<Unit>> = _deleteUserState.asStateFlow()

    // Search users state
    private val _searchUsersState = MutableStateFlow(UiState<List<User>>())
    val searchUsersState: StateFlow<UiState<List<User>>> = _searchUsersState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = _usersState.value.copy(isLoading = true, error = null)
            
            when (val result = userRepository.getUsers()) {
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

    fun getUserById(id: Int) {
        viewModelScope.launch {
            _userState.value = _userState.value.copy(isLoading = true, error = null)
            
            when (val result = userRepository.getUserById(id)) {
                is ApiResult.Success -> {
                    _userState.value = UiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    _userState.value = UiState(
                        isLoading = false,
                        data = null,
                        error = result.message,
                        errorCode = result.errorCode
                    )
                }
                is ApiResult.Loading -> {
                    _userState.value = _userState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            _createUserState.value = _createUserState.value.copy(isLoading = true, error = null)
            
            when (val result = userRepository.createUser(user)) {
                is ApiResult.Success -> {
                    _createUserState.value = UiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                    // Refresh users list after creating
                    loadUsers()
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

    fun updateUser(id: Int, user: User) {
        viewModelScope.launch {
            _updateUserState.value = _updateUserState.value.copy(isLoading = true, error = null)
            
            when (val result = userRepository.updateUser(id, user)) {
                is ApiResult.Success -> {
                    _updateUserState.value = UiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                    // Refresh users list after updating
                    loadUsers()
                }
                is ApiResult.Error -> {
                    _updateUserState.value = UiState(
                        isLoading = false,
                        data = null,
                        error = result.message,
                        errorCode = result.errorCode
                    )
                }
                is ApiResult.Loading -> {
                    _updateUserState.value = _updateUserState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun patchUser(id: Int, user: User) {
        viewModelScope.launch {
            _updateUserState.value = _updateUserState.value.copy(isLoading = true, error = null)
            
            when (val result = userRepository.patchUser(id, user)) {
                is ApiResult.Success -> {
                    _updateUserState.value = UiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                    // Refresh users list after patching
                    loadUsers()
                }
                is ApiResult.Error -> {
                    _updateUserState.value = UiState(
                        isLoading = false,
                        data = null,
                        error = result.message,
                        errorCode = result.errorCode
                    )
                }
                is ApiResult.Loading -> {
                    _updateUserState.value = _updateUserState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            _deleteUserState.value = _deleteUserState.value.copy(isLoading = true, error = null)
            
            when (val result = userRepository.deleteUser(id)) {
                is ApiResult.Success -> {
                    _deleteUserState.value = UiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                    // Refresh users list after deleting
                    loadUsers()
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

    fun searchUsers(name: String? = null, email: String? = null, limit: Int? = null) {
        viewModelScope.launch {
            _searchUsersState.value = _searchUsersState.value.copy(isLoading = true, error = null)
            
            when (val result = userRepository.searchUsers(name, email, limit)) {
                is ApiResult.Success -> {
                    _searchUsersState.value = UiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    _searchUsersState.value = UiState(
                        isLoading = false,
                        data = null,
                        error = result.message,
                        errorCode = result.errorCode
                    )
                }
                is ApiResult.Loading -> {
                    _searchUsersState.value = _searchUsersState.value.copy(isLoading = true)
                }
            }
        }
    }

    // Clear error states
    fun clearUserError() {
        _userState.value = _userState.value.copy(error = null)
    }

    fun clearCreateUserError() {
        _createUserState.value = _createUserState.value.copy(error = null)
    }

    fun clearUpdateUserError() {
        _updateUserState.value = _updateUserState.value.copy(error = null)
    }

    fun clearDeleteUserError() {
        _deleteUserState.value = _deleteUserState.value.copy(error = null)
    }

    fun clearSearchUsersError() {
        _searchUsersState.value = _searchUsersState.value.copy(error = null)
    }
}
