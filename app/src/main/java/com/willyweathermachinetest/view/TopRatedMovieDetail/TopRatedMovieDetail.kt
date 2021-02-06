package com.willyweathermachinetest.view.TopRatedMovieDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.willyweathermachinetest.R
import com.willyweathermachinetest.databinding.ActivityPlayerDetailBinding
import com.willyweathermachinetest.model.topRateMoviesList.TopRatedMovie
import com.willyweathermachinetest.sharedpreferences.SharedPreferencesName
import com.willyweathermachinetest.view.SuperActivity

class TopRatedMovieDetail : SuperActivity() {
    private var topRatedMovie: TopRatedMovie? = null
    private var binding: ActivityPlayerDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player_detail)
        updateToolbar(resources.getString(R.string.title_movie_detail))
        if (intent != null) {
            val topRatedMovieString = intent.getStringExtra(SharedPreferencesName.TOP_RATED_MOVIE_DETAIL)
            topRatedMovie = Gson().fromJson(topRatedMovieString, TopRatedMovie::class.java)
            binding?.movieDetail = topRatedMovie
        }
    }

    
}