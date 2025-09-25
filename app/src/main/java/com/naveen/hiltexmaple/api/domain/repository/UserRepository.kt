package com.naveen.hiltexmaple.api.domain.repository

import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult

interface UserRepository {
    suspend fun getUsers(): ApiResult<List<User>>
    suspend fun getUserById(id: Int): ApiResult<User>
    suspend fun createUser(user: User): ApiResult<User>
    suspend fun updateUser(id: Int, user: User): ApiResult<User>
    suspend fun patchUser(id: Int, user: User): ApiResult<User>
    suspend fun deleteUser(id: Int): ApiResult<Unit>
    suspend fun searchUsers(name: String?, email: String?, limit: Int?): ApiResult<List<User>>
}
