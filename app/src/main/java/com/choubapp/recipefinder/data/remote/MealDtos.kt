package com.choubapp.recipefinder.data.remote

data class CategoriesResponse(val categories: List<CategoryDto>?)

data class CategoryDto(
    val idCategory: String?,
    val strCategory: String?,
    val strCategoryThumb: String?,
    val strCategoryDescription: String?
)

data class MealListResponse(val meals: List<MealSummaryDto>?)

data class MealSummaryDto(
    val idMeal: String?,
    val strMeal: String?,
    val strMealThumb: String?
)

// A full meal has more than 50 fields, so we read it as a Map.
data class MealDetailResponse(val meals: List<Map<String, String?>>?)