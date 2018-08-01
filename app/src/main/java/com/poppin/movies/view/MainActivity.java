package com.poppin.movies.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.poppin.movies.R;
import com.poppin.movies.Retrofit.MoviesService;
import com.poppin.movies.adapter.MoviesAdapter;
import com.poppin.movies.models.Movies;
import com.poppin.movies.models.RetornoBusca;
import com.poppin.movies.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MoviesAdapter.OnItemClickListener {

    private Retrofit retrofit;
    private MoviesService service;

    private TextView txt;

    private RetornoBusca retornoBusca;
    private List<Movies> moviesList;
    private RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configRetrofit();

        SearchView sv = findViewById(R.id.svMovies);
        rvMovies = findViewById(R.id.rvMovies);

        sv.setOnQueryTextListener(this);
    }

    private void configRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MoviesService.class);
    }

    private void buscar(String name){
        Call<RetornoBusca> chamada = service.searchMovie(Constants.API_KEY, name, "movie");
        chamada.enqueue(new Callback<RetornoBusca>() {
            @Override
            public void onResponse(Call<RetornoBusca> call, Response<RetornoBusca> response) {
                if (response.body() != null) {
                    Log.e("Teste", response.body().toString());
                    retornoBusca = response.body();
                    moviesList = retornoBusca.Search;
                    for (Movies f : retornoBusca.Search) {
                        Log.e("Teste", f.Title);
                        Log.e("Teste", f.Year);
                        Log.e("Teste", f.imdbID);
                    }

                    setupRecycler(moviesList);
                }
            }

            @Override
            public void onFailure(Call<RetornoBusca> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void setupRecycler(List<Movies> moviesList) {
        rvMovies.setHasFixedSize(false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(layoutManager);
        MoviesAdapter adapter = new MoviesAdapter(moviesList, this);
        rvMovies.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String text) {
        Log.e("Texto", text);
        buscar(text);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onClick(View view, int position, String imdbID) {

    }
}
