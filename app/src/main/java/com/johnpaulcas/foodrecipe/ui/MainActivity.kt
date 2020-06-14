package com.johnpaulcas.foodrecipe.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnpaulcas.foodrecipe.R
import com.johnpaulcas.foodrecipe.adapters.OnRecipeListener
import com.johnpaulcas.foodrecipe.adapters.RecipeRecyclerAdapter
import com.johnpaulcas.foodrecipe.base.BaseActivity
import com.johnpaulcas.foodrecipe.models.Recipe
import com.johnpaulcas.foodrecipe.viewmodels.MainViewModel
import com.johnpaulcas.foodrecipe.widgets.VerticalSpacingItemDecorator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), OnRecipeListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeAdapter: RecipeRecyclerAdapter

    override fun getLayoutResourceId(): Int =
        R.layout.activity_main

    override fun init(bundle: Bundle?) {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setSupportActionBar(toolbar as Toolbar)

        setupRecyclerView()
        setupSearchView()
        subscribeObserver()

        showLoading(false)

        if (!mainViewModel.isViewingRecipes) {
            displaySearchCategories()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_catories) {
            displaySearchCategories()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeRecyclerAdapter(this)
        rvRecipes.addItemDecoration(VerticalSpacingItemDecorator(30))
        rvRecipes.adapter = recipeAdapter
        rvRecipes.layoutManager = LinearLayoutManager(this)

        rvRecipes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (rvRecipes.canScrollVertically(1) && !mainViewModel.isPerformingQuery) {
                    mainViewModel.searchNextPage()
                }
            }
        })
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    recipeAdapter.displayLoading()
                    searchRecipeApi(it, 1)
                    searchView.clearFocus()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun subscribeObserver() {
        mainViewModel.recipes.observe(this, Observer {
            it?.let { recipes ->
                if (mainViewModel.isViewingRecipes) {
                    mainViewModel.isPerformingQuery = false
                    recipeAdapter.setNewRecipes(recipes)
                }
            }
        })

        mainViewModel.isQueryExhausted.observe(this, Observer {
            it?.let { isQueryExhausted ->
                if (isQueryExhausted) {
                    recipeAdapter.setQueryExhausted()
                }
            }
        })

    }

    private fun searchRecipeApi(query: String, pageNumber: Int) {
        mainViewModel.searchRecipeApi(query, pageNumber)
    }

    private fun displaySearchCategories() {
        mainViewModel.isViewingRecipes = false
        recipeAdapter.displaySearchCategoryList()
    }

    override fun onRecipeClick(recipe: Recipe?) {
        val intent = Intent(this, RecipeDetailsActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }

    override fun onCategoryClick(category: String) {
        recipeAdapter.displayLoading()
        searchRecipeApi(category, 1)
        searchView.clearFocus()
    }

    override fun onBackPressed() {
        if (mainViewModel.onBackPressed()) {
            super.onBackPressed()
        } else {
            displaySearchCategories()
        }
    }
}
