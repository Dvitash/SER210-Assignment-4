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

//Our API can be found at: https://rapidapi.com/apimaker/api/zillow-com1/playground/apiendpoint_4af058da-4283-4bfc-98ed-760f1cfd5f86
interface ApiInterface {
    @Headers(
        "X-RapidAPI-Key: //PUT YOUR KEY HERE",
        "X-RapidAPI-Host: zillow-com1.p.rapidapi.com"
    )
    //THIS IS THE SEARCH FUNCTION WE USE
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