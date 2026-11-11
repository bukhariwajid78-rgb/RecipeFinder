package com.choubapp.recipefinder.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.choubapp.recipefinder.ui.AppViewModelProvider
import com.choubapp.recipefinder.ui.components.MealCard
import com.choubapp.recipefinder.ui.components.MealCardSkeleton

@Composable
fun HomeScreen(
    onMealClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val favoriteIds = remember(favorites) { favorites.map { it.id }.toSet() }
    val focusManager = LocalFocusManager.current
    val error = state.error

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {

            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp)) {
                Text(
                    text = "Recipe Finder",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "What would you like\nto cook today?",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            TextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = { Text("Search recipes, e.g. pasta") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.clearSearch()
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.search()
                    focusManager.clearFocus()
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.categories, key = { it.name }) { category ->
                    CategoryPill(
                        label = category.name,
                        selected = category.name == state.selectedCategory,
                        onClick = { viewModel.selectCategory(category.name) }
                    )
                }
            }

            Text(
                text = state.selectedCategory?.let { "$it recipes" } ?: "Search results",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        userScrollEnabled = false,
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(6) { MealCardSkeleton() }
                    }

                    error != null -> Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = error, textAlign = TextAlign.Center)
                        Button(onClick = viewModel::retry) { Text("Try again") }
                    }

                    state.meals.isEmpty() -> Text(
                        text = "No recipes found.\nTry another search.",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )

                    else -> LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.meals, key = { it.id }) { meal ->
                            MealCard(
                                meal = meal,
                                isFavorite = meal.id in favoriteIds,
                                onClick = { onMealClick(meal.id) },
                                onFavoriteClick = { viewModel.toggleFavorite(meal) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryPill(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
        )
    }
}
