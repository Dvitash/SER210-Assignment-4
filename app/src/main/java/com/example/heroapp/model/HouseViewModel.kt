package com.example.heroapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroapp.api.ApiInterface
import com.example.heroapp.api.House
import com.example.heroapp.api.NetworkResponse
import com.example.heroapp.api.ZillowResponse
import kotlinx.coroutines.launch
import retrofit2.Response


class HouseViewModel : ViewModel() {
    private val api = ApiInterface.create()
    private val _houseResult = MutableLiveData<NetworkResponse<List<House>>>()
    val houseResult: LiveData<NetworkResponse<List<House>>> = _houseResult

    fun getData() {
        viewModelScope.launch {
            _houseResult.value = NetworkResponse.Loading // Emit loading state

            try {
                val response: Response<ZillowResponse> = api.getHouses()
                Log.d("API_CALL", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val houses = response.body()?.houses ?: emptyList()
                    _houseResult.value = NetworkResponse.Success(houses)
                    Log.d("API_SUCCESS", "Houses: $houses")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _houseResult.value = NetworkResponse.Error(errorMessage)
                    Log.e("API_ERROR", "Error Response: $errorMessage")
                }
            } catch (e: Exception) {
                _houseResult.value = NetworkResponse.Error(e.message ?: "Unknown error")
                Log.e("API_EXCEPTION", "Exception: ${e.message}")
            }
        }
    }
}