package com.naveen.hiltexmaple.api.data.repository

import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.data.remote.ApiService
import com.naveen.hiltexmaple.api.data.remote.NetworkException
import com.naveen.hiltexmaple.api.data.remote.handleNetworkException
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {

    override suspend fun getUsers(): ApiResult<List<User>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                response.body()?.let { users ->
                    ApiResult.Success(users)
                } ?: ApiResult.Error("Empty response body", response.code())
            } else {
                ApiResult.Error(
                    "Failed to fetch users: ${response.message()}",
                    response.code()
                )
            }
        } catch (e: Exception) {
            val networkException = handleNetworkException(e)
            ApiResult.Error(networkException.message ?: "Unknown error occurred", (networkException as? NetworkException.HttpError)?.code)
        }
    }

    override suspend fun getUserById(id: Int): ApiResult<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserById(id)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    ApiResult.Success(user)
                } ?: ApiResult.Error("User not found", response.code())
            } else {
                ApiResult.Error(
                    "Failed to fetch user: ${response.message()}",
                    response.code()
                )
            }
        } catch (e: Exception) {
            val networkException = handleNetworkException(e)
            ApiResult.Error(networkException.message ?: "Unknown error occurred", (networkException as? NetworkException.HttpError)?.code)
        }
    }

    override suspend fun createUser(user: User): ApiResult<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createUser(user)
            if (response.isSuccessful) {
                response.body()?.let { createdUser ->
                    ApiResult.Success(createdUser)
                } ?: ApiResult.Error("Failed to create user", response.code())
            } else {
                ApiResult.Error(
                    "Failed to create user: ${response.message()}",
                    response.code()
                )
            }
        } catch (e: Exception) {
            val networkException = handleNetworkException(e)
            ApiResult.Error(networkException.message ?: "Unknown error occurred", (networkException as? NetworkException.HttpError)?.code)
        }
    }

    override suspend fun updateUser(id: Int, user: User): ApiResult<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateUser(id, user)
            if (response.isSuccessful) {
                response.body()?.let { updatedUser ->
                    ApiResult.Success(updatedUser)
                } ?: ApiResult.Error("Failed to update user", response.code())
            } else {
                ApiResult.Error(
                    "Failed to update user: ${response.message()}",
                    response.code()
                )
            }
        } catch (e: Exception) {
            val networkException = handleNetworkException(e)
            ApiResult.Error(networkException.message ?: "Unknown error occurred", (networkException as? NetworkException.HttpError)?.code)
        }
    }

    override suspend fun patchUser(id: Int, user: User): ApiResult<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.patchUser(id, user)
            if (response.isSuccessful) {
                response.body()?.let { patchedUser ->
                    ApiResult.Success(patchedUser)
                } ?: ApiResult.Error("Failed to patch user", response.code())
            } else {
                ApiResult.Error(
                    "Failed to patch user: ${response.message()}",
                    response.code()
                )
            }
        } catch (e: Exception) {
            val networkException = handleNetworkException(e)
            ApiResult.Error(networkException.message ?: "Unknown error occurred", (networkException as? NetworkException.HttpError)?.code)
        }
    }

    override suspend fun deleteUser(id: Int): ApiResult<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteUser(id)
            if (response.isSuccessful) {
                ApiResult.Success(Unit)
            } else {
                ApiResult.Error(
                    "Failed to delete user: ${response.message()}",
                    response.code()
                )
            }
        } catch (e: Exception) {
            val networkException = handleNetworkException(e)
            ApiResult.Error(networkException.message ?: "Unknown error occurred", (networkException as? NetworkException.HttpError)?.code)
        }
    }

    override suspend fun searchUsers(name: String?, email: String?, limit: Int?): ApiResult<List<User>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchUsers(name, email, limit)
            if (response.isSuccessful) {
                response.body()?.let { users ->
                    ApiResult.Success(users)
                } ?: ApiResult.Error("No users found", response.code())
            } else {
                ApiResult.Error(
                    "Failed to search users: ${response.message()}",
                    response.code()
                )
            }
        } catch (e: Exception) {
            val networkException = handleNetworkException(e)
            ApiResult.Error(networkException.message ?: "Unknown error occurred", (networkException as? NetworkException.HttpError)?.code)
        }
    }
}
