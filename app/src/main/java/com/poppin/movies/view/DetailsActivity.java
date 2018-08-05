package com.poppin.movies.view;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.poppin.movies.R;
import com.poppin.movies.base.BaseActivity;
import com.poppin.movies.models.MovieDetail;
import com.poppin.movies.models.Ratings;
import com.poppin.movies.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends BaseActivity {

    //ImageViews
    private ImageView ivPoster;
    private ImageView ivImdb;
    private ImageView ivRotten;
    private ImageView ivMetacritic;

    //TextViews
    private TextView tvMovieRelease;
    private TextView tvRated;
    private TextView tvDirector;
    private TextView tvWriter;
    private TextView tvWriterValue;
    private TextView tvRatings;
    private TextView tvValueImdb;
    private TextView tvValueRotten;
    private TextView tvValueMetacritic;
    private TextView tvDescription;
    private TextView tvDescriptionValue;

    //ChipGroup
    private ChipGroup chipGroupGenre;

    //LinearLayout
    private LinearLayout llImdb;
    private LinearLayout llRotten;
    private LinearLayout llMetacritic;
    private LinearLayout llRatings;

    //Other
    private String imdbId;
    private MovieDetail details;
    private ConstraintLayout loaderFull;
    private CardView cardImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        refLayout = R.layout.activity_details;
        super.onCreate(savedInstanceState);
        configViews();
        configToolbar();
        imdbId = (String) getIntent().getSerializableExtra(Constants.IMDB_ID);
        showLoader(loaderFull, true);
        Call<MovieDetail> chamada = service.getMovie(Constants.API_KEY, imdbId);

        chamada.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.code() < 200 || response.code() >= 300) {
                    Log.e("Falha de conexão", String.valueOf(response.code()));
                    showLoader(loaderFull, false);
                } else {
                    if (response.body() != null) {
                        showLoader(loaderFull, false);
                        details = response.body();
                        getSupportActionBar().setTitle(details.Title);
                        preencheTela();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e("Falha de conexão", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void configViews() {

        ivPoster = findViewById(R.id.ivPoster);
        ivImdb = findViewById(R.id.ivImdb);
        ivRotten = findViewById(R.id.ivRotten);
        ivMetacritic = findViewById(R.id.ivMetacritic);

        tvMovieRelease = findViewById(R.id.tvMovieRelease);
        tvRated = findViewById(R.id.tvRated);
        tvDirector = findViewById(R.id.tvDirector);
        tvWriter = findViewById(R.id.tvWriter);
        tvWriterValue = findViewById(R.id.tvWriterValue);
        tvRatings = findViewById(R.id.tvRatings);
        tvValueImdb = findViewById(R.id.tvValueImdb);
        tvValueRotten = findViewById(R.id.tvValueRotten);
        tvValueMetacritic = findViewById(R.id.tvValueMetacritic);
        tvDescription = findViewById(R.id.tvDescription);
        tvDescriptionValue = findViewById(R.id.tvDescriptionValue);

        chipGroupGenre = findViewById(R.id.chipGroupGenre);

        llImdb = findViewById(R.id.llImdb);
        llRotten = findViewById(R.id.llRotten);
        llMetacritic = findViewById(R.id.llMetacritic);
        llRatings = findViewById(R.id.llRatings);

        loaderFull = findViewById(R.id.loaderFull);

        cardImage = findViewById(R.id.cardImage);

        RequestOptions options = new RequestOptions();
        options.fitCenter();
        Glide.with(ctx).load(R.mipmap.imdb).apply(options).into(ivImdb);
        Glide.with(ctx).load(R.mipmap.rotten).apply(options).into(ivRotten);
        Glide.with(ctx).load(R.mipmap.metacritic).apply(options).into(ivMetacritic);
    }

    private void configToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void preencheTela() {
        RequestOptions options = new RequestOptions();
        cardImage.setVisibility(View.VISIBLE);
        if (details.Poster.equals("N/A")) {
            options.fitCenter();
            Glide.with(ctx).load(R.drawable.ic_movie).apply(options).into(ivPoster);
        } else {
            options.centerCrop();
            Glide.with(ctx).load(details.Poster).apply(options).into(ivPoster);
        }

        if (details.Ratings.length > 0) {
            llRatings.setVisibility(View.VISIBLE);
            tvRatings.setVisibility(View.VISIBLE);
        }

        for (Ratings r : details.Ratings) {
            switch (r.Source) {
                case "Internet Movie Database":
                    llImdb.setVisibility(View.VISIBLE);
                    tvValueImdb.setText(r.Value);
                    break;
                case "Rotten Tomatoes":
                    llRotten.setVisibility(View.VISIBLE);
                    tvValueRotten.setText(r.Value);
                    break;
                case "Metacritic":
                    llMetacritic.setVisibility(View.VISIBLE);
                    tvValueMetacritic.setText(r.Value);
                    break;
            }
        }

        StringBuilder writers = new StringBuilder();

        for (String writer : details.Writer.split(", ")) {
            writers.append("● " + writer + "\n");
        }

        tvMovieRelease.setText("Released: " + details.Released);
        tvRated.setText("Rated: " + details.Rated);
        tvDirector.setText("Director: " + details.Director);
        tvWriter.setText("Writer");
        tvWriterValue.setText(writers);
        tvRatings.setText("Ratings:");
        tvDescription.setText("Description:");
        tvDescriptionValue.setText(details.Plot);

        String[] genres = details.Genre.split(", ");

        for (String genre : genres) {
            Chip chip = new Chip(chipGroupGenre.getContext());
            chip.setText(genre);
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(ctx, R.color.colorAccent)));
            chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(ctx, R.color.colorPrimaryDark)));
            chipGroupGenre.addView(chip);
        }

    }
}
