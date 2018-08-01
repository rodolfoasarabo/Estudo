package com.poppin.movies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poppin.movies.R;
import com.poppin.movies.models.Movies;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<Movies> movies;
    private Context ctx;
    private MoviesAdapter.OnItemClickListener clickListener;
    public void setClickListener(MoviesAdapter.OnItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }

    public MoviesAdapter(List<Movies> movies, Context ctx){
        this.movies = movies;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Movies movie = movies.get(position);
        Glide.with(ctx).load(movie.Poster).into(holder.moviePoster);
        holder.txtMovieYear.setText(movie.Year);
        holder.txtTitle.setText(movie.Title);

    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTitle;
        TextView txtMovieYear;
        ImageView moviePoster;
        CardView movieCard;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMovieYear = itemView.findViewById(R.id.txtMovieYear);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieCard = itemView.findViewById(R.id.movieCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface OnItemClickListener{
        void onClick(View view, int position, String imdbID);
    }

}
