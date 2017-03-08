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

package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.popularmovies.models.Movie;


/**
 * Defines table and column names for the weather database. This class is not necessary, but keeps
 * the code organized.
 */
public class MovieContract {

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for Movie.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MOVIE = "movie";

    /* Inner class that defines the table contents of the weather table */
    public static final class MovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Movie table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();


        public static final String TABLE_NAME = "movie";

        /* MOVIE ID as returned by API, used to identify movie id */
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";
        public static final String COLUMN_SCORE = "score";
        /* backdrop is relative path to backdrop image */
        public static final String COLUMN_BACKDROP = "backdrop";
        /* poster is relative path to poster image*/
        public static final String COLUMN_POSTER = "poster";


        public static Uri buildMovieUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }

        public static ContentValues mapToContentValues(Movie movie) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_MOVIE_ID, movie.getId());
            cv.put(COLUMN_TITLE, movie.getTitle());
            cv.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
            cv.put(COLUMN_PLOT_SYNOPSIS, movie.getPlotSynopsis());
            cv.put(COLUMN_SCORE, movie.getScore());
            cv.put(COLUMN_BACKDROP, movie.getBackDropFileName());
            cv.put(COLUMN_POSTER, movie.getPosterFileName());
            return cv;
        }

        public  static  Movie mapToMovie(Cursor cursor){
            Movie movie = new Movie();
            int colId = cursor.getColumnIndex(COLUMN_MOVIE_ID);
            int colTitle = cursor.getColumnIndex(COLUMN_TITLE);
            int colRelease = cursor.getColumnIndex(COLUMN_RELEASE_DATE);
            int colPlot = cursor.getColumnIndex(COLUMN_PLOT_SYNOPSIS);
            int colScore = cursor.getColumnIndex(COLUMN_SCORE);
            int colBackdrop = cursor.getColumnIndex(COLUMN_BACKDROP);
            int colPoster = cursor.getColumnIndex(COLUMN_POSTER);
            if(colId > -1){
                movie.setId(cursor.getInt(colId));
            }if(colTitle> -1){
                movie.setTitle(cursor.getString(colTitle));
            }if(colRelease > -1){
                movie.setReleaseDate(cursor.getString(colRelease));
            }if(colPlot > -1){
                movie.setPlotSynopsis(cursor.getString(colPlot));
            }if(colScore > -1){
                movie.setScore(cursor.getFloat(colScore));
            }if(colBackdrop > -1){
                movie.setBackDropFileName(cursor.getString(colBackdrop));
            }if(colPoster > -1){
                movie.setPosterFileName(cursor.getString(colPoster));
            }
            movie.setFavourite(true);
            return movie;
        }
    }
}