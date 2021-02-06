package com.willyweathermachinetest.view.TopRatedMoviesList

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.snackbar.Snackbar
import com.willyweathermachinetest.R
import com.willyweathermachinetest.broadcastreceiver.ConnectivityReceiver
import com.willyweathermachinetest.broadcastreceiver.ConnectivityReceiver.ConnectivityReceiverListener
import com.willyweathermachinetest.databinding.ActivityMainBinding
import com.willyweathermachinetest.util.CommonUtility
import com.willyweathermachinetest.view.SuperActivity
import com.willyweathermachinetest.view.WillyWeatherApplication
import com.willyweathermachinetest.viewModel.PlayerListViewModel

class TopRatedMoviesListActivity : SuperActivity(), ConnectivityReceiverListener {
    private var binding: ActivityMainBinding? = null
    private var viewModel: PlayerListViewModel? = null
    private var adapter: TopRatedMoviesListAdapter? = null
    private var receiver: ConnectivityReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        receiver = ConnectivityReceiver()
        updateToolbarWithoutBackButton(resources.getString(R.string.title_top_rated_movies))
        WillyWeatherApplication?.instance?.setConnectivityListener(this)
        initRecyclerView()
        binding!!.swipeLayout.setOnRefreshListener(onRefreshListener)
        binding?.swipeLayout?.isRefreshing = true
        loadData()


    }
    fun loadData(){
        if (!CommonUtility(this).checkInternetConnection()) {
            binding?.swipeLayout?.isRefreshing = false
            Log.d("close","==loading===")
            val snackbar = Snackbar
                .make(binding!!.coordinatorLayout, resources.getString(R.string.msg_offline), Snackbar.LENGTH_LONG)
            snackbar.show()
        }else {
            viewModel = ViewModelProviders.of(this).get(PlayerListViewModel::class.java)
            binding?.viewModel = viewModel

            if(!(viewModel?.topRatedMoviePagedList?.hasActiveObservers()!=null && viewModel?.topRatedMoviePagedList?.hasActiveObservers()!!)){
                Log.d("observer","not attached");
                viewModel?.topRatedMoviePagedList?.observe(this, Observer { movies ->
                    adapter?.submitList(movies)
                    if (binding!!.swipeLayout.isRefreshing) {
                        binding?.swipeLayout?.isRefreshing = false
                    }
                })
            }else{
                binding?.swipeLayout?.isRefreshing = false
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(receiver, intentFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_player_list_menu,menu)
        return true

    }
    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    private fun initRecyclerView() {
        adapter = TopRatedMoviesListAdapter(this)
        val layoutManager = LinearLayoutManager(this)
        binding?.rvPlayers?.layoutManager = layoutManager
        binding?.rvPlayers?.adapter = adapter
    }



    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            Log.d("Connected","ksdmcklsmcls")
            loadData()
        } else {
            val snackbar = Snackbar
                    .make(binding!!.coordinatorLayout, resources.getString(R.string.msg_offline), Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    var onRefreshListener = OnRefreshListener {
        loadData()
    }
}