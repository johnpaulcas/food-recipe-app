package com.johnpaulcas.foodrecipe.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.johnpaulcas.foodrecipe.models.Recipe
import com.johnpaulcas.foodrecipe.request.RecipeApiClient

/**
 * Created by johnpaulcas on 12/06/2020.
 */
class MainRepository {

    private var recipeApiClient: RecipeApiClient = RecipeApiClient.getInstance()
    private var query: String = ""
    private var pageNumber: Int = 1

    private var _isQueryExhausted = MutableLiveData<Boolean>()
    private var _mediatorRecipe = MediatorLiveData<List<Recipe>>()

    val recipes: LiveData<List<Recipe>>
        get() = _mediatorRecipe

    val isQueryExhausted: LiveData<Boolean>
        get() = _isQueryExhausted

    init {
        initMediator()
    }

    companion object {
        private var instance: MainRepository? = null

        fun newInstance(): MainRepository {
            if (instance == null) {
                instance = MainRepository()
            }
            return instance as MainRepository
        }
    }

    private fun initMediator() {
        val recipesApiSource = recipeApiClient.recipes
        _mediatorRecipe.addSource(recipesApiSource, Observer {
            if (!it.isNullOrEmpty()) {
                    _mediatorRecipe.value = it
                    doneQuery(it)
            } else {
                doneQuery(null)
            }
        })
    }

    private fun doneQuery(list: List<Recipe>?) {
        if (list.isNullOrEmpty() || list.size < 30) {
            _isQueryExhausted.value = true
        }
    }

    fun searchRecipeApi(query: String, pageNumber: Int) {
        var page = pageNumber
        if (page == 0) {
            page = 1
        }

        this._isQueryExhausted.value = false
        this.query = query
        this.pageNumber = pageNumber

        recipeApiClient.searchRecipesApi(query, page)
    }

    fun searchNextPage() {
        recipeApiClient.searchRecipesApi(query, pageNumber++)
    }

    fun cancelCoroutinesJob() {
        recipeApiClient.cancelCoroutinesJob()
    }

}