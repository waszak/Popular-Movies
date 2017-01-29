/*
 * Copyright (C) 2013 The Android Open Source Project
 */
package com.example.android.popularmovies.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmovies.Movie;
import com.example.android.popularmovies.MoviesAdapter;

import java.util.ArrayList;

public class FetchMovieTask extends AsyncTaskLoader<ArrayList<Movie>> {

	private int mPage = 1;
	private MoviesAdapter mMoviesAdapter;

	public FetchMovieTask(Context context){
		super(context);
	}

	public FetchMovieTask(Context context, int page){
		this(context);
		mPage = page;
	}

	public static FetchMovieTask with(Context context){
		return new FetchMovieTask(context);
	}

	public FetchMovieTask setPage(int page)
	{
		mPage = page;
		return  this;
	}

	public FetchMovieTask setOrder(MoviesAdapter.SORT_MODE sortMode){
		return  this;
	}

	public void into(MoviesAdapter adapter){
		mMoviesAdapter = mMoviesAdapter;
	}
	/*
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mActivity.mLoadingIndicator.setVisibility(View.VISIBLE);
	}

	@Override
	protected ArrayList<Movie> doInBackground(Boolean... params) {

		/* If there's no params, there's nothing to look up. */
		/*if (params.length == 0) {
			return null;
		}
		boolean topRated = params[0];
		String apiKey = mActivity.getResources().getString(R.string.api_key_the_movie_db);
		URL movieRequestUrl = NetworkUtils.buildUrl(mPage, topRated, apiKey);

		try {
			String jsonMovieResponse = NetworkUtils
					.getResponseFromHttpUrl(movieRequestUrl);

			return MovieJsonUtils
					.getMoviesFromJson(jsonMovieResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/
	/*
	@Override
	protected void onPostExec/ute(ArrayList<Movie> movies) {
		mActivity.mLoadingIndicator.setVisibility(View.INVISIBLE);
		if (movies != null) {
			mActivity.showMoviesDataView();
			if (mPage == 1) {
				mActivity.mMoviesAdapter.setMoviesData(movies);
			} else {
				mActivity.mMoviesAdapter.addMoviesData(movies);
			}
		} else {
			mActivity.showErrorMessage();
		}
	}
	*/
	@Override
	public ArrayList<Movie> loadInBackground() {
		return null;
	}
}
