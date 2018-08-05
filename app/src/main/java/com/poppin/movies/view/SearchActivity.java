package com.poppin.movies.view;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poppin.movies.R;
import com.poppin.movies.retrofit.MoviesService;
import com.poppin.movies.adapter.MoviesAdapter;
import com.poppin.movies.models.Movies;
import com.poppin.movies.models.RetornoBusca;
import com.poppin.movies.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MoviesAdapter.OnItemClickListener {

    private Retrofit retrofit;
    private MoviesService service;

    private TextView txt;
    private TextView txtEmptyView;

    private ConstraintLayout emptyView;
    private ProgressBar progressBar;

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
        emptyView = findViewById(R.id.emptyView);
        progressBar = findViewById(R.id.progressBar);
        txtEmptyView = findViewById(R.id.txtEmptyView);
        sv.setQueryHint("Digite sua busca");
        emptyView(true, R.string.empty_nao_buscou);
        progress(false);
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
                progress(false);
                if (response.body() != null) {
                    Log.e("Teste", response.body().toString());
                    retornoBusca = response.body();
                    if(retornoBusca.Search != null) {
                        emptyView(false, 0);
                        moviesList = retornoBusca.Search;
                        setupRecycler(moviesList);
                    } else {
                        emptyView(true, R.string.empty_nao_encontrado);
                    }
                }
            }

            @Override
            public void onFailure(Call<RetornoBusca> call, Throwable t) {
                progress(false);
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void emptyView(boolean b, int valor){
        if (b){
            txtEmptyView.setText(valor);
            emptyView.setVisibility(View.VISIBLE);
            rvMovies.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            rvMovies.setVisibility(View.VISIBLE);
        }
    }

    private void progress(boolean b){
        if(b)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
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
        progress(true);
        emptyView(false, 0);
        buscar(text);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.equals("")){
            emptyView(true, R.string.empty_nao_buscou);
        }
        return false;
    }



    @Override
    public void onClick(View view, int position, String imdbID) {

    }
}
