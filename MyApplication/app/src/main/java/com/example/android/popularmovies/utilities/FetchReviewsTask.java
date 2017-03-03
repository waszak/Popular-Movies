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

import java.util.ArrayList;

/**
 * Created by Waszak on 22.02.2017.
 */

public class FetchReviewsTask extends AbstractAsyncTask<Boolean,Void,ArrayList<Movie>>{

    private int mPage;
    private final Context mContext;

    public interface ICallbackMovieTask extends ICallbackTask<ArrayList<Movie>>{
    }

    @Override
    protected ArrayList<Movie> doInBackground(Boolean... params) {
        return null;
    }

    private FetchReviewsTask(Context context, int page){
        mPage = page;
        mContext = context;
    }

    public static FetchReviewsTask with(Context context){
        return new FetchReviewsTask(context,1);
    }

    public FetchReviewsTask setPage(int page)
    {
        mPage = page;
        return  this;
    }
}
