package com.rodolfo.movies.adapter

import android.content.Context
import android.support.v7.widget.CardView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rodolfo.movies.R
import com.rodolfo.movies.models.Movies

class MoviesAdapter(val movies: List<Movies>, val ctx: Context) : RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>() {

    private lateinit var clickListener: MoviesAdapter.OnItemClickListener

    fun setClickListener(itemClickListener: MoviesAdapter.OnItemClickListener){
        this.clickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem_movie, parent, false)
        return MoviesAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies?.size ?: 0
    }

    override fun onBindViewHolder(holder: MoviesAdapterViewHolder, position: Int) {
        val options: RequestOptions = RequestOptions()
        val movie: Movies = movies.get(position)
        if (movie.Poster.equals("N/A")){
            options.fitCenter()
            Glide.with(ctx).load(R.drawable.ic_movie).apply(options).into(holder.moviePoster)
        } else {
            options.centerCrop()
            Glide.with(ctx).load(movie.Poster).apply(options).into(holder.moviePoster)
        }
        holder.txtMovieYear.text = movie.Year
        holder.txtTitle.text = movie.Title
    }

    inner class MoviesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtMovieYear: TextView = itemView.findViewById(R.id.txtMovieYear)
        val moviePoster: ImageView = itemView.findViewById(R.id.moviePoster)
        val movieCard: CardView = itemView.findViewById(R.id.movieCard)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (clickListener != null){
                clickListener.onClick(view, adapterPosition, movies.get(adapterPosition).imdbID)
            }
        }

    }

    interface OnItemClickListener {
        fun onClick(view: View, position: Int, imdbID: String?)
    }
}