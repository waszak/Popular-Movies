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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieTrailersFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>>,
          TrailerAdapter.TrailerAdapterOnClickHandler{

    private final static int LOADER_ID = 0;

    private Movie mMovie;
    private TrailerAdapter mTrailerAdapter;
    @BindView(R.id.rv_trailers) RecyclerView mRecyclerView;

    public MovieTrailersFragment() {
        // Required empty public constructor
    }

    public static MovieTrailersFragment newInstance(Movie movie) {
        MovieTrailersFragment fragment =  new MovieTrailersFragment();
        Bundle args = new Bundle();
        args.putParcelable(Movie.TAG,movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        getLoaderManager().getLoader(LOADER_ID).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_trailers, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);

        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerView.setAdapter(mTrailerAdapter);

        if(getArguments().containsKey(Movie.TAG)) {
            mMovie = getArguments().getParcelable(Movie.TAG);
        }else{
            throw new IllegalArgumentException("No movie passed to fragment");
        }
        return view;
    }

    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Trailer>>(getContext()) {
            @Override
            public ArrayList<Trailer> loadInBackground() {
                String apiKey = getContext().getResources().getString(R.string.api_key_the_movie_db);
                URL movieRequestUrl = NetworkUtils.buildVideosURL(mMovie.getId(),apiKey);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    return MovieJsonUtils
                            .getTrailersFromJson(jsonMovieResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
       mTrailerAdapter.setTrailersData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

    }

    @Override
    public void onClick(Trailer trailer) {

    }
}
