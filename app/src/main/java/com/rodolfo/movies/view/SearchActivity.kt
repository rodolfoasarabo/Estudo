package com.rodolfo.movies.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.rodolfo.movies.R
import com.rodolfo.movies.view.adapter.MoviesAdapter
import com.rodolfo.movies.base.BaseActivity
import com.rodolfo.movies.models.Movies
import com.rodolfo.movies.utils.Constants
import com.rodolfo.movies.view.viewmodel.SearchViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.standalone.KoinComponent

class SearchActivity : BaseActivity(), SearchView.OnQueryTextListener, MoviesAdapter.OnItemClickListener, KoinComponent {

    lateinit var txtEmptyView: TextView

    lateinit var emptyView: ConstraintLayout
    lateinit var loaderFull: ConstraintLayout

    lateinit var rvMovies: RecyclerView

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        refLayout = R.layout.activity_search
        super.onCreate(savedInstanceState)
        hideKeyboard()
        configViews()
        emptyView(true, R.string.empty_nao_buscou)
        showLoader(loaderFull, false)
        observeChanges()
    }

    private fun observeChanges() {
        viewModel.movies().observe(this, Observer { retornoBusca ->
            showLoader(loaderFull, false)
            retornoBusca.Search?.let { movieList ->
                setupRecycler(movieList.sortedWith(compareByDescending { movie -> movie.Year }))
            } ?: emptyView(true, R.string.empty_nao_encontrado)
        })
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
        viewModel.searchMovie(name)
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
        val layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 2)
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
