package com.willyweathermachinetest.repositories

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.willyweathermachinetest.BuildConfig

import com.willyweathermachinetest.model.topRateMoviesList.TopRatedMovie
import com.willyweathermachinetest.model.topRateMoviesList.ServerResponse
import com.willyweathermachinetest.service.ApiService
import com.willyweathermachinetest.service.NetworkConnection
import com.willyweathermachinetest.service.RestClient
import retrofit2.Call
import retrofit2.Callback


// It act as a Repositry
class PlayerDataSource : PageKeyedDataSource<Long, TopRatedMovie?>() {
    private var service: ApiService? = null
    private val limit = 20
    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, TopRatedMovie?>) {
        Log.d("Intial Load", "Intial Load")
        service = RestClient(NetworkConnection()).client
        service?.getListOfTopRatedMovies(BuildConfig.apiKey, 1)?.enqueue(object : Callback<ServerResponse?> {
            override fun onResponse(call: Call<ServerResponse?>, serverResponse: retrofit2.Response<ServerResponse?>) {
                Log.d("Inside Success","Inside Success"+serverResponse.body()?.results.toString());
                if (serverResponse != null && serverResponse.body() != null) {
                    val topRatedMovieList: List<TopRatedMovie?>? = serverResponse?.body()?.results
                    callback.onResult(topRatedMovieList!!, null, 2.toLong())
                }
            }

            override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                Log.d("Inside Failure",t.message+"");
            }
        })
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, TopRatedMovie?>) {
        Log.d("Load Before", "Load Before")
        service = RestClient(NetworkConnection()).client
        Log.d("Param key", params.key.toString() + "")
        service?.getListOfTopRatedMovies(BuildConfig.apiKey,params.key)?.enqueue(object : Callback<ServerResponse?> {
            override fun onResponse(call: Call<ServerResponse?>, serverResponse: retrofit2.Response<ServerResponse?>) {
                val topRatedMovieList: List<TopRatedMovie?>? = serverResponse?.body()?.results
                if (serverResponse != null && serverResponse.body() != null) {
                    val topRatedMovieList: List<TopRatedMovie?>? = serverResponse?.body()?.results
                    val key: Long
                    key = if (params.key > 1) {
                        params.key - 1
                    } else {
                        0
                    }
                    callback.onResult(topRatedMovieList!!, key)
                }
            }

            override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {}
        })
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, TopRatedMovie?>) {
        Log.d("Load After", "Load After")
        service = RestClient(NetworkConnection()).client
        Log.d("Param key", params.key.toString() + "")
        service?.getListOfTopRatedMovies(BuildConfig.apiKey,params.key)?.enqueue(object : Callback<ServerResponse?> {
            override fun onResponse(call: Call<ServerResponse?>, serverResponse: retrofit2.Response<ServerResponse?>) {
                val topRatedMovieList: List<TopRatedMovie?>? = serverResponse?.body()?.results
                if (serverResponse != null && serverResponse.body() != null) {
                    val topRatedMovieList: List<TopRatedMovie?>? = serverResponse?.body()?.results
                    callback.onResult(topRatedMovieList!!, params.key + 1)
                }
            }

            override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {}
        })
    }
}