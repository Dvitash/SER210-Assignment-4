package com.example.heroapp.screens

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heroapp.api.House
import com.example.heroapp.api.NetworkResponse
import com.example.heroapp.model.HouseViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun DetailsScreen(
    navController: NavController,
    houseViewModel: HouseViewModel,
    houseId: String?,
    onShareTextChange: ((String) -> Unit)? = null
) {
    val houseResult by houseViewModel.houseResult.observeAsState()

    LaunchedEffect(houseResult) {
        when (houseResult) {
            is NetworkResponse.Success -> {
                val houses = (houseResult as NetworkResponse.Success<List<House>>).data
                val selectedHouse = houses.find { it.id == houseId }

                selectedHouse?.let { house ->
                    val shareText = """
                        House Details:
                        Address: ${house.address}
                        Price: $${house.price}
                        Distance: ${house.distance} miles
                        Square Footage: ${house.squareFootage} sqft
                    """.trimIndent()

                    onShareTextChange?.invoke(shareText)
                }
            }
            else -> onShareTextChange?.invoke("Unable to retrieve house details")
        }
    }

    Column(modifier = Modifier.padding(12.dp)) {
        when (houseResult) {
            NetworkResponse.Loading -> {
                Text(text = "Loading house details...", style = MaterialTheme.typography.bodyLarge)
            }

            is NetworkResponse.Error -> {
                Text(
                    text = "Error: ${(houseResult as NetworkResponse.Error).message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            is NetworkResponse.Success -> {
                val houses = (houseResult as NetworkResponse.Success<List<House>>).data
                val selectedHouse = houses.find { it.id == houseId }

                selectedHouse?.let { house ->
                    Text(text = "Address: ${house.address}", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Price: \$${house.price}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Distance: ${house.distance} miles", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Square Footage: ${house.squareFootage} sqft", style = MaterialTheme.typography.bodyLarge)
                } ?: run {
                    Text(text = "House not found.", style = MaterialTheme.typography.bodyLarge)
                }
            }

            else -> {
                Text(text = "Unexpected error.", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}