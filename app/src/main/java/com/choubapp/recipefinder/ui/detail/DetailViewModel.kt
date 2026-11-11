package com.choubapp.recipefinder.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choubapp.recipefinder.data.FavoritesStore
import com.choubapp.recipefinder.data.RecipeRepository
import com.choubapp.recipefinder.model.MealDetail
import com.choubapp.recipefinder.model.MealSummary
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val meal: MealDetail? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class DetailViewModel(
    private val id: String,
    private val repository: RecipeRepository,
    private val favoritesStore: FavoritesStore
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    val isFavorite: StateFlow<Boolean> = favoritesStore.favorites
        .map { list -> list.any { it.id == id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), favoritesStore.isFavorite(id))

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val meal = repository.getMealDetail(id)
                if (meal == null) {
                    _state.update { it.copy(isLoading = false, error = "Recipe not found.") }
                } else {
                    _state.update { it.copy(meal = meal, isLoading = false) }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Couldn't load this recipe. Check your internet and try again."
                    )
                }
            }
        }
    }

    fun toggleFavorite() {
        val meal = _state.value.meal ?: return
        favoritesStore.toggle(MealSummary(id = meal.id, name = meal.name, thumbnail = meal.thumbnail))
    }
}
