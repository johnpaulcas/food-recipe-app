package com.johnpaulcas.foodrecipe.request

import com.johnpaulcas.foodrecipe.request.responses.RecipeResponse
import com.johnpaulcas.foodrecipe.request.responses.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by johnpaulcas on 12/06/2020.
 */
interface RecipeApi {

    @GET("api/search")
    suspend fun searchRecipe(
        @Query("q") q: String,
        @Query("page") page: String
    ): RecipeSearchResponse

    @GET("api/get")
    suspend fun getRecipe(
        @Query("rId") recipeId: String
    ): RecipeResponse

}