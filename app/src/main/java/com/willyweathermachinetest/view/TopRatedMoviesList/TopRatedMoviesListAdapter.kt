package com.willyweathermachinetest.view.TopRatedMoviesList

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.willyweathermachinetest.R
import com.willyweathermachinetest.database.WishListDatabase
import com.willyweathermachinetest.database.Wishlist
import com.willyweathermachinetest.databinding.LayoutSubitemMovieBinding

import com.willyweathermachinetest.model.topRateMoviesList.TopRatedMovie
import com.willyweathermachinetest.sharedpreferences.SharedPreferencesName
import com.willyweathermachinetest.view.TopRatedMovieDetail.TopRatedMovieDetail
import com.willyweathermachinetest.view.TopRatedMoviesList.TopRatedMoviesListAdapter.PlayerViewHolder
import com.willyweathermachinetest.view.WillyWeatherApplication

class TopRatedMoviesListAdapter : PagedListAdapter<TopRatedMovie, PlayerViewHolder> {
    private var mContext: Context? = null
    private var emptyLabel: String? = null

    constructor(mContext: Context?) : super(USER_COMPARATOR) {
        this.mContext = mContext
        emptyLabel = "__"
    }

    protected constructor(diffCallback: DiffUtil.ItemCallback<TopRatedMovie?>) : super(diffCallback) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        var binding: LayoutSubitemMovieBinding
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.layout_subitem_movie,
            parent,
            false
        )
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        var topRatedMovie: TopRatedMovie? = getItem(position)
        holder.binding.movie = topRatedMovie
        if (WishListDatabase.getInstance(mContext)?.wishListDao()?.isWish(topRatedMovie!!.id) == 1) {
            holder.binding.imgLike.setImageResource(R.drawable.like)
        } else {
            holder.binding.imgLike.setImageResource(R.drawable.dislike)
        }
    }

    inner class PlayerViewHolder(var binding: LayoutSubitemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.getRoot().setOnClickListener(View.OnClickListener {
                val position: Int = getAdapterPosition()
                if (position != RecyclerView.NO_POSITION) {
                    val selectedMovie: TopRatedMovie? = getItem(position)
                    val intent = Intent(mContext, TopRatedMovieDetail::class.java)
                    var key: String? = null
                    var value: String? = null
                    key = "movie"
                    value = Gson().toJson(selectedMovie, TopRatedMovie::class.java)
                    intent.putExtra(SharedPreferencesName.TOP_RATED_MOVIE_DETAIL, value)
                    mContext?.startActivity(intent)
                }
            })
            binding.imgLike.setOnClickListener(View.OnClickListener {
                val position: Int = getAdapterPosition()
                if (position != RecyclerView.NO_POSITION) {
                    var selectedMovie: TopRatedMovie? = getItem(position)
                    val wishlist = Wishlist()
                    wishlist.movieId = selectedMovie!!.id
                    wishlist.posterPath = selectedMovie!!.poster_path
                    wishlist.movieTitle = selectedMovie!!.title
                    wishlist.releaseDate = selectedMovie!!.release_date
                    if (WishListDatabase.getInstance(mContext)?.wishListDao()?.isWish(selectedMovie!!.id) != 1) {
                        binding.imgLike.setImageResource(R.drawable.like)
                        WishListDatabase.getInstance(mContext)?.wishListDao()?.addTowishdata(wishlist)
                    } else {
                        binding.imgLike.setImageResource(R.drawable.dislike)
                        WishListDatabase.getInstance(mContext)?.wishListDao()?.delete(wishlist)
                    }
                }
            })
        }

    }

    companion object {
        private val USER_COMPARATOR: DiffUtil.ItemCallback<TopRatedMovie> =
            object : DiffUtil.ItemCallback<TopRatedMovie>() {
                override fun areItemsTheSame(
                    oldItem: TopRatedMovie,
                    newItem: TopRatedMovie
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: TopRatedMovie,
                    newItem: TopRatedMovie
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}


