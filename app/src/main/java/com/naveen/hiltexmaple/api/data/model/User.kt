package com.naveen.hiltexmaple.api.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("website")
    val website: String? = null,
    
    @SerializedName("address")
    val address: Address? = null,
    
    @SerializedName("company")
    val company: Company? = null
)

data class Address(
    @SerializedName("street")
    val street: String? = null,
    
    @SerializedName("suite")
    val suite: String? = null,
    
    @SerializedName("city")
    val city: String? = null,
    
    @SerializedName("zipcode")
    val zipcode: String? = null,
    
    @SerializedName("geo")
    val geo: Geo? = null
)

data class Geo(
    @SerializedName("lat")
    val lat: String? = null,
    
    @SerializedName("lng")
    val lng: String? = null
)

data class Company(
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("catchPhrase")
    val catchPhrase: String? = null,
    
    @SerializedName("bs")
    val bs: String? = null
)
