package com.johnpaulcas.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.johnpaulcas.foodrecipe.R
import com.johnpaulcas.foodrecipe.adapters.viewholder.CategoryViewHolder
import com.johnpaulcas.foodrecipe.adapters.viewholder.RecipeViewHolder
import com.johnpaulcas.foodrecipe.models.Recipe
import com.johnpaulcas.foodrecipe.util.Constants
import java.lang.Exception

/**
 * Created by johnpaulcas on 13/06/2020.
 */
class RecipeRecyclerAdapter(
    val onRecipeListener: OnRecipeListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val RECIPE_TYPE = 1
    private val LOADING_TYPE = 2
    private val CATEGORY_TYPE = 3
    private val EXHAUSTED_TYPE = 4

    private var recipes: List<Recipe>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            LOADING_TYPE -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_loading_list_item, parent, false)
                return RecipeViewHolder(view, onRecipeListener)
            }
            EXHAUSTED_TYPE -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_search_exhausted, parent, false)
                return RecipeViewHolder(view, onRecipeListener)
            }
            CATEGORY_TYPE -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_category_item, parent, false)
                return CategoryViewHolder(view, onRecipeListener)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_recipe_item, parent, false)
                return RecipeViewHolder(view, onRecipeListener)
            }
        }
    }

    override fun getItemCount(): Int = recipes?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemViewType = getItemViewType(position)
        if (itemViewType == RECIPE_TYPE) {
            recipes?.get(position).let {
                val recipe = it
                if (holder is RecipeViewHolder) {
                    holder.bindData(recipe)

                }
            }
        } else if (itemViewType == CATEGORY_TYPE) {
            recipes?.get(position).let {
                val recipe = it
                if (holder is CategoryViewHolder) {
                    holder.bindData(recipe)
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (recipes?.get(position)?.socialRank == -1.0f) {
            CATEGORY_TYPE
        } else if (recipes?.get(position)?.title.equals("LOADING...")) {
            LOADING_TYPE
        } else if (recipes?.get(position)?.title.equals("EXHAUSTED...")) {
            EXHAUSTED_TYPE
        } else if (
            position == (recipes?.size?.minus(1))
            && position != 0
            && !recipes?.get(position)?.title.equals("EXHAUSTED...")
        ) {
            LOADING_TYPE
        } else {
            RECIPE_TYPE
        }
    }

    fun setQueryExhausted() {
        try {
            hideLoading()
            if (this.recipes != null && !recipes?.get(
                    (recipes?.size ?: 0).minus(1)
                )?.title.equals("EXHAUSTED...")
            ) {
               addExhaustedData()
            }
        } catch (e: Exception) {
            addExhaustedData()
        }
    }

    private fun addExhaustedData() {
        val exhausted = Recipe("EXHAUSTED...", null, null, null, null, 0.0F)
        val list = recipes?.toMutableList()
        list?.add(exhausted)
        this.recipes = list?.toList()
        notifyDataSetChanged()
    }

    fun displayLoading() {
        if (!isLoading()) {
            val recipe = Recipe("LOADING...", null, null, null, null, 0.0F)
            val loadingList = listOf<Recipe>(recipe)
            this.recipes = loadingList
            notifyDataSetChanged()
        }
    }

    fun displaySearchCategoryList() {
        val searchCategories = mutableListOf<Recipe>()

        for ((category, categorySearch) in Constants.DEFAULT_SEARCH_CATEGORY.withIndex()) {
            val recipe = Recipe(
                categorySearch,
                null,
                null,
                null,
                Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[category],
                -1.0f
            )
            searchCategories.add(recipe)
        }

        this.recipes = searchCategories.toList()
        notifyDataSetChanged()
    }

    private fun hideLoading() {
        if (isLoading()) {
            recipes?.let {
                for (recipe in it) {
                    if (!recipe.title.isNullOrEmpty() && recipe.title.equals("LOADING...")) {
                        val list = recipes?.toMutableList()
                        list?.remove(recipe)
                        recipes = list?.toList()
                    }
                }
                notifyDataSetChanged()
            }
        }
    }

    private fun isLoading(): Boolean {
        if ((recipes?.size ?: 0) > 0) {
            if (recipes?.get(recipes?.size?.minus(1) ?: 0)?.title.equals("LOADING...")) {
                return true
            }
        }

        return false
    }

    fun setNewRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

}