package com.example.heroapp.api

import com.google.gson.annotations.SerializedName

data class House(
    @SerializedName("zpid") val id: String,
    @SerializedName("address") val address: String,
    @SerializedName("price") val price: Double,
    @SerializedName("livingArea") val squareFootage: Int,
    @SerializedName("distance") val distance: Double?,
    @SerializedName("imgSrc") val imageUrl: String
)
