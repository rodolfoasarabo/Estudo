package com.poppin.movies.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poppin.movies.R;
import com.poppin.movies.adapter.MoviesAdapter;
import com.poppin.movies.base.BaseActivity;
import com.poppin.movies.models.Movies;
import com.poppin.movies.models.RetornoBusca;
import com.poppin.movies.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, MoviesAdapter.OnItemClickListener {

    private TextView txtEmptyView;

    private ConstraintLayout emptyView;
    private ConstraintLayout loaderFull;

    private RetornoBusca retornoBusca;
    private List<Movies> moviesList;
    private RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        refLayout = R.layout.activity_search;
        super.onCreate(savedInstanceState);
        hideKeyboard();
        SearchView sv = findViewById(R.id.svMovies);
        rvMovies = findViewById(R.id.rvMovies);
        emptyView = findViewById(R.id.emptyView);
        loaderFull = findViewById(R.id.loaderFull);
        txtEmptyView = findViewById(R.id.txtEmptyView);
        sv.setQueryHint("Digite sua busca");
        emptyView(true, R.string.empty_nao_buscou);
        showLoader(loaderFull, false);
        sv.setOnQueryTextListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }

    private void buscar(String name){
        Call<RetornoBusca> chamada = service.searchMovie(Constants.API_KEY, name, "movie");
        chamada.enqueue(new Callback<RetornoBusca>() {
            @Override
            public void onResponse(Call<RetornoBusca> call, Response<RetornoBusca> response) {
                showLoader(loaderFull, false);

                if (response.body() != null) {
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
                showLoader(loaderFull, false);

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
        }
    }

    private void setupRecycler(List<Movies> moviesList) {
        rvMovies.setHasFixedSize(false);
        rvMovies.setVisibility(View.VISIBLE);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(layoutManager);
        MoviesAdapter adapter = new MoviesAdapter(moviesList, this);
        rvMovies.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String text) {
        showLoader(loaderFull, true);
        rvMovies.setVisibility(View.GONE);
        emptyView(false, 0);
        hideKeyboard();
        buscar(text);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.equals("")){
            emptyView(true, R.string.empty_nao_buscou);
        }
        return true;
    }



    @Override
    public void onClick(View view, int position, String imdbID) {
        Intent i =new Intent(this, DetailsActivity.class);
        i.putExtra(Constants.IMDB_ID, imdbID);
        startActivity(i);

    }
}
