package com.johnpaulcas.foodrecipe.request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by johnpaulcas on 12/06/2020.
 */
object ServiceGenerator {

    private const val BASE_URL = "https://recipesapi.herokuapp.com"

    private val retrofitBuilder =  Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = retrofitBuilder.build()

    private val _recipeApi = retrofit.create(RecipeApi::class.java)

    val recipeApi: RecipeApi
        get() = _recipeApi

}