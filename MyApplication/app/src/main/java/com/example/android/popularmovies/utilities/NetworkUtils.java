/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.popularmovies.utilities;

import android.net.Uri;

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

    final static private String PARAM_API_KEY= "api_key";

	final static private String PARAM_PAGE= "page";

    final static private String topRated ="/top_rated";
    final static private String popularity = "/popular";

    /**
     * Builds the URL used to query The Movie DB
     *
     * @param apiKey Api key to access The Movie DB.
     * @return The URL to use to query the The Movie DB.
     */
    public static URL buildUrl(int page, boolean sortByTopRated, String apiKey ) {
		String sortMode = topRated;
		if(!sortByTopRated){
			sortMode = popularity;
		}
        Uri.Builder builder = Uri.parse(THE_MOVIE_DB_BASE_URL+sortMode).buildUpon()
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
	 * @param posterFileName File name of poster
	 * @return The URL to use to query the The Movie DB.
	 */
	public static String buildPosterStringUrl(String posterFileName) {
		return POSTER_BASE_URL+posterFileName;
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
