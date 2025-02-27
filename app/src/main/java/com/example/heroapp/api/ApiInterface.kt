package com.example.heroapp.api


import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

data class ZillowResponse(
    @SerializedName("props") val houses: List<House>
)

interface ApiInterface {
    @Headers(
        "X-RapidAPI-Key: 4a233b656emsh02d17db07f14be8p1bb218jsn998f5fb93257",
        "X-RapidAPI-Host: zillow-com1.p.rapidapi.com"
    )
    @GET("propertyExtendedSearch?location=Hamden%2C%20Connecticut&status_type=ForSale&home_type=Houses")
    suspend fun getHouses(): Response<ZillowResponse>

    companion object {
        private const val BASE_URL = "https://zillow-com1.p.rapidapi.com/"
        fun create(): ApiInterface {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ApiInterface::class.java)
        }
    }
}