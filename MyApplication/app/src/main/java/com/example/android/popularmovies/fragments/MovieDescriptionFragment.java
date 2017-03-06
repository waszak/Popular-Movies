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

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.IMovieListListener;
import com.example.android.popularmovies.viewmodels.MovieDescriptionViewModel;
import com.example.android.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDescriptionFragment extends Fragment {

    @BindView(R.id.fab_favorite) FloatingActionButton mFloatingActionButton;
    private MovieBinding mMovieBinding;
    private MovieDescriptionViewModel mViewModel;
    private static final String TAG = MovieDescriptionFragment.class.getSimpleName();
    private IMovieListListener listener;

    public MovieDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mMovieBinding = DataBindingUtil.inflate(
              inflater, R.layout.fragment_movie_description, container, false);
        View view =  mMovieBinding.getRoot();
        ButterKnife.bind(this, view);

        if(getArguments().containsKey(Movie.TAG)) {
            Movie m = getArguments().getParcelable(Movie.TAG);
            mViewModel = new MovieDescriptionViewModel(m);
            mMovieBinding.setViewModel(mViewModel);
        }else{
            throw new IllegalArgumentException("No movie passed to fragment");
        }
        updateFavoriteImage();
        return view;
    }

    @OnClick(R.id.fab_favorite)
    public void onClick(View view) {
        mViewModel.setFavorite(!mViewModel.isFavorite());
        updateFavoriteImage();
        Uri uri = MovieContract.MovieEntry.buildMovieUriWithId(mViewModel.getId());
        if(mViewModel.isFavorite()){
            getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.mapToContentValues(mViewModel.getMovie()));
        }else {
            getContext().getContentResolver().delete(uri,null,null);
        }
        listener.UpdateMovie(mViewModel.getMovie(),mViewModel.isFavorite());
    }

    private void updateFavoriteImage(){
        if(mViewModel.isFavorite()){
            mFloatingActionButton.setImageResource(R.drawable.heart);
        }else {
            mFloatingActionButton.setImageResource(R.drawable.heart_outline);
        }
    }

    public static MovieDescriptionFragment newInstance(Movie movie) {
        MovieDescriptionFragment fragment =  new MovieDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(Movie.TAG,movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IMovieListListener) context;
        } catch (ClassCastException castException) {
            Log.d(TAG, "Activity doesn't implement the interface");
            throw castException;
        }

    }
}
