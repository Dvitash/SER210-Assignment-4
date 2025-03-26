package com.example.heroapp.navigation

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heroapp.R
import com.example.heroapp.model.HouseViewModel
import com.example.heroapp.navagation.AppScreens

import com.example.heroapp.screens.home.HomeScreen
import com.example.heroapp.screens.DetailsScreen


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAppBar(
    currentScreen: String,
    navController: NavController,
    navigateUp: () -> Unit,
    modifier: Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Detail Screen",
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomBar(onSettingsClick: () -> Unit,
                    onShareClick: () -> Unit,
                    onInfoClick: () -> Unit) {
    Row (modifier = Modifier){
        BottomAppBar(
            actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Toggle Theme",
                    )
                }
                IconButton(onClick = onShareClick) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "Localized description",
                    )
                }
                IconButton(onClick = onInfoClick) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = "Localized description",
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MovieNavigation() {
    //State to track the current theme
    var isDarkTheme by remember { mutableStateOf(false) }

    var shareText by remember { mutableStateOf("") }
    val lightColorScheme = lightColorScheme()
    val darkColorScheme = darkColorScheme()

    // Function to toggle theme
    val toggleTheme = { isDarkTheme = !isDarkTheme }

    val context = LocalContext.current

    // Function to share house details
    val shareHouseDetails: () -> Unit = {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        ContextCompat.startActivity(context, shareIntent, null)
    }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
    ) {
        val navController = rememberNavController()
        val houseViewModel: HouseViewModel = viewModel()
        houseViewModel.getData()

        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        Scaffold(
            bottomBar = {
                if (currentRoute?.contains(AppScreens.DetailScreen.name) == true) {
                    DetailBottomBar(
                        onSettingsClick = toggleTheme,
                        onShareClick = shareHouseDetails,
                        onInfoClick = { Toast.makeText(context, R.string.toast_string, Toast.LENGTH_LONG).show() }
                    )
                }
            },
            topBar = {
                if (currentRoute?.contains(AppScreens.DetailScreen.name) == true) {
                    DetailAppBar(
                        currentScreen = "Detail Screen",
                        navController = navController,
                        navigateUp = { navController.navigateUp() },
                        modifier = Modifier
                    )
                } else {
                    AppBar(
                        currentScreen = "House App",
                        navController = navController,
                        navigateUp = { navController.navigateUp() },
                        modifier = Modifier
                    )
                }
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
                        backStackEntry.arguments?.getString("houseId"),
                        onShareTextChange = { text -> shareText = text }
                    )
                }
            }
        }
    }
}