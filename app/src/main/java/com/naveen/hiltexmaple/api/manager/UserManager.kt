package com.naveen.hiltexmaple.api.manager

import android.util.Log
import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.usecase.UserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class demonstrating Use Case pattern in normal classes
 * This shows how to use use cases in regular classes with proper lifecycle management
 */
@Singleton
class UserManager @Inject constructor(
    private val userUseCase: UserUseCase
) {
    
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var cachedUsers: List<User> = emptyList()
    private var lastSyncTime: Long = 0L
    
    companion object {
        private const val TAG = "UserManager"
        private const val CACHE_DURATION = 5 * 60 * 1000L // 5 minutes
    }

    /**
     * Get users with caching mechanism
     */
    fun getUsers(
        forceRefresh: Boolean = false,
        onSuccess: (List<User>) -> Unit,
        onError: (String) -> Unit
    ) {
        val currentTime = System.currentTimeMillis()
        
        if (!forceRefresh && 
            cachedUsers.isNotEmpty() && 
            (currentTime - lastSyncTime) < CACHE_DURATION) {
            Log.d(TAG, "Returning cached users")
            onSuccess(cachedUsers)
            return
        }
        
        managerScope.launch {
            try {
                Log.d(TAG, "Fetching users from API...")
                
                userUseCase.getAllUsers().collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            cachedUsers = result.data
                            lastSyncTime = currentTime
                            Log.d(TAG, "Successfully fetched ${result.data.size} users")
                            onSuccess(result.data)
                        }
                        is ApiResult.Error -> {
                            Log.e(TAG, "Failed to fetch users: ${result.message}")
                            onError(result.message)
                        }
                        is ApiResult.Loading -> {
                            Log.d(TAG, "Loading users...")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching users", e)
                onError("Unexpected error: ${e.message}")
            }
        }
    }

    /**
     * Create user with callback
     */
    fun createUser(
        name: String,
        email: String,
        phone: String? = null,
        website: String? = null,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        managerScope.launch {
            try {
                Log.d(TAG, "Creating user: $name, $email")
                
                val newUser = User(
                    name = name,
                    email = email,
                    phone = phone,
                    website = website
                )
                
                userUseCase.createUser(newUser).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            Log.d(TAG, "Successfully created user: ${result.data.name}")
                            // Invalidate cache
                            cachedUsers = emptyList()
                            onSuccess(result.data)
                        }
                        is ApiResult.Error -> {
                            Log.e(TAG, "Failed to create user: ${result.message}")
                            onError(result.message)
                        }
                        is ApiResult.Loading -> {
                            Log.d(TAG, "Creating user...")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating user", e)
                onError("Unexpected error: ${e.message}")
            }
        }
    }

    /**
     * Delete user with callback
     */
    fun deleteUser(
        userId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        managerScope.launch {
            try {
                Log.d(TAG, "Deleting user with ID: $userId")
                
                userUseCase.deleteUser(userId).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            Log.d(TAG, "Successfully deleted user with ID: $userId")
                            // Invalidate cache
                            cachedUsers = emptyList()
                            onSuccess()
                        }
                        is ApiResult.Error -> {
                            Log.e(TAG, "Failed to delete user: ${result.message}")
                            onError(result.message)
                        }
                        is ApiResult.Loading -> {
                            Log.d(TAG, "Deleting user...")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting user", e)
                onError("Unexpected error: ${e.message}")
            }
        }
    }

    /**
     * Search users with callback
     */
    fun searchUsers(
        name: String? = null,
        email: String? = null,
        limit: Int = 10,
        onSuccess: (List<User>) -> Unit,
        onError: (String) -> Unit
    ) {
        managerScope.launch {
            try {
                Log.d(TAG, "Searching users with name: $name, email: $email")
                
                userUseCase.searchUsers(name, email, limit).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            Log.d(TAG, "Found ${result.data.size} users")
                            onSuccess(result.data)
                        }
                        is ApiResult.Error -> {
                            Log.e(TAG, "Failed to search users: ${result.message}")
                            onError(result.message)
                        }
                        is ApiResult.Loading -> {
                            Log.d(TAG, "Searching users...")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error searching users", e)
                onError("Unexpected error: ${e.message}")
            }
        }
    }

    /**
     * Get user statistics with callback
     */
    fun getUserStatistics(
        onSuccess: (com.naveen.hiltexmaple.api.domain.usecase.UserStatistics) -> Unit,
        onError: (String) -> Unit
    ) {
        managerScope.launch {
            try {
                Log.d(TAG, "Getting user statistics...")
                
                userUseCase.getUserStatistics().collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            Log.d(TAG, "Successfully got statistics")
                            onSuccess(result.data)
                        }
                        is ApiResult.Error -> {
                            Log.e(TAG, "Failed to get statistics: ${result.message}")
                            onError(result.message)
                        }
                        is ApiResult.Loading -> {
                            Log.d(TAG, "Loading statistics...")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting statistics", e)
                onError("Unexpected error: ${e.message}")
            }
        }
    }

    /**
     * Get cached users
     */
    fun getCachedUsers(): List<User> {
        return cachedUsers
    }

    /**
     * Check if cache is valid
     */
    fun isCacheValid(): Boolean {
        val currentTime = System.currentTimeMillis()
        return cachedUsers.isNotEmpty() && (currentTime - lastSyncTime) < CACHE_DURATION
    }

    /**
     * Clear cache
     */
    fun clearCache() {
        cachedUsers = emptyList()
        lastSyncTime = 0L
        Log.d(TAG, "Cache cleared")
    }

    /**
     * Cleanup resources
     */
    fun cleanup() {
        managerScope.cancel()
        Log.d(TAG, "UserManager cleaned up")
    }
}
