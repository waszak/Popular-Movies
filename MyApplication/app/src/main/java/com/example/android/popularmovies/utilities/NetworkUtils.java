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

import android.net.Uri;

import com.example.android.popularmovies.MoviesAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static private String THE_MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static private String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    final static private String YOUTUBE_THUMBNAIL="https://img.youtube.com/vi/%s/hqdefault.jpg";

    final static private String PARAM_API_KEY= "api_key";
    final static private String PARAM_PAGE= "page";

    final static private String TOP_RATED ="/top_rated";
    final static private String POPULARITY = "/popular";

    final static private String VIDEOS = "/%d/videos";

    final static private String REVIEWS = "/%d/reviews";

    final static private String IMAGES = "/%d/images";

    /**
     * Builds the URL used to query The Movie DB
     *
     * @param apiKey Api key to access The Movie DB.
     * @return The URL to use to query the The Movie DB.
     */
    public static URL buildUrlToRequestPage(int page, MoviesAdapter.SORT_MODE sortMode, String apiKey ) {
        String sortBy;
        switch (sortMode){
            case MOST_POPULAR:
                sortBy = POPULARITY;
                break;
            case TOP_RATED:
            default:
                sortBy = TOP_RATED;
                break;
        }
        Uri.Builder builder = Uri.parse(THE_MOVIE_DB_BASE_URL+sortBy).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .appendQueryParameter(PARAM_PAGE,Integer.toString(page));

        Uri builtUri = builder.build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to query The Movie DB
     *
     * @param additionalPath to request more details from movie.
     * @param apiKey Api key to access The Movie DB.
     * @return The URL to use to query the The Movie DB.
     */
    private static URL buildUrl(String additionalPath,String apiKey ) {

        Uri.Builder builder = Uri.parse(THE_MOVIE_DB_BASE_URL+additionalPath).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, apiKey);

        Uri builtUri = builder.build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Builds the URL used to query The Movie DB
     *
     * @param movieId id of movie to query.
     * @param apiKey Api key to access The Movie DB.
     * @return The URL to use to query the The Movie DB.
     */
    public static URL buildVideosURL(int movieId, String apiKey){
        return buildUrl(String.format(VIDEOS,movieId), apiKey);
    }

    /**
     * Builds the URL used to query The Movie DB
     *
     * @param movieId id of movie to query.
     * @param apiKey Api key to access The Movie DB.
     * @return The URL to use to query the The Movie DB.
     */
    public static URL buildReviewsURL(int movieId, String apiKey){
        return buildUrl(String.format(REVIEWS,movieId), apiKey);
    }

    /**
     * Builds the URL used to query The Movie DB
     *
     * @param movieId id of movie to query.
     * @param apiKey Api key to access The Movie DB.
     * @return The URL to use to query the The Movie DB.
     */
    public static URL buildImagesURL(int movieId, String apiKey){
        return buildUrl(String.format(IMAGES,movieId), apiKey);
    }

    /**
     * Builds the URL used to query The Movie DB
     *
     * @param posterFileName File name of poster
     * @return The URL to use to query the The Movie DB.
     */
    public static String buildPosterStringUrl(String posterFileName) {
        return POSTER_BASE_URL+posterFileName;
    }

    public static String buildThumbnailUrl(String youtubeId){
        return String.format(YOUTUBE_THUMBNAIL, youtubeId);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
