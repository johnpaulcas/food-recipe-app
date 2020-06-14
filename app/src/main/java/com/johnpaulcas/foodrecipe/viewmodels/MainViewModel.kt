package com.johnpaulcas.foodrecipe.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.johnpaulcas.foodrecipe.models.Recipe
import com.johnpaulcas.foodrecipe.repositories.MainRepository

/**
 * Created by johnpaulcas on 12/06/2020.
 */
class MainViewModel : ViewModel() {

    private var repository: MainRepository = MainRepository.newInstance()
    private var _recipes: LiveData<List<Recipe>>
    private var _isQueryExhausted: LiveData<Boolean>

    var isAlreadyExhausted: Boolean = false
    var isPerformingQuery = false
    var isViewingRecipes: Boolean = false

    val recipes: LiveData<List<Recipe>>
        get() = _recipes

    val isQueryExhausted: LiveData<Boolean>
        get() = _isQueryExhausted

    init {
        _recipes = repository.recipes
        _isQueryExhausted = repository.isQueryExhausted
    }

    fun searchRecipeApi(query: String, pageNumber: Int) {
        this.isViewingRecipes = true
        this.isPerformingQuery = true
        this.repository.searchRecipeApi(query, pageNumber)
    }

    fun searchNextPage() {
        if (!isPerformingQuery && isViewingRecipes && (_isQueryExhausted.value != true)) {
            this.repository.searchNextPage()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        repository.cancelCoroutinesJob()
    }

    fun onBackPressed(): Boolean {
        if (isPerformingQuery) {
            repository.cancelCoroutinesJob()
            isPerformingQuery = false
        }

        if (isViewingRecipes) {
            isViewingRecipes = false
            return false
        }

        return true
    }

}