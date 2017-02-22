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


import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/*
* Maps Json to list of movies.
*/
public final class MovieJsonUtils {
    private static final String TAG = MovieJsonUtils.class.getSimpleName();

    private static final String TAG_RESULTS = "results";
    private static final String TAG_DEBUG_SUCCESS = "success";
    private static final String TAG_DEBUG_STATUS_CODE = "status_code";
    private static final String TAG_DEBUG_STATUS_MESSAGE = "status_message";

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     *
     *
     * @param moviesJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    @Nullable
    public static ArrayList<Movie> getMoviesFromJson(String moviesJsonStr)
            throws JSONException {

        final String TAG_POSTER_PATH ="poster_path";
        final String TAG_TITLE = "title";
        final String TAG_RELEASE_DATE= "release_date";
        final String TAG_VOTE_AVG ="vote_average";
        final String TAG_OVERVIEW = "overview";
        final String TAG_ID = "id";
        final String TAG_BACKDROP_PATH = "backdrop_path";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        if (validate(moviesJson))
            return null;

        JSONArray jsonArray = moviesJson.getJSONArray(TAG_RESULTS);
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject singleMovieJson = jsonArray.getJSONObject(i);
            //this shouldn't happen
            if(singleMovieJson == null){
                continue;
            }
            //If for some reason there is no poster or title then I skip;
            if(!singleMovieJson.has(TAG_POSTER_PATH)
                || !singleMovieJson.has(TAG_TITLE)
                    ||!singleMovieJson.has(TAG_RELEASE_DATE)
                        ||! singleMovieJson.has(TAG_OVERVIEW)
                            ||! singleMovieJson.has(TAG_VOTE_AVG)
                                ||! singleMovieJson.has(TAG_ID)
                                    ||! singleMovieJson.has(TAG_BACKDROP_PATH))
                            {
                continue;
            }
            int id = singleMovieJson.getInt(TAG_ID);
            String posterFileName = singleMovieJson.getString(TAG_POSTER_PATH);
            String title = singleMovieJson.getString(TAG_TITLE);
            String releaseDate = singleMovieJson.getString(TAG_RELEASE_DATE);
            String overview = singleMovieJson.getString(TAG_OVERVIEW);
            double avg = singleMovieJson.getDouble(TAG_VOTE_AVG);
            String backdrop = singleMovieJson.getString(TAG_BACKDROP_PATH);
            Movie movie = new Movie(id,title, posterFileName,releaseDate,avg,overview, backdrop);
            movies.add(movie);
        }
        return  movies;
    }

    @Nullable
    public static ArrayList<Trailer> getTrailersFromJson(String trailersJsonStr) throws JSONException{
        final String TAG_NAME = "name";
        final String TAG_KEY = "key";
        final String TAG_SITE = "site";
        final String TAG_TYPE = "type";
        final String TAG_SIZE = "size";//int
        JSONObject trailersJson = new JSONObject(trailersJsonStr);
        if (validate(trailersJson))
            return null;
        JSONArray jsonArray = trailersJson.getJSONArray(TAG_RESULTS);
        ArrayList<Trailer> trailers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject singleTrailerJson = jsonArray.getJSONObject(i);
            //this shouldn't happen
            if (singleTrailerJson == null) {
                continue;
            }

            //If for some reason there is no poster or title then I skip;
            if (!singleTrailerJson.has(TAG_SITE)
                    || !singleTrailerJson.has(TAG_TYPE)
                    || !singleTrailerJson.has(TAG_SIZE)
                    || !singleTrailerJson.has(TAG_KEY)
                    || !singleTrailerJson.has(TAG_NAME)) {
                continue;
            }

            String site = singleTrailerJson.getString(TAG_SITE);
            String type = singleTrailerJson.getString(TAG_TYPE);
            int size = singleTrailerJson.getInt(TAG_SIZE);
            String key = singleTrailerJson.getString(TAG_KEY);
            String name = singleTrailerJson.getString(TAG_NAME);
            Trailer trailer = new Trailer(name,key,size,type,site);
            trailers.add(trailer);
        }
        return trailers;
    }

    @Nullable
    public static ArrayList<Trailer> getReviewsFromJson(String trailersJsonStr) throws JSONException{
        final String TAG_NAME = "name";
        final String TAG_KEY = "key";
        final String TAG_SITE = "site";
        final String TAG_TYPE = "type";
        final String TAG_SIZE = "size";//int
        JSONObject trailersJson = new JSONObject(trailersJsonStr);
        if (validate(trailersJson))
            return null;
        JSONArray jsonArray = trailersJson.getJSONArray(TAG_RESULTS);
        ArrayList<Trailer> trailers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject singleTrailerJson = jsonArray.getJSONObject(i);
            //this shouldn't happen
            if (singleTrailerJson == null) {
                continue;
            }

            //If for some reason there is no poster or title then I skip;
            if (!singleTrailerJson.has(TAG_SITE)
                    || !singleTrailerJson.has(TAG_TYPE)
                    || !singleTrailerJson.has(TAG_SIZE)
                    || !singleTrailerJson.has(TAG_KEY)
                    || !singleTrailerJson.has(TAG_NAME)) {
                continue;
            }

            String site = singleTrailerJson.getString(TAG_SITE);
            String type = singleTrailerJson.getString(TAG_TYPE);
            int size = singleTrailerJson.getInt(TAG_SIZE);
            String key = singleTrailerJson.getString(TAG_KEY);
            String name = singleTrailerJson.getString(TAG_NAME);
            Trailer trailer = new Trailer(name,key,size,type,site);
            trailers.add(trailer);
        }
        return trailers;
    }

    private static boolean validate(JSONObject moviesJson) throws JSONException {
    /* Is there an error when we have no results? */
        if(!moviesJson.has(TAG_RESULTS)){
            if(moviesJson.has(TAG_DEBUG_SUCCESS)){
                Log.v(TAG, TAG_DEBUG_SUCCESS+": "+ String.valueOf(moviesJson.getBoolean(TAG_DEBUG_SUCCESS)));
            }
            if(moviesJson.has(TAG_DEBUG_STATUS_CODE)){
                Log.v(TAG, TAG_DEBUG_STATUS_CODE+": "+moviesJson.getInt(TAG_DEBUG_STATUS_CODE));
            }
            if(moviesJson.has(TAG_DEBUG_STATUS_MESSAGE)){
                Log.v(TAG, TAG_DEBUG_STATUS_MESSAGE+": "+moviesJson.getString(TAG_DEBUG_STATUS_MESSAGE));
            }
            return true;
        }
        return false;
    }


}
