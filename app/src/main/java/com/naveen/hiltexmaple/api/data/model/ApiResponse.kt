package com.naveen.hiltexmaple.api.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("data")
    val data: T? = null,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("status")
    val status: String? = null,
    
    @SerializedName("code")
    val code: Int? = null
)

data class ErrorResponse(
    @SerializedName("error")
    val error: String? = null,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    
    @SerializedName("timestamp")
    val timestamp: String? = null
)
