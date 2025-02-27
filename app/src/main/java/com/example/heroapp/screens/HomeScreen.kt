package com.example.heroapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material.Scaffold

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.heroapp.api.House
import com.example.heroapp.api.NetworkResponse
import com.example.heroapp.model.HouseViewModel
import com.example.heroapp.navagation.AppScreens

@Composable
fun HomeScreen(
    navController: NavController, houseViewModel: HouseViewModel
) {
    val houseResult by houseViewModel.houseResult.observeAsState()

    Column(modifier = Modifier.padding(12.dp)) {
        when (houseResult) {
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
                Text("Loading houses...", style = MaterialTheme.typography.bodyLarge)
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
                if (houses.isEmpty()) {
                    Text("No houses found.", style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn {
                        items(houses) { house ->
                            HouseRow(house = house) { selectedHouse ->
                                navController.navigate(route = AppScreens.DetailScreen.name + "/$selectedHouse")
                            }
                        }
                    }
                }
            }
            else -> {
                Text("Unexpected error.", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun HouseRow(house: House, itemClick: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize()
            .clickable {
                itemClick(house.id)
            },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = house.imageUrl),
                    contentScale = ContentScale.Crop,
                    contentDescription = "House Image"
                )
            }
            Text(
                text = house.address,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = "Price: \$${house.price}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = "Distance: ${house.distance} miles",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = "Square Footage: ${house.squareFootage} sqft",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}