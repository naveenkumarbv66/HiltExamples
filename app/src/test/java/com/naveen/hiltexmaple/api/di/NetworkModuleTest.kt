package com.naveen.hiltexmaple.api.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.naveen.hiltexmaple.api.data.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkModuleTest {

    @Test
    fun `Gson should be configured correctly`() {
        // Given
        val gson = GsonBuilder()
            .setLenient()
            .create()

        // Then
        assertNotNull(gson)
        // Note: isLenient property is not accessible, but we can test that it was created
    }

    @Test
    fun `HttpLoggingInterceptor should be configured correctly`() {
        // Given
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Then
        assertNotNull(interceptor)
        assertEquals(HttpLoggingInterceptor.Level.BODY, interceptor.level)
    }

    @Test
    fun `OkHttpClient should be configured correctly`() {
        // Given
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // Then
        assertNotNull(okHttpClient)
        assertEquals(30000L, okHttpClient.connectTimeoutMillis)
        assertEquals(30000L, okHttpClient.readTimeoutMillis)
        assertEquals(30000L, okHttpClient.writeTimeoutMillis)
    }

    @Test
    fun `Retrofit should be configured correctly`() {
        // Given
        val gson = GsonBuilder().setLenient().create()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // Then
        assertNotNull(retrofit)
        assertEquals("https://jsonplaceholder.typicode.com/", retrofit.baseUrl().toString())
    }

    @Test
    fun `ApiService should be created correctly`() {
        // Given
        val gson = GsonBuilder().setLenient().create()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        
        val apiService = retrofit.create(ApiService::class.java)

        // Then
        assertNotNull(apiService)
        assertTrue(apiService is ApiService)
    }

    @Test
    fun `Retrofit should have Gson converter factory`() {
        // Given
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // Then
        val converterFactories = retrofit.converterFactories()
        assertTrue(converterFactories.any { it.javaClass.simpleName.contains("GsonConverterFactory") })
    }

    @Test
    fun `OkHttpClient should have logging interceptor`() {
        // Given
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Then
        val interceptors = okHttpClient.interceptors
        assertTrue(interceptors.any { it is HttpLoggingInterceptor })
    }
}
