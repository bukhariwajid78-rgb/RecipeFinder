package com.choubapp.recipefinder.data.remote
import retrofit2.http.GET
import retrofit2.http.Query
interface MealApi {

    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealListResponse

    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealListResponse

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): MealDetailResponse
}