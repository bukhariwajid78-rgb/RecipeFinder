package com.choubapp.recipefinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.choubapp.recipefinder.ui.navigation.AppNavigation
import com.choubapp.recipefinder.ui.theme.RecipeFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeFinderTheme {
                AppNavigation()
            }
        }
    }
}
