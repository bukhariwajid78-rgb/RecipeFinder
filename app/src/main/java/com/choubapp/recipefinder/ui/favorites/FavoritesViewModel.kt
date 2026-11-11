package com.choubapp.recipefinder.ui.favorites

import androidx.lifecycle.ViewModel
import com.choubapp.recipefinder.data.FavoritesStore
import com.choubapp.recipefinder.model.MealSummary
import kotlinx.coroutines.flow.StateFlow

class FavoritesViewModel(private val favoritesStore: FavoritesStore) : ViewModel() {

    val favorites: StateFlow<List<MealSummary>> = favoritesStore.favorites

    fun toggleFavorite(meal: MealSummary) {
        favoritesStore.toggle(meal)
    }
}
