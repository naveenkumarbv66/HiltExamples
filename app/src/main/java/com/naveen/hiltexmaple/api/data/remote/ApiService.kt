package com.naveen.hiltexmaple.api.data.remote

import com.naveen.hiltexmaple.api.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // GET - Fetch all users
    @GET("users")
    suspend fun getUsers(): Response<List<User>>
    
    // GET - Fetch user by ID
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>
    
    // POST - Create new user
    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>
    
    // PUT - Update existing user
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Response<User>
    
    // PATCH - Partially update user
    @PATCH("users/{id}")
    suspend fun patchUser(@Path("id") id: Int, @Body user: User): Response<User>
    
    // DELETE - Delete user
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
    
    // GET - Search users with query parameters
    @GET("users")
    suspend fun searchUsers(
        @Query("name") name: String? = null,
        @Query("email") email: String? = null,
        @Query("_limit") limit: Int? = null
    ): Response<List<User>>
}
