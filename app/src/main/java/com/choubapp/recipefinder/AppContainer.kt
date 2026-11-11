package com.choubapp.recipefinder

import android.content.Context
import com.choubapp.recipefinder.data.FavoritesStore
import com.choubapp.recipefinder.data.RecipeRepository
import com.choubapp.recipefinder.data.remote.MealApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {

    private val api: MealApi = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MealApi::class.java)

    val repository = RecipeRepository(api)
    val favorites = FavoritesStore(context)
}
