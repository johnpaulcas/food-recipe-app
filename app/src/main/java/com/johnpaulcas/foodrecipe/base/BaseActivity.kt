package com.johnpaulcas.foodrecipe.base

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.johnpaulcas.foodrecipe.R

/**
 * Created by johnpaulcas on 08/06/2020.
 */
abstract class BaseActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
        init(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        val constrainLayout = layoutInflater.inflate(R.layout.activity_base, null)
        val frameLayout = constrainLayout.findViewById<FrameLayout>(R.id.flMainContainer)

        progressBar = constrainLayout.findViewById<ProgressBar>(R.id.pbLoading)

        layoutInflater.inflate(layoutResID, frameLayout, true)

        super.setContentView(constrainLayout)
    }

    protected fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    abstract fun getLayoutResourceId(): Int

    abstract fun init(bundle: Bundle?)

}