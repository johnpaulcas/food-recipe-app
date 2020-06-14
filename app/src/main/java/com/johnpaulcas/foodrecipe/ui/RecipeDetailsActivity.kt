package com.johnpaulcas.foodrecipe.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.johnpaulcas.foodrecipe.R
import com.johnpaulcas.foodrecipe.base.BaseActivity
import com.johnpaulcas.foodrecipe.models.Recipe
import com.johnpaulcas.foodrecipe.viewmodels.RecipeDetailsViewModel
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlin.math.roundToInt

class RecipeDetailsActivity : BaseActivity() {

    private lateinit var recipeDetailsViewModel: RecipeDetailsViewModel

    override fun getLayoutResourceId(): Int = R.layout.activity_recipe_details

    override fun init(bundle: Bundle?) {
        subscribeObserver()
        getIncomingIntent()
    }

    private fun subscribeObserver() {
        recipeDetailsViewModel = ViewModelProvider(this).get(RecipeDetailsViewModel::class.java)

        recipeDetailsViewModel.recipe.observe(this, Observer { recipe ->
            recipe?.let {
                if (it.recipeId!!.equals(recipeDetailsViewModel.recipeId)) {
                    setRecipeProperties(it)
                }
            }
        })

        recipeDetailsViewModel.isNetworkTimeout.observe(this, Observer {
            it?.let { isNetworkTimeout ->
                if (isNetworkTimeout) {
                    displayErrorMessage("Error retrieving data. Please check internet connection.")
                    showRecipeDetails()
                }
            }
        })
    }

    private fun getIncomingIntent() {
        if (intent?.hasExtra("recipe") == true) {
            val recipe = intent.getParcelableExtra<Recipe>("recipe")
            recipe.recipeId?.let {
                retrieveRecipeData(it)
            }
        }
    }

    private fun setRecipeProperties(recipe: Recipe) {
        val requestOption = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)

        Glide.with(this)
            .setDefaultRequestOptions(requestOption)
            .load(recipe.imageUrl)
            .into(ivRecipeImage)

        tvRecipeTitle.text = recipe.title
        tvRecipeSocialScore.text = recipe.socialRank.roundToInt().toString()

        llIngredientsContainer.removeAllViews()
        if (recipe.ingredients != null) {
            for (ingredient in recipe.ingredients) {
                val tv = AppCompatTextView(this)
                tv.text = ingredient
                tv.textSize = 15f
                tv.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                llIngredientsContainer.addView(tv)
            }
        }

        showRecipeDetails()
    }

    private fun displayErrorMessage(message: String) {
        val requestOption = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)

        Glide.with(this)
            .setDefaultRequestOptions(requestOption)
            .load(R.drawable.ic_launcher_background)
            .into(ivRecipeImage)

        tvRecipeTitle.text = "Error retrieving recipe..."
        tvRecipeSocialScore.text = ""
        val tv = AppCompatTextView(this)
        tv.textSize = 15f
        tv.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tv.text = if (message.isNotEmpty()) {
             message
        } else {
            "Error"
        }

        llIngredientsContainer.removeAllViews()
        llIngredientsContainer.addView(tv)

    }

    private fun showRecipeDetails() {
        recipeDetailsViewContainer.visibility = View.VISIBLE
        showLoading(false)
    }

    private fun retrieveRecipeData(recipeId: String) {
        recipeDetailsViewModel.retrieveRecipe(recipeId)
    }
}
