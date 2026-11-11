package com.choubapp.recipefinder.model

data class Category(val name: String, val thumbnail: String)

data class MealSummary(val id: String, val name: String, val thumbnail: String)

data class Ingredient(val name: String, val measure: String)

data class MealDetail(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumbnail: String,
    val youtube: String,
    val ingredients: List<Ingredient>
)
