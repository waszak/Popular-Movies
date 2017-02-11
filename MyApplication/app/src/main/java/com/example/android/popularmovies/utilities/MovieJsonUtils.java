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


import android.util.Log;

import com.example.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/*
* Maps Json to list of movies.
*/
public final class MovieJsonUtils {
    private static final String TAG = MovieJsonUtils.class.getSimpleName();
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
    public static ArrayList<Movie> getMoviesFromJson(String moviesJsonStr)
            throws JSONException {

        final String TAG_RESULTS = "results";
        final String TAG_POSTER_PATH ="poster_path";
        final String TAG_TITLE = "title";
        final String TAG_RELEASE_DATE= "release_date";
        final String TAG_VOTE_AVG ="vote_average";
        final String TAG_OVERVIEW = "overview";

        final String TAG_DEBUG_SUCCESS = "success";
        final String TAG_DEBUG_STATUS_CODE = "status_code";
        final String TAG_DEBUG_STATUS_MESSAGE = "status_message";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

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
            return  null;
        }
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
                            ||! singleMovieJson.has(TAG_VOTE_AVG))
                            {
                continue;
            }
            String posterFileName = singleMovieJson.getString(TAG_POSTER_PATH);
            String title = singleMovieJson.getString(TAG_TITLE);
            String releaseDate = singleMovieJson.getString(TAG_RELEASE_DATE);
            String overview = singleMovieJson.getString(TAG_OVERVIEW);
            double avg = singleMovieJson.getDouble(TAG_VOTE_AVG);
            Movie movie = new Movie(title, posterFileName,releaseDate,avg,overview);
            movies.add(movie);
        }
        return  movies;
    }
}
