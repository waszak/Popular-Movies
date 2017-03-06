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

package com.example.android.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.adapters.ReviewAdapter;
import com.example.android.popularmovies.core.ScrollListener;
import com.example.android.popularmovies.data.MoviePreferences;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Results;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.utilities.BaseAsyncTask;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieReviewsFragment extends Fragment {
    @BindView(R.id.rv_reviews) RecyclerView mReviewList;
    @BindInt(R.integer.number_of_columns_with_review) int mNumberOfColumns;

    private ReviewAdapter mReviewAdapter;
    private Movie mMovie;

    private static final String MOVIE_ACTIVE = "MOVIE_ACTIVE";
    private static final String REVIEWS_ADAPTER_STATE = "REVIEWS_ADAPTER_STATE";

    public MovieReviewsFragment() {
        // Required empty public constructor
    }

    public static MovieReviewsFragment newInstance(Movie movie) {
        MovieReviewsFragment fragment =  new MovieReviewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Movie.TAG,movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        ButterKnife.bind(this,view);
        if(getArguments().containsKey(Movie.TAG)) {
            mMovie = getArguments().getParcelable(Movie.TAG);
        }else{
            throw new IllegalArgumentException("No movie passed to fragment");
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),mNumberOfColumns);
        ScrollListener scrollListener = new ScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadReviews(page + 1);
            }

        };

        mReviewList.setLayoutManager(gridLayoutManager);
        mReviewList.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter();
        mReviewList.setAdapter(mReviewAdapter);
        mReviewList.addOnScrollListener(scrollListener);

        onRestoreState(savedInstanceState);
        return view;
    }

    private void loadReviews(int page) {
        BaseAsyncTask<Void, Void, ArrayList<Review>> asyncTask = new BaseAsyncTask<>();
        asyncTask.setCallback(getCallback(page));
        asyncTask.setTask(getJob(page));
        asyncTask.execute();
    }

    private BaseAsyncTask.ICallbackTask<ArrayList<Review>> getCallback(final int page) {
        return new BaseAsyncTask.ICallbackTask<ArrayList<Review>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ArrayList<Review> reviews) {
                if(page == 1) {
                    mReviewAdapter.setReviewsData(reviews);
                }else{
                    mReviewAdapter.addReviewsData(reviews);
                }
            }

            @Override
            public void onError() {
            }
        };
    }

    private BaseAsyncTask.ITask<ArrayList<Review>> getJob(final int page) {
        final String apiKey = getContext().getResources().getString(R.string.api_key_the_movie_db);
        BaseAsyncTask.ITask<ArrayList<Review>> review = new BaseAsyncTask.ITask<ArrayList<Review>> (){
            @Override
            public ArrayList<Review> task() {
                Results<Review> results;
                try {
                    results =  NetworkUtils.buildRetrofit().loadReviews(mMovie.getId(),page, apiKey)
                                    .execute().body();
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
        };
        return review;
    }

    private void onRestoreState(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            loadReviews(1);
        }
        else{
            if(savedInstanceState.containsKey(REVIEWS_ADAPTER_STATE)){
                ArrayList<Review> reviewsArrayList = savedInstanceState.
                        getParcelableArrayList(REVIEWS_ADAPTER_STATE);
                mReviewAdapter.setReviewsData(reviewsArrayList);
            }if(savedInstanceState.containsKey(MOVIE_ACTIVE)){
                mMovie = savedInstanceState.getParcelable(MOVIE_ACTIVE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(REVIEWS_ADAPTER_STATE, mReviewAdapter.getList());
        //We need this to recover fragment details on tablet.
        if (mMovie != null) {
            outState.putParcelable(MOVIE_ACTIVE, mMovie);
        }
        super.onSaveInstanceState(outState);
    }
}
