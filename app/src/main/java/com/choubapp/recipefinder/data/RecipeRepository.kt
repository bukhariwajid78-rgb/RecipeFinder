package com.choubapp.recipefinder.data

import com.choubapp.recipefinder.data.remote.MealApi
import com.choubapp.recipefinder.data.remote.MealSummaryDto
import com.choubapp.recipefinder.model.Category
import com.choubapp.recipefinder.model.Ingredient
import com.choubapp.recipefinder.model.MealDetail
import com.choubapp.recipefinder.model.MealSummary

class RecipeRepository(private val api: MealApi) {

    suspend fun getCategories(): List<Category> =
        api.getCategories().categories.orEmpty().mapNotNull { dto ->
            val name = dto.strCategory ?: return@mapNotNull null
            Category(name = name, thumbnail = dto.strCategoryThumb.orEmpty())
        }

    suspend fun getMealsByCategory(category: String): List<MealSummary> =
        api.getMealsByCategory(category).meals.toSummaries()

    suspend fun searchMeals(query: String): List<MealSummary> =
        api.searchMeals(query).meals.toSummaries()

    suspend fun getMealDetail(id: String): MealDetail? =
        api.getMealById(id).meals?.firstOrNull()?.toMealDetail()
}

private fun List<MealSummaryDto>?.toSummaries(): List<MealSummary> =
    orEmpty().mapNotNull { dto ->
        val id = dto.idMeal ?: return@mapNotNull null
        MealSummary(
            id = id,
            name = dto.strMeal.orEmpty(),
            thumbnail = dto.strMealThumb.orEmpty()
        )
    }

private fun Map<String, String?>.toMealDetail(): MealDetail? {
    val id = this["idMeal"] ?: return null
    val ingredients = (1..20).mapNotNull { i ->
        val name = this["strIngredient$i"]?.trim().orEmpty()
        if (name.isEmpty()) null
        else Ingredient(name = name, measure = this["strMeasure$i"]?.trim().orEmpty())
    }
    return MealDetail(
        id = id,
        name = this["strMeal"].orEmpty(),
        category = this["strCategory"].orEmpty(),
        area = this["strArea"].orEmpty(),
        instructions = this["strInstructions"].orEmpty(),
        thumbnail = this["strMealThumb"].orEmpty(),
        youtube = this["strYoutube"].orEmpty(),
        ingredients = ingredients
    )
}