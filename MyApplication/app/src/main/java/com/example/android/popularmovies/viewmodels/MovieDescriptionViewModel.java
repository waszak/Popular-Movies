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

package com.example.android.popularmovies.viewmodels;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.NetworkUtils;

/**
 * Created by Waszak on 19.02.2017.
 */

public class MovieDescriptionViewModel {
    final static String TAG = MovieDescriptionViewModel.class.getSimpleName();
    private final Movie mMovie;
    public MovieDescriptionViewModel(Movie movie){
        mMovie = movie;
    }

    public String getPosterFileName(){
        return mMovie.getPosterFileName();
    }

    public String getTitle(){
        return mMovie.getTitle();
    }

    public String getReleaseDate(){
        return  mMovie.getReleaseDate();
    }

    public String getPlotSynopsis(){
        return mMovie.getPlotSynopsis();
    }

    public float getScore(){
        return mMovie.getScore();
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        NetworkUtils.buildPosterRequest(view.getContext(),imageUrl).into(view);
    }

    public boolean isFavorite(){
        return  mMovie.isFavourite();
    }

    public void setFavorite(boolean isFavorite){
        mMovie.setFavourite(isFavorite);
    }

    public int getId(){return mMovie.getId();}

    public Movie getMovie(){return mMovie;}
}
