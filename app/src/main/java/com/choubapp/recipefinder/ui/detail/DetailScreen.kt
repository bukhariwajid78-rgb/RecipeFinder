package com.choubapp.recipefinder.ui.detail

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.choubapp.recipefinder.model.Ingredient
import com.choubapp.recipefinder.model.MealDetail
import com.choubapp.recipefinder.ui.AppViewModelProvider
import com.choubapp.recipefinder.ui.components.CircleIconButton

@Composable
fun DetailScreen(
    id: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(
        key = "detail_$id",
        factory = AppViewModelProvider.detailFactory(id)
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
    val meal = state.meal
    val error = state.error

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            error != null -> Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = error, textAlign = TextAlign.Center)
                Button(onClick = viewModel::load) { Text("Try again") }
            }

            meal != null -> RecipeContent(meal)
        }

        // Floating buttons sit on top of the photo.
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            CircleIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                onClick = onBack
            )
            if (meal != null) {
                CircleIconButton(
                    icon = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    onClick = viewModel::toggleFavorite,
                    tint = if (isFavorite) Color(0xFFFF6B6B) else Color.White
                )
            }
        }
    }
}

@Composable
private fun RecipeContent(meal: MealDetail) {
    val uriHandler = LocalUriHandler.current
    val stepNumberOnly = Regex("^(step\\s*\\d+|\\d+)[.:]?$", RegexOption.IGNORE_CASE)
    val steps = meal.instructions
        .split("\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() && !stepNumberOnly.matches(it) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box {
            AsyncImage(
                model = meal.thumbnail,
                contentDescription = meal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(340.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.55f), Color.Transparent)
                        )
                    )
            )
        }

        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier.fillMaxWidth().offset(y = (-28).dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 32.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(meal.name, style = MaterialTheme.typography.headlineMedium)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    if (meal.category.isNotEmpty()) InfoPill(meal.category)
                    if (meal.area.isNotEmpty()) InfoPill(meal.area)
                    InfoPill("${meal.ingredients.size} ingredients")
                }

                if (meal.youtube.isNotBlank()) {
                    Button(
                        onClick = { uriHandler.openUri(meal.youtube) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Watch video tutorial")
                    }
                }

                Text("Ingredients", style = MaterialTheme.typography.titleLarge)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    meal.ingredients.chunked(2).forEach { pair ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            pair.forEach { IngredientCard(it, Modifier.weight(1f)) }
                            if (pair.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }

                Text("Instructions", style = MaterialTheme.typography.titleLarge)
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    steps.forEachIndexed { index, step ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoPill(text: String) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun IngredientCard(ingredient: Ingredient, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            AsyncImage(
                model = "https://www.themealdb.com/images/ingredients/${Uri.encode(ingredient.name)}-Small.png",
                contentDescription = ingredient.name,
                modifier = Modifier.size(40.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (ingredient.measure.isNotEmpty()) {
                    Text(
                        text = ingredient.measure,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
