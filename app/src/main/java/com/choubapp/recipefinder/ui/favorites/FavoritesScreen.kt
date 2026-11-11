package com.choubapp.recipefinder.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.choubapp.recipefinder.ui.AppViewModelProvider
import com.choubapp.recipefinder.ui.components.MealCard

@Composable
fun FavoritesScreen(
    onMealClick: (String) -> Unit,
    viewModel: FavoritesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp)) {
                Text(
                    text = if (favorites.isEmpty()) "Favorites" else "${favorites.size} saved",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Your favorite\nrecipes",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (favorites.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(72.dp)
                        )
                        Text(
                            text = "Nothing here yet.\nTap the heart on a recipe to save it.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(favorites, key = { it.id }) { meal ->
                            MealCard(
                                meal = meal,
                                isFavorite = true,
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
