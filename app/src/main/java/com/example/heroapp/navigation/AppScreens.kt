package com.example.heroapp.navagation

import java.lang.IllegalArgumentException

enum class AppScreens {
    HomeScreen,
    DetailScreen;
    companion object {
        fun fromRoute(route: String?): AppScreens = when (route?.substringBefore("/")) {
            HomeScreen.name -> HomeScreen
            DetailScreen.name -> DetailScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}
