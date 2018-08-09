package com.rodolfo.movies.view

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rodolfo.movies.R
import com.rodolfo.movies.base.BaseActivity
import com.rodolfo.movies.models.MovieDetail
import com.rodolfo.movies.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : BaseActivity(){

    //ImageViews
    lateinit var ivPoster: ImageView
    lateinit var ivImdb: ImageView
    lateinit var ivRotten: ImageView
    lateinit var ivMetacritic: ImageView

    //TextViews
    lateinit var tvMovieRelease: TextView
    lateinit var tvRated: TextView
    lateinit var tvDirector: TextView
    lateinit var tvWriter: TextView
    lateinit var tvWriterValue: TextView
    lateinit var tvRatings: TextView
    lateinit var tvValueImdb: TextView
    lateinit var tvValueRotten: TextView
    lateinit var tvValueMetacritic: TextView
    lateinit var tvDescription: TextView
    lateinit var tvDescriptionValue: TextView

    //ChipGroup
    lateinit var chipGroupGenre: ChipGroup

    //LinearLayout
    lateinit var llImdb: LinearLayout
    lateinit var llRotten: LinearLayout
    lateinit var llMetacritic: LinearLayout
    lateinit var llRatings: LinearLayout

    //Other
    lateinit var imdbId: String
    lateinit var details: MovieDetail
    lateinit var loaderFull: ConstraintLayout
    lateinit var cardImage: CardView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        refLayout = R.layout.activity_details
        super.onCreate(savedInstanceState)
        configViews()
        configToolbar()
        imdbId = intent.getSerializableExtra(Constants.IMDB_ID) as String
        showLoader(loaderFull, true)
        val chamada: Call<MovieDetail> = service.getMovie(Constants.API_KEY, imdbId)

        chamada.enqueue(object : Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                if (response.code() < 200 || response.code() >= 300) {
                    Log.e("Falha de conexão", response.code().toString())
                    showLoader(loaderFull, false)
                } else {
                    if (response.body() != null) {
                        showLoader(loaderFull, false)
                        details = response.body()!!
                        supportActionBar!!.setTitle(details.Title)
                        preencheTela()
                    }
                }
            }

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                Log.e("Falha de conexão", t.message)
            }
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

        ivPoster = findViewById(R.id.ivPoster)
        ivImdb = findViewById(R.id.ivImdb)
        ivRotten = findViewById(R.id.ivRotten)
        ivMetacritic = findViewById(R.id.ivMetacritic)

        tvMovieRelease = findViewById(R.id.tvMovieRelease)
        tvRated = findViewById(R.id.tvRated)
        tvDirector = findViewById(R.id.tvDirector)
        tvWriter = findViewById(R.id.tvWriter)
        tvWriterValue = findViewById(R.id.tvWriterValue)
        tvRatings = findViewById(R.id.tvRatings)
        tvValueImdb = findViewById(R.id.tvValueImdb)
        tvValueRotten = findViewById(R.id.tvValueRotten)
        tvValueMetacritic = findViewById(R.id.tvValueMetacritic)
        tvDescription = findViewById(R.id.tvDescription)
        tvDescriptionValue = findViewById(R.id.tvDescriptionValue)

        chipGroupGenre = findViewById(R.id.chipGroupGenre)

        llImdb = findViewById(R.id.llImdb)
        llRotten = findViewById(R.id.llRotten)
        llMetacritic = findViewById(R.id.llMetacritic)
        llRatings = findViewById(R.id.llRatings)

        loaderFull = findViewById(R.id.loaderFull)

        cardImage = findViewById(R.id.cardImage)

        val options = RequestOptions()
        options.fitCenter()
        Glide.with(ctx).load(R.mipmap.imdb).apply(options).into(ivImdb)
        Glide.with(ctx).load(R.mipmap.rotten).apply(options).into(ivRotten)
        Glide.with(ctx).load(R.mipmap.metacritic).apply(options).into(ivMetacritic)
    }

    private fun configToolbar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
    }

    @SuppressLint("SetTextI18n")
    private fun preencheTela(){
        val options = RequestOptions()
        cardImage.visibility = View.VISIBLE
        if (details.Poster == "N/A") {
            options.fitCenter()
            Glide.with(ctx).load(R.drawable.ic_movie).apply(options).into(ivPoster)
        } else {
            options.centerCrop()
            Glide.with(ctx).load(details.Poster).apply(options).into(ivPoster)
        }

        if (details.Ratings!!.isNotEmpty()) {
            llRatings.visibility = View.VISIBLE
            tvRatings.visibility = View.VISIBLE
        }

        for (r in details.Ratings!!) {
            when (r.Source) {
                "Internet Movie Database" -> {
                    llImdb.visibility = View.VISIBLE
                    tvValueImdb.text = r.Value
                }
                "Rotten Tomatoes" -> {
                    llRotten.visibility = View.VISIBLE
                    tvValueRotten.text = r.Value
                }
                "Metacritic" -> {
                    llMetacritic.visibility = View.VISIBLE
                    tvValueMetacritic.text = r.Value
                }
            }
        }

        val writers = StringBuilder()

        for (writer: String in details.Writer!!.split("), ")){
            writers.append("● $writer)\n")
        }

        tvMovieRelease.text = "Released: " + details.Released!!
        tvRated.text = "Rated: " + details.Rated!!
        tvDirector.text = "Director: " + details.Director!!
        tvWriter.text = "Writer"
        tvWriterValue.text = writers
        tvRatings.text = "Ratings:"
        tvDescription.text = "Description:"
        tvDescriptionValue.text = details.Plot

        val genres = details.Genre!!.split(", ")

        for (genre in genres) {
            val chip = Chip(chipGroupGenre.context)
            chip.text = genre
            chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(ctx, R.color.colorAccent))
            chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(ctx, R.color.colorPrimaryDark)))
            chipGroupGenre.addView(chip)
        }
    }

}