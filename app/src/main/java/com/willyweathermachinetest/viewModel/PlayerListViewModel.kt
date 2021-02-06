package com.willyweathermachinetest.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.willyweathermachinetest.model.topRateMoviesList.TopRatedMovie
import com.willyweathermachinetest.repositories.PlayerDataSourceFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

//JsonObject
class PlayerListViewModel(application: Application) : AndroidViewModel(application) {
    var topRatedMoviePagedList: LiveData<PagedList<TopRatedMovie>>
    private val executor: Executor

    init {
        Log.d("inside ","init")
        val factory = PlayerDataSourceFactory()
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(4)
                .build()
        executor = Executors.newFixedThreadPool(5)
        topRatedMoviePagedList = LivePagedListBuilder(factory, config)
                .setFetchExecutor(executor)
                .build()
    }
}