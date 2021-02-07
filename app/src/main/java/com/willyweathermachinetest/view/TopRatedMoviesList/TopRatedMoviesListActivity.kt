package com.willyweathermachinetest.view.TopRatedMoviesList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.willyweathermachinetest.view.Wishlist.WishlistActivity
import com.google.android.material.snackbar.Snackbar
import com.willyweathermachinetest.R
import com.willyweathermachinetest.broadcastreceiver.ConnectivityReceiver
import com.willyweathermachinetest.databinding.ActivityMainBinding
import com.willyweathermachinetest.util.CommonUtility
import com.willyweathermachinetest.view.SuperActivity
import com.willyweathermachinetest.viewModel.TopRatedMovieListViewModel

class TopRatedMoviesListActivity : SuperActivity(){
    private var binding: ActivityMainBinding? = null
    private var viewModel: TopRatedMovieListViewModel? = null
    private var adapter: TopRatedMoviesListAdapter? = null
    private var receiver: ConnectivityReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(TopRatedMovieListViewModel::class.java)
        binding?.viewModel = viewModel
        receiver = ConnectivityReceiver()
        updateToolbarWithoutBackButton(resources.getString(R.string.title_top_rated_movies))
        initRecyclerView()
        binding!!.swipeLayout.setOnRefreshListener(onRefreshListener)
        if (!CommonUtility(this).checkInternetConnection()) {
            val snackbar = Snackbar
                    .make(binding!!.coordinatorLayout, resources.getString(R.string.msg_offline), Snackbar.LENGTH_LONG)
            snackbar.show()
        }
        binding?.swipeLayout?.isRefreshing = true
        fetchTopRatedMoviesList()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_player_list_menu,menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intent = Intent(this, WishlistActivity::class.java)
        when(item.getItemId())

        {
            R.id.menuFavorite-> startActivity(intent)
            else-> Log.d("Invalid","");


        }

        return true
    }

    private fun initRecyclerView() {
        adapter = TopRatedMoviesListAdapter(this)
        val layoutManager = LinearLayoutManager(this)
        binding?.rvPlayers?.layoutManager = layoutManager
        binding?.rvPlayers?.adapter = adapter
    }

    //add observer
    private fun fetchTopRatedMoviesList() {
        viewModel?.topRatedMoviePagedList?.observe(this, Observer { players ->
            binding?.swipeLayout?.isRefreshing = false
            adapter?.submitList(players)
            if (binding!!.swipeLayout.isRefreshing) {
                binding!!.swipeLayout.isRefreshing = false
            }
        })
    }



    var onRefreshListener = OnRefreshListener {
        binding?.swipeLayout?.isRefreshing=false
    }
}