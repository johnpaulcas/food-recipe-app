package com.johnpaulcas.foodrecipe.adapters.viewholder

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.johnpaulcas.foodrecipe.R
import com.johnpaulcas.foodrecipe.adapters.OnRecipeListener
import com.johnpaulcas.foodrecipe.models.Recipe
import kotlinx.android.synthetic.main.layout_category_item.view.*

/**
 * Created by johnpaulcas on 13/06/2020.
 */
class CategoryViewHolder(
    view: View,
    val listener: OnRecipeListener
): RecyclerView.ViewHolder(view) {
    fun bindData(recipe: Recipe?) {
        val uriPath = Uri.parse("android.resource://com.johnpaulcas.foodapp/drawable/${recipe?.imageUrl}")

        val glideRequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)

        Glide.with(itemView.context)
            .setDefaultRequestOptions(glideRequestOptions)
            .load(uriPath)
            .into(itemView.civCategory)


        itemView.actvCategoryTitle.text = recipe?.title

        itemView.setOnClickListener {
            listener.onCategoryClick(itemView.actvCategoryTitle.text.toString())
        }
    }
}