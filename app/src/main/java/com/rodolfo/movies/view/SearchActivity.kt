package com.rodolfo.movies.view

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.rodolfo.movies.R
import com.rodolfo.movies.adapter.MoviesAdapter
import com.rodolfo.movies.base.BaseActivity
import com.rodolfo.movies.models.Movies
import com.rodolfo.movies.models.RetornoBusca
import com.rodolfo.movies.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity(), SearchView.OnQueryTextListener, MoviesAdapter.OnItemClickListener {

    lateinit var txtEmptyView: TextView

    lateinit var emptyView: ConstraintLayout
    lateinit var loaderFull: ConstraintLayout

    lateinit var retornoBusca: RetornoBusca
    lateinit var moviesList: List<Movies>
    lateinit var rvMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        refLayout = R.layout.activity_search
        super.onCreate(savedInstanceState)
        hideKeyboard()
        configViews()
        emptyView(true, R.string.empty_nao_buscou)
        showLoader(loaderFull, false)
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    private fun configViews() {
        val sv = findViewById<SearchView>(R.id.svMovies)

        sv.queryHint = "Digite sua busca"
        rvMovies = findViewById(R.id.rvMovies)
        emptyView = findViewById(R.id.emptyView)
        loaderFull = findViewById(R.id.loaderFull)
        txtEmptyView = findViewById(R.id.txtEmptyView)
        sv.setOnQueryTextListener(this)

    }

    private fun buscar(name: String) {
        val chamada = service.searchMovie(Constants.API_KEY, name, "movie")
        chamada.enqueue(object : Callback<RetornoBusca> {
            override fun onResponse(call: Call<RetornoBusca>, response: Response<RetornoBusca>) {
                showLoader(loaderFull, false)

                if (response.body() != null) {
                    retornoBusca = response.body()!!
                    if (retornoBusca.Search != null) {
                        emptyView(false, 0)
                        moviesList = retornoBusca.Search!!
                        setupRecycler(moviesList.sortedWith(compareByDescending {it.Year}))
                    } else {
                        emptyView(true, R.string.empty_nao_encontrado)
                    }
                }
            }

            override fun onFailure(call: Call<RetornoBusca>, t: Throwable) {
                showLoader(loaderFull, false)

                Log.e("Error", t.message)
            }
        })
    }

    private fun emptyView(b: Boolean, valor: Int) {
        if (b) {
            txtEmptyView.setText(valor)
            emptyView.visibility = View.VISIBLE
            rvMovies.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
        }
    }

    private fun setupRecycler(moviesList: List<Movies>) {
        rvMovies.setHasFixedSize(false)
        rvMovies.visibility = View.VISIBLE
        val layoutManager = GridLayoutManager(this, 2)
        rvMovies.layoutManager = layoutManager
        val adapter = MoviesAdapter(moviesList, this)
        rvMovies.adapter = adapter
        adapter.setClickListener(this)
    }

    override fun onQueryTextSubmit(text: String): Boolean {
        showLoader(loaderFull, true)
        rvMovies.visibility = View.GONE
        emptyView(false, 0)
        hideKeyboard()
        buscar(text)
        return true
    }

    override fun onQueryTextChange(s: String): Boolean {
        if (s == "") {
            emptyView(true, R.string.empty_nao_buscou)
        }
        return true
    }


    override fun onClick(view: View, position: Int, imdbID: String?) {
        val i = Intent(this, DetailsActivity::class.java)
        i.putExtra(Constants.IMDB_ID, imdbID)
        startActivity(i)

    }
}
