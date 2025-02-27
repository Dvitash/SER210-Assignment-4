package com.example.heroapp.navagation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heroapp.model.HouseViewModel

import com.example.heroapp.screens.home.HomeScreen
import com.example.heroapp.screens.details.DetailsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: String,
    navController: NavController,
    navigateUp: () -> Unit,
    modifier: Modifier
) {
    TopAppBar(
        title = { Text("House App") },
        modifier = modifier
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MovieNavigation() {
    val navController = rememberNavController()
    val houseViewModel: HouseViewModel = viewModel()
    houseViewModel.getData()

    Scaffold(
        topBar = {
            AppBar(
                currentScreen = "House App",
                navController = navController,
                navigateUp = { navController.navigateUp() },
                modifier = Modifier
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.HomeScreen.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(AppScreens.HomeScreen.name) {
                HomeScreen(navController = navController, houseViewModel)
            }
            composable(
                AppScreens.DetailScreen.name + "/{houseId}",
                arguments = listOf(navArgument(name = "houseId") { type = NavType.StringType })
            ) { backStackEntry ->
                DetailsScreen(
                    navController = navController,
                    houseViewModel,
                    backStackEntry.arguments?.getString("houseId")
                )
            }
        }
    }
}