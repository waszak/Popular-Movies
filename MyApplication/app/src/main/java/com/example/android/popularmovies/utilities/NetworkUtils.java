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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import okhttp3.Cache;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static private String THE_MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static private String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    final static private String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/hqdefault.jpg";
    final static private String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=%s";
    public static final String YOUTUBE = "youtube";


    private static Retrofit retrofit;
    public static ITheMovieDbApi buildRetrofit() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(THE_MOVIE_DB_BASE_URL + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit.create(ITheMovieDbApi.class);
    }

    private static final int CACHE_SIZE = 60 * 1024 * 1024;
    private static boolean sIsPicassoSingletonSet;
    public static Picasso getPicasso(Context context){
        if(!sIsPicassoSingletonSet) {
            Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);
            okhttp3.OkHttpClient okHttp3Client = new okhttp3.OkHttpClient.Builder().cache(cache).build();
            OkHttp3Downloader downloader = new OkHttp3Downloader(okHttp3Client);
            Picasso picasso = new Picasso.Builder(context).downloader(downloader).build();
            Picasso.setSingletonInstance(picasso);
            sIsPicassoSingletonSet = true;
        }
        return Picasso.with(context);
    }

    /**
     * Builds the URL used to query The Movie DB
     *
     * @param posterFileName File name of poster
     * @return The URL to use to query the The Movie DB.
     */
    public static RequestCreator buildPosterRequest(Context context, String posterFileName) {
        return getPicasso(context).load(POSTER_BASE_URL+posterFileName);
    }

    public static RequestCreator buildThumbnailRequest(Context context,String youtubeId){
        return getPicasso(context).load(String.format(YOUTUBE_THUMBNAIL, youtubeId));
    }


    public static String getYoutubeUrl(String youtubeId ){
        return  String.format(YOUTUBE_BASE_URL, youtubeId);
    }

}
