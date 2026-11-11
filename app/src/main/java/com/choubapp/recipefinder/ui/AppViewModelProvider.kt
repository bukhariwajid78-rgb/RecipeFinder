package com.choubapp.recipefinder.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.choubapp.recipefinder.RecipeApp
import com.choubapp.recipefinder.ui.detail.DetailViewModel
import com.choubapp.recipefinder.ui.favorites.FavoritesViewModel
import com.choubapp.recipefinder.ui.home.HomeViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer {
            val container = recipeApp().container
            HomeViewModel(container.repository, container.favorites)
        }
        initializer {
            FavoritesViewModel(recipeApp().container.favorites)
        }
    }

    fun detailFactory(id: String) = viewModelFactory {
        initializer {
            val container = recipeApp().container
            DetailViewModel(id, container.repository, container.favorites)
        }
    }
}

fun CreationExtras.recipeApp(): RecipeApp =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeApp
