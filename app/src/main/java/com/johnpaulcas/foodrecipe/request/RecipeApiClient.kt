package com.johnpaulcas.foodrecipe.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.johnpaulcas.foodrecipe.models.Recipe
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

/**
 * Created by johnpaulcas on 12/06/2020.
 */
class RecipeApiClient {

    private var recipeSearchJob: Job? = null
    private var recipeJob: Job? = null

    private var _recipes = MutableLiveData<List<Recipe>>()
    private var _recipe = MutableLiveData<Recipe>()
    private var _isRecipesDetailsNetworkTimeout = MutableLiveData<Boolean>()

    val recipes: LiveData<List<Recipe>>
        get() = _recipes

    val recipe: LiveData<Recipe>
        get() = _recipe

    val isRecipeDetailsNetworkTimeout: LiveData<Boolean>
        get() = _isRecipesDetailsNetworkTimeout

    companion object {
        private var instance: RecipeApiClient? = null

        fun getInstance(): RecipeApiClient {
            if (instance == null) {
                instance = RecipeApiClient()
            }

            return instance as RecipeApiClient
        }
    }

    fun searchRecipesApi(query: String, pageNumber: Int) {
        recipeSearchJob = CoroutineScope(IO).launch {
            val recipes = getRecipes(query, pageNumber)
            withContext(Dispatchers.Main) {
                if (pageNumber <= 1) {
                    _recipes.value = recipes
                } else {
                    if (recipes != null && recipes.isNotEmpty()) {
                        val oldList = _recipes.value?.toMutableList()
                        for (recipe in recipes) {
                            oldList?.add(recipe)
                        }
                        _recipes.value = oldList?.toList()
                    } else {
                        _recipes.value = null
                    }
                }
            }
        }
    }

    fun retrieveRecipeApi(recipeId: String) {
        recipeJob = CoroutineScope(IO).launch {
            try {
                val job = withTimeoutOrNull(3000L) {
                    val recipe = getRecipeById(recipeId)
                    withContext(Dispatchers.Main) {
                        _isRecipesDetailsNetworkTimeout.value = false
                        _recipe.value = recipe
                    }
                }

                if (job == null) {
                    withContext(Dispatchers.Main) {
                        _isRecipesDetailsNetworkTimeout.value = true
                        _recipe.value = null
                    }
                }
            } catch (e: Throwable) {
                Log.d("###", "error message = ${e.message}")
                withContext(Dispatchers.Main) {
                    _isRecipesDetailsNetworkTimeout.value = true
                    _recipe.value = null
                }
            }
        }
    }

    fun cancelCoroutinesJob() {
        if (recipeSearchJob?.isActive == true) {
            recipeSearchJob?.cancel()
        }
    }

    fun cancelRecipeJob() {
        if (recipeJob?.isActive == true) {
            recipeJob?.cancel()
        }
    }

    suspend fun getRecipes(query: String, pageNumber: Int): List<Recipe>? {
        val result = ServiceGenerator.recipeApi.searchRecipe(query, pageNumber.toString())
        return result.recipes
    }

    suspend fun getRecipeById(recipeId: String): Recipe {
        val result = ServiceGenerator.recipeApi.getRecipe(recipeId)
        return result.recipe
    }

}