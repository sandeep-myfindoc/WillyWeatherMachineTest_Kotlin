package com.willyweathermachinetest.service

import com.willyweathermachinetest.model.topRateMoviesList.ServerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/top_rated")
    fun getListOfTopRatedMovies(@Query("api_key") apiKey: String, @Query("page") page: Long): Call<ServerResponse?>
}