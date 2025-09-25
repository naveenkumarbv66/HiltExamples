package com.naveen.hiltexmaple.api.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.usecase.UserUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Background service demonstrating Use Case pattern
 * This shows how to use use cases in Android Services for background operations
 */
@AndroidEntryPoint
class UserSyncService : Service() {

    @Inject
    lateinit var userUseCase: UserUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isRunning = false

    companion object {
        private const val TAG = "UserSyncService"
        const val ACTION_SYNC_USERS = "com.naveen.hiltexmaple.SYNC_USERS"
        const val ACTION_CREATE_USER = "com.naveen.hiltexmaple.CREATE_USER"
        const val ACTION_DELETE_USER = "com.naveen.hiltexmaple.DELETE_USER"
        
        const val EXTRA_USER_NAME = "user_name"
        const val EXTRA_USER_EMAIL = "user_email"
        const val EXTRA_USER_ID = "user_id"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SYNC_USERS -> {
                syncUsers()
            }
            ACTION_CREATE_USER -> {
                val name = intent.getStringExtra(EXTRA_USER_NAME) ?: ""
                val email = intent.getStringExtra(EXTRA_USER_EMAIL) ?: ""
                createUser(name, email)
            }
            ACTION_DELETE_USER -> {
                val userId = intent.getIntExtra(EXTRA_USER_ID, -1)
                if (userId != -1) {
                    deleteUser(userId)
                }
            }
        }
        return START_STICKY
    }

    private fun syncUsers() {
        if (isRunning) return
        
        isRunning = true
        serviceScope.launch {
            try {
                Log.d(TAG, "Starting user sync...")
                
                userUseCase.getAllUsers().collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {
                            Log.d(TAG, "Syncing users...")
                        }
                        is ApiResult.Success -> {
                            Log.d(TAG, "Successfully synced ${result.data.size} users")
                            // Here you could save to local database, send to analytics, etc.
                            processUsers(result.data)
                        }
                        is ApiResult.Error -> {
                            Log.e(TAG, "Failed to sync users: ${result.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during user sync", e)
            } finally {
                isRunning = false
            }
        }
    }

    private fun createUser(name: String, email: String) {
        serviceScope.launch {
            try {
                Log.d(TAG, "Creating user: $name, $email")
                
                val newUser = User(name = name, email = email)
                userUseCase.createUser(newUser).collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {
                            Log.d(TAG, "Creating user...")
                        }
                        is ApiResult.Success -> {
                            Log.d(TAG, "Successfully created user: ${result.data.name}")
                            // Here you could send notification, update local database, etc.
                        }
                        is ApiResult.Error -> {
                            Log.e(TAG, "Failed to create user: ${result.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating user", e)
            }
        }
    }

    private fun deleteUser(userId: Int) {
        serviceScope.launch {
            try {
                Log.d(TAG, "Deleting user with ID: $userId")
                
                userUseCase.deleteUser(userId).collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {
                            Log.d(TAG, "Deleting user...")
                        }
                        is ApiResult.Success -> {
                            Log.d(TAG, "Successfully deleted user with ID: $userId")
                            // Here you could send notification, update local database, etc.
                        }
                        is ApiResult.Error -> {
                            Log.e(TAG, "Failed to delete user: ${result.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting user", e)
            }
        }
    }

    private fun processUsers(users: List<User>) {
        serviceScope.launch {
            try {
                // Example: Get statistics and log them
                userUseCase.getUserStatistics().collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            val stats = result.data
                            Log.d(TAG, "User Statistics - Total: ${stats.totalUsers}, " +
                                    "With Email: ${stats.usersWithEmail}, " +
                                    "With Phone: ${stats.usersWithPhone}, " +
                                    "With Website: ${stats.usersWithWebsite}")
                        }
                        is ApiResult.Error -> {
                            Log.e(TAG, "Failed to get statistics: ${result.message}")
                        }
                        is ApiResult.Loading -> {
                            Log.d(TAG, "Loading statistics...")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing users", e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Log.d(TAG, "UserSyncService destroyed")
    }
}
