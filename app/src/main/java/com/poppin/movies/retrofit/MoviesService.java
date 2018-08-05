package com.poppin.movies.retrofit;

import com.poppin.movies.models.MovieDetail;
import com.poppin.movies.models.RetornoBusca;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesService {

    @GET("/")
    Call<RetornoBusca> searchMovie(@Query("apikey") String apiKey,
                                   @Query("s") String name,
                                   @Query("type") String type);

    @GET("/")
    Call<MovieDetail> getMovie(@Query("apikey") String apiKey,
                               @Query("i") String imdbId);
}
