package com.choubapp.recipefinder.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choubapp.recipefinder.data.FavoritesStore
import com.choubapp.recipefinder.data.RecipeRepository
import com.choubapp.recipefinder.model.Category
import com.choubapp.recipefinder.model.MealSummary
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategory: String? = null,
    val query: String = "",
    val meals: List<MealSummary> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(
    private val repository: RecipeRepository,
    private val favoritesStore: FavoritesStore
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    val favorites: StateFlow<List<MealSummary>> = favoritesStore.favorites

    private var loadJob: Job? = null
    private var lastLoad: suspend () -> List<MealSummary> = { emptyList() }

    init {
        loadCategories()
        selectCategory(DEFAULT_CATEGORY)
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = repository.getCategories()
                _state.update { it.copy(categories = categories) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // The chips stay empty. The meals list shows the error.
            }
        }
    }

    fun selectCategory(name: String) {
        _state.update { it.copy(selectedCategory = name, query = "") }
        loadMeals { repository.getMealsByCategory(name) }
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
    }

    fun search() {
        val query = _state.value.query.trim()
        if (query.isEmpty()) {
            selectCategory(_state.value.selectedCategory ?: DEFAULT_CATEGORY)
            return
        }
        _state.update { it.copy(selectedCategory = null) }
        loadMeals { repository.searchMeals(query) }
    }

    fun clearSearch() {
        selectCategory(_state.value.selectedCategory ?: DEFAULT_CATEGORY)
    }

    fun retry() {
        if (_state.value.categories.isEmpty()) loadCategories()
        loadMeals(lastLoad)
    }

    fun toggleFavorite(meal: MealSummary) {
        favoritesStore.toggle(meal)
    }

    private fun loadMeals(block: suspend () -> List<MealSummary>) {
        lastLoad = block
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val meals = block()
                _state.update { it.copy(meals = meals, isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Couldn't load recipes. Check your internet and try again."
                    )
                }
            }
        }
    }

    private companion object {
        const val DEFAULT_CATEGORY = "Chicken"
    }
}
