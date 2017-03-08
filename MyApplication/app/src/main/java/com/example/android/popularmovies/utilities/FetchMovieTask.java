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
import com.example.android.popularmovies.models.Results;

import java.io.IOException;
import java.util.ArrayList;
/*
 * It creates task to load movies by page and sort mode
 */
public class FetchMovieTask extends BaseAsyncTask<Boolean,Void,ArrayList<Movie>> {

    private int mPage;
    private MoviesAdapter.SORT_MODE mSortMode;
    private final Context mContext;

    public interface ICallbackMovieTask extends ICallbackTask<ArrayList<Movie>>{
    }

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
        Results<Movie> results;
        try {
            switch (mSortMode){
                case MOST_POPULAR:
                    results = NetworkUtils.buildRetrofit().loadPopular(mPage, apiKey)
                            .execute().body();
                    break;
                case TOP_RATED:
                    results = NetworkUtils.buildRetrofit().loadTopRated(mPage, apiKey)
                            .execute().body();
                    break;
                default:
                    throw new IllegalArgumentException(
                            "There is no implementation for this sort mode: "
                            + mSortMode.name());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return null;
        }
        return new ArrayList<>(results.getResults());
    }

}
