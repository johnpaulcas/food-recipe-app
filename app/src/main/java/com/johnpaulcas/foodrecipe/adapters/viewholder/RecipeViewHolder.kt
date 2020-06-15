package com.johnpaulcas.foodrecipe.adapters.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.johnpaulcas.foodrecipe.R
import com.johnpaulcas.foodrecipe.adapters.OnRecipeListener
import com.johnpaulcas.foodrecipe.models.Recipe
import kotlinx.android.synthetic.main.recipe_item.view.*

/**
 * Created by johnpaulcas on 13/06/2020.
 */
class RecipeViewHolder(
    view: View,
    private val onRecipeListener: OnRecipeListener
): RecyclerView.ViewHolder(view) {

    fun bindData(recipe: Recipe?) {
        val glideRequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)

        Glide.with(itemView.context)
            .setDefaultRequestOptions(glideRequestOptions)
            .load(recipe?.imageUrl)
            .into(itemView.recipe_image)

        itemView.recipe_title.text = recipe?.title
        itemView.recipe_publisher.text = recipe?.publisher
        itemView.recipe_social_score.text =
            Math.round(recipe?.socialRank ?: 0.0F).toString()

        itemView.setOnClickListener {
            onRecipeListener.onRecipeClick(recipe)
        }
    }

}