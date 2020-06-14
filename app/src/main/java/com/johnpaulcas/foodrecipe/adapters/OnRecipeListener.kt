package com.johnpaulcas.foodrecipe.adapters

import com.johnpaulcas.foodrecipe.models.Recipe

/**
 * Created by johnpaulcas on 13/06/2020.
 */
interface OnRecipeListener {

    fun onRecipeClick(recipe: Recipe?)

    fun onCategoryClick(category: String)

}