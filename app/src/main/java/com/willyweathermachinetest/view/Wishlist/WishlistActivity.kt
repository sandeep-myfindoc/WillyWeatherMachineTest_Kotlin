package com.willyweathermachinetest.view.Wishlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.willyweathermachinetest.database.Wishlist
import com.willyweathermachinetest.R
import com.willyweathermachinetest.databinding.ActivityWishlistBinding
import com.willyweathermachinetest.view.SuperActivity
import com.willyweathermachinetest.viewModel.WishListViewModel


class WishlistActivity : SuperActivity() {
    private var binding: ActivityWishlistBinding? = null
    private var viewModel: WishListViewModel? = null
    private var adapter: WishListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wishlist)
        viewModel = ViewModelProviders.of(this).get(WishListViewModel::class.java)
        binding?.viewModel = viewModel
        updateToolbar(resources.getString(R.string.title_wishlist))
        initRecylerView()
        fetchWishListData()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.wishlist_list_menu,menu)
        val myActionMenuItem = menu!!.findItem(R.id.menuSearch)
        var searchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { // Toast like print
                myActionMenuItem.collapseActionView()
                return true
            }

            override fun onQueryTextChange(s: String): Boolean { // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                adapter?.filter?.filter(s)
                return true
            }
        })
        return true

    }

    private fun fetchWishListData() {
        viewModel?.getWishList()?.observe(this, Observer {
            wishList->
            adapter?.response = wishList as List<Wishlist>?
            adapter?.filteredResponse = wishList as List<Wishlist>?
            adapter?.notifyDataSetChanged()

        })
    }

    private fun initRecylerView() {
        adapter = WishListAdapter(this)
        val layoutManager = LinearLayoutManager(this)
        binding?.rvWishListMovies?.layoutManager = layoutManager
        binding?.rvWishListMovies?.adapter = adapter
    }
}
