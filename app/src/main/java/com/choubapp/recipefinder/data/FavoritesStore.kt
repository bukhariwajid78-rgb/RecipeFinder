package com.choubapp.recipefinder.data

import android.content.Context
import com.choubapp.recipefinder.model.MealSummary
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Saves favorite recipes on the phone (SharedPreferences) so they survive app restarts. */
class FavoritesStore(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _favorites = MutableStateFlow(load())
    val favorites: StateFlow<List<MealSummary>> = _favorites.asStateFlow()

    fun isFavorite(id: String): Boolean = _favorites.value.any { it.id == id }

    fun toggle(meal: MealSummary) {
        val current = _favorites.value
        val updated = if (current.any { it.id == meal.id }) {
            current.filterNot { it.id == meal.id }
        } else {
            listOf(meal) + current
        }
        _favorites.value = updated
        prefs.edit().putString(KEY, gson.toJson(updated)).apply()
    }

    private fun load(): List<MealSummary> {
        val json = prefs.getString(KEY, null) ?: return emptyList()
        return try {
            gson.fromJson(json, Array<MealSummary>::class.java)?.toList().orEmpty()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private companion object {
        const val KEY = "favorite_meals"
    }
}
