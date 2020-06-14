package com.johnpaulcas.foodrecipe.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.johnpaulcas.foodrecipe.models.Recipe
import com.johnpaulcas.foodrecipe.repositories.RecipeDetailsRepository

/**
 * Created by johnpaulcas on 14/06/2020.
 */
class RecipeDetailsViewModel: ViewModel() {

    private var repository = RecipeDetailsRepository.getInstance()
    private var _recipe: LiveData<Recipe>
    private var _isNetworkTimeout: LiveData<Boolean>

    private var _recipeId: String? = null

    val recipe: LiveData<Recipe>
        get() = _recipe

    val recipeId: String?
        get() = _recipeId

    val isNetworkTimeout: LiveData<Boolean>
     get() = _isNetworkTimeout

    init {
        _recipe = repository.recipe
        _isNetworkTimeout = repository.isNetworkTimeout
    }

    fun retrieveRecipe(recipeId: String) {
        _recipeId = recipeId
        repository.retrieveRecipe(recipeId)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        repository.cancelJob()
    }

}