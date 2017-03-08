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
import android.database.Cursor;
import android.net.Uri;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Movie;

import java.util.ArrayList;

/*
 * It creates task to load movies by page and sort mode
 * It uses DB instead
 */

public class FetchMovieTaskDB  extends BaseAsyncTask<Boolean,Void,ArrayList<Movie>> {
    private final Context mContext;
    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_SCORE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_BACKDROP,
            MovieContract.MovieEntry.COLUMN_POSTER
    };
    @Override
    protected ArrayList<Movie> doInBackground(Boolean... params) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(uri, MOVIE_DETAIL_PROJECTION, null, null, null);
        ArrayList<Movie> movies = new ArrayList<>();
        while (cursor!= null && cursor.moveToNext()){
            Movie movie = MovieContract.MovieEntry.mapToMovie(cursor);
            movies.add(movie);
        }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return movies;
    }
    public FetchMovieTaskDB(Context context){
        mContext = context;
    }

}
