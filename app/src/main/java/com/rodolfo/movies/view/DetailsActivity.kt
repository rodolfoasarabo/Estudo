package com.rodolfo.movies.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.rodolfo.movies.R
import com.rodolfo.movies.base.BaseActivity
import com.rodolfo.movies.databinding.ActivityDetailsBinding
import com.rodolfo.movies.models.MovieDetail
import com.rodolfo.movies.utils.Constants
import com.rodolfo.movies.view.viewmodel.DetailsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.standalone.KoinComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity(), KoinComponent {

    //Other
    private lateinit var imdbId: String
    lateinit var details: MovieDetail
    private lateinit var binding: ActivityDetailsBinding

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        super.onCreate(savedInstanceState)
        configViews()
        configToolbar()
        imdbId = intent.getSerializableExtra(Constants.IMDB_ID) as String
        binding.loader.loaderFull.showLoader(true)
        observeChanges()
        viewModel.getMovie(imdbId)
    }

    private fun observeChanges() {
        viewModel.movieDetail().observe(this, Observer {
            binding.loader.loaderFull.showLoader(false)
            details = it
            supportActionBar?.title = it.Title
            preencheTela()
        })
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun configViews() {
        val options = RequestOptions()
        options.fitCenter()
        Glide.with(this).load(R.mipmap.imdb).apply(options).into(binding.ivImdb)
        Glide.with(this).load(R.mipmap.rotten).apply(options).into(binding.ivRotten)
        Glide.with(this).load(R.mipmap.metacritic).apply(options).into(binding.ivMetacritic)
    }

    private fun configToolbar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
    }

    private fun preencheTela() {
        val options = RequestOptions()
        binding.cardImage.visibility = View.VISIBLE
        if (details.Poster == "N/A") {
            options.fitCenter()
            Glide.with(this).load(R.drawable.ic_movie).apply(options).into(binding.ivPoster)
        } else {
            options.centerCrop()
            Glide.with(this).load(details.Poster).apply(options).into(binding.ivPoster)
        }

        if (details.Ratings!!.isNotEmpty()) {
            binding.llRatings.visibility = View.VISIBLE
            binding.tvRatings.visibility = View.VISIBLE
        }

        for (r in details.Ratings!!) {
            when (r.Source) {
                "Internet Movie Database" -> {
                    binding.llImdb.visibility = View.VISIBLE
                    binding.tvValueImdb.text = r.Value
                }
                "Rotten Tomatoes" -> {
                    binding.llRotten.visibility = View.VISIBLE
                    binding.tvValueRotten.text = r.Value
                }
                "Metacritic" -> {
                    binding.llMetacritic.visibility = View.VISIBLE
                    binding.tvValueMetacritic.text = r.Value
                }
            }
        }

        val writers = StringBuilder()

        for (writer: String in details.Writer!!.split("), ")) {
            writers.append("● $writer)\n")
        }

        binding.tvMovieRelease.text = ("Released: " + details.Released!!)
        binding.tvRated.text = ("Rated: " + details.Rated!!)
        binding.tvDirector.text = ("Director: " + details.Director!!)
        binding.tvWriter.text = ("Writer")
        binding.tvWriterValue.text = writers
        binding.tvRatings.text = ("Ratings:")
        binding.tvDescription.text = ("Description:")
        binding.tvDescriptionValue.text = details.Plot

        val genres = details.Genre!!.split(", ")

        for (genre in genres) {
            val chip = Chip(binding.chipGroupGenre.context)
            chip.text = genre
            chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent))
            chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryDark)))
            binding.chipGroupGenre.addView(chip)
        }
    }

    private fun View.showLoader(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

}