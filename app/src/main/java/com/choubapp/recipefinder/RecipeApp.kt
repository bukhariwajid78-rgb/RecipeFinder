package com.choubapp.recipefinder

import android.app.Application

class RecipeApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}