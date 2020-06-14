package com.johnpaulcas.foodrecipe.repositories

import androidx.lifecycle.LiveData
import com.johnpaulcas.foodrecipe.models.Recipe
import com.johnpaulcas.foodrecipe.request.RecipeApiClient

/**
 * Created by johnpaulcas on 14/06/2020.
 */
class RecipeDetailsRepository {
    private val recipeApiClient = RecipeApiClient.getInstance()

    private var _recipe: LiveData<Recipe>
    private var _isNetworkTimeout: LiveData<Boolean>

    val recipe: LiveData<Recipe>
        get() = _recipe

    val isNetworkTimeout: LiveData<Boolean>
        get() = _isNetworkTimeout

    init {
        _recipe = recipeApiClient.recipe
        _isNetworkTimeout = recipeApiClient.isRecipeDetailsNetworkTimeout
    }

    companion object {
        private var instance: RecipeDetailsRepository? = null

        fun getInstance(): RecipeDetailsRepository {
            if (instance == null) {
                instance = RecipeDetailsRepository()
            }

            return instance as RecipeDetailsRepository
        }
    }

    fun retrieveRecipe(recipeId: String) {
        recipeApiClient.retrieveRecipeApi(recipeId)
    }

    fun cancelJob() {
        recipeApiClient.cancelRecipeJob()
    }
}