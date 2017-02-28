/*
 * Copyright (C) 2017. The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Results;
import com.example.android.popularmovies.models.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Waszak on 27.02.2017.
 */

public interface ITheMovieDbApi {
    @GET("/3/movie/{id}/videos")
    Call<Results<Trailer>> loadTrailers(@Path("id") int id, @Query("api_key") String status);

    @GET("/3/movie/popular")
    Call<Results<Movie>> loadPopular(@Query("page") int page, @Query("api_key") String status);

    @GET("/3/movie/top_rated")
    Call<Results<Movie>> loadTopRated( @Query("page") int page, @Query("api_key") String status);
}
