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

import android.content.Context;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.R;

import java.net.URL;
import java.util.ArrayList;

public class FetchMovieTask extends AbstractAsyncTask<Boolean,Void,ArrayList<Movie>> {

    private int mPage;
    private MoviesAdapter.SORT_MODE mSortMode;
    private Context mContext;

    public interface ICallbackMovieTask extends ICallbackTask<ArrayList<Movie>>{
    };

    private FetchMovieTask(Context context, int page){
        mPage = page;
        mContext = context;
    }

    public static FetchMovieTask with(Context context){
        return new FetchMovieTask(context,1);
    }

    public FetchMovieTask setPage(int page)
    {
        mPage = page;
        return  this;
    }

    public FetchMovieTask setOrder(MoviesAdapter.SORT_MODE sortMode){
        mSortMode = sortMode;
        return  this;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Boolean... params) {

        String apiKey = mContext.getResources().getString(R.string.api_key_the_movie_db);
        URL movieRequestUrl = NetworkUtils.buildUrlToRequestPage(mPage, mSortMode, apiKey);

        try {
            String jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);

            return MovieJsonUtils
                    .getMoviesFromJson(jsonMovieResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
