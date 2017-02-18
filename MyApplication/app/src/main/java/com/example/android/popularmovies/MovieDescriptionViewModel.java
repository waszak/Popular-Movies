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

package com.example.android.popularmovies;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Waszak on 19.02.2017.
 */

public class MovieDescriptionViewModel {

    private Movie mMovie;
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
        BigDecimal bd = new BigDecimal(mMovie.getScore()/2);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext()).load(NetworkUtils.buildPosterStringUrl(imageUrl)).into(view);
    }

}
