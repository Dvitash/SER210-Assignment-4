package com.example.heroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.heroapp.navagation.MovieNavigation
import com.example.heroapp.ui.theme.HeroAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MovieNavigation()
            }
        }
    }
}
@Composable
fun MyApp(content: @Composable () -> Unit) {
    HeroAppTheme {
        content()


    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
   MyApp {
      MovieNavigation()
   }
}