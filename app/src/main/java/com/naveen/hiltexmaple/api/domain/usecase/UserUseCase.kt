package com.naveen.hiltexmaple.api.domain.usecase

import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for User operations - demonstrates clean architecture pattern
 * This use case encapsulates business logic and can be used across different layers
 */
class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    /**
     * Get all users with business logic
     */
    suspend fun getAllUsers(): Flow<ApiResult<List<User>>> = flow {
        emit(ApiResult.Loading())
        try {
            val result = userRepository.getUsers()
            emit(result)
        } catch (e: Exception) {
            emit(ApiResult.Error("Failed to fetch users: ${e.message}"))
        }
    }
    
    /**
     * Get user by ID with validation
     */
    suspend fun getUserById(id: Int): Flow<ApiResult<User>> = flow {
        emit(ApiResult.Loading())
        try {
            if (id <= 0) {
                emit(ApiResult.Error("Invalid user ID"))
                return@flow
            }
            val result = userRepository.getUserById(id)
            emit(result)
        } catch (e: Exception) {
            emit(ApiResult.Error("Failed to fetch user: ${e.message}"))
        }
    }
    
    /**
     * Create user with validation
     */
    suspend fun createUser(user: User): Flow<ApiResult<User>> = flow {
        emit(ApiResult.Loading())
        try {
            if (user.name.isBlank()) {
                emit(ApiResult.Error("User name cannot be empty"))
                return@flow
            }
            if (user.email.isBlank() || !isValidEmail(user.email)) {
                emit(ApiResult.Error("Invalid email address"))
                return@flow
            }
            val result = userRepository.createUser(user)
            emit(result)
        } catch (e: Exception) {
            emit(ApiResult.Error("Failed to create user: ${e.message}"))
        }
    }
    
    /**
     * Update user with validation
     */
    suspend fun updateUser(id: Int, user: User): Flow<ApiResult<User>> = flow {
        emit(ApiResult.Loading())
        try {
            if (id <= 0) {
                emit(ApiResult.Error("Invalid user ID"))
                return@flow
            }
            if (user.name.isBlank()) {
                emit(ApiResult.Error("User name cannot be empty"))
                return@flow
            }
            if (user.email.isBlank() || !isValidEmail(user.email)) {
                emit(ApiResult.Error("Invalid email address"))
                return@flow
            }
            val result = userRepository.updateUser(id, user)
            emit(result)
        } catch (e: Exception) {
            emit(ApiResult.Error("Failed to update user: ${e.message}"))
        }
    }
    
    /**
     * Delete user with validation
     */
    suspend fun deleteUser(id: Int): Flow<ApiResult<Unit>> = flow {
        emit(ApiResult.Loading())
        try {
            if (id <= 0) {
                emit(ApiResult.Error("Invalid user ID"))
                return@flow
            }
            val result = userRepository.deleteUser(id)
            emit(result)
        } catch (e: Exception) {
            emit(ApiResult.Error("Failed to delete user: ${e.message}"))
        }
    }
    
    /**
     * Search users with business logic
     */
    suspend fun searchUsers(name: String?, email: String?, limit: Int = 10): Flow<ApiResult<List<User>>> = flow {
        emit(ApiResult.Loading())
        try {
            if (name.isNullOrBlank() && email.isNullOrBlank()) {
                emit(ApiResult.Error("Please provide search criteria"))
                return@flow
            }
            if (limit <= 0 || limit > 100) {
                emit(ApiResult.Error("Limit must be between 1 and 100"))
                return@flow
            }
            val result = userRepository.searchUsers(name, email, limit)
            emit(result)
        } catch (e: Exception) {
            emit(ApiResult.Error("Failed to search users: ${e.message}"))
        }
    }
    
    /**
     * Get user statistics
     */
    suspend fun getUserStatistics(): Flow<ApiResult<UserStatistics>> = flow {
        emit(ApiResult.Loading())
        try {
            val usersResult = userRepository.getUsers()
            when (usersResult) {
                is ApiResult.Success -> {
                    val users = usersResult.data
                    val stats = UserStatistics(
                        totalUsers = users.size,
                        usersWithEmail = users.count { !it.email.isBlank() },
                        usersWithPhone = users.count { !it.phone.isNullOrBlank() },
                        usersWithWebsite = users.count { !it.website.isNullOrBlank() }
                    )
                    emit(ApiResult.Success(stats))
                }
                is ApiResult.Error -> emit(ApiResult.Error(usersResult.message, usersResult.errorCode))
                is ApiResult.Loading -> emit(ApiResult.Loading())
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Failed to get user statistics: ${e.message}"))
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

/**
 * Data class for user statistics
 */
data class UserStatistics(
    val totalUsers: Int,
    val usersWithEmail: Int,
    val usersWithPhone: Int,
    val usersWithWebsite: Int
)
