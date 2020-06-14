package com.johnpaulcas.foodrecipe.request.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.johnpaulcas.foodrecipe.models.Recipe

/**
 * Created by johnpaulcas on 12/06/2020.
 */
data class RecipeResponse(
    @Expose
    @SerializedName("recipe")
    val recipe: Recipe
) {
}