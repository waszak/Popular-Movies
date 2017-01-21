/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
	implements  MoviesAdapter.MoviesAdapterOnClickHandler {


    private RecyclerView mMovieList;
    private MoviesAdapter mMoviesAdapter;
	private TextView mErrorMessageDisplay;
	private ProgressBar mLoadingIndicator;

	private ScrollListener mScrollListener;

    private static final int NUMBERS_OF_COLUMN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
		mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBERS_OF_COLUMN);
		mScrollListener= new ScrollListener(gridLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
				loadMoviesData(page+1);
			}

		};

        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);
        mMovieList.setLayoutManager(gridLayoutManager);
        mMovieList.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mMovieList.setAdapter(mMoviesAdapter);

		mMovieList.addOnScrollListener(mScrollListener);

		//we load first page;
		loadMoviesData(1);
    }

	private void loadMoviesData(int page) {
		showMoviesDataView();
		new FetchMovieTask(page).execute(mMoviesAdapter.topRated());
	}

	private void showMoviesDataView() {
        /* First, make sure the error is invisible */
		mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
		mMovieList.setVisibility(View.VISIBLE);
	}

	private void showErrorMessage() {
        /* First, hide the currently visible data */
		mMovieList.setVisibility(View.INVISIBLE);
        /* Then, show the error */
		mErrorMessageDisplay.setVisibility(View.VISIBLE);
	}


	public class FetchMovieTask extends AsyncTask<Boolean, Void, ArrayList<Movie>> {

		private int mPage =1;

		FetchMovieTask(int page){
			mPage = page;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mLoadingIndicator.setVisibility(View.VISIBLE);
		}

		@Override
		protected ArrayList<Movie> doInBackground(Boolean... params) {

			/* If there's no zip code, there's nothing to look up. */
			if (params.length == 0) {
				return null;
			}
			boolean topRated = params[0];
			String apiKey = getResources().getString(R.string.api_key_the_movie_db);
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
		}

		@Override
		protected void onPostExecute(ArrayList<Movie> movies) {
			mLoadingIndicator.setVisibility(View.INVISIBLE);
			if (movies != null) {
				showMoviesDataView();
				if(mPage == 1) {
					mMoviesAdapter.setMoviesData(movies);
				}else{
					mMoviesAdapter.addMoviesData(movies);
				}
			} else {
				showErrorMessage();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
		MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
		inflater.inflate(R.menu.movie_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id){
			case R.id.action_refresh:
				ArrayList<Movie> movies = new ArrayList<>();
				mMoviesAdapter.setMoviesData(movies);
				mScrollListener.resetState();
				loadMoviesData(1);
				return true;
			case R.id.action_sort_popular:
				mMoviesAdapter.setPopular();
				mScrollListener.resetState();
				loadMoviesData(1);
				return true;
			case R.id.action_top_rated:
				mMoviesAdapter.setTopRated();
				mScrollListener.resetState();
				loadMoviesData(1);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(Movie movie) {

		Context context = MainActivity.this;
		/* This is the class that we want to start (and open) when the button is clicked. */
		Class destinationActivity = MovieDetailsActivity.class;
		/*
		 * Here, we create the Intent that will start the Activity we specified above in
		 * the destinationActivity variable. The constructor for an Intent also requires a
		 * context, which we stored in the variable named "context".
		 */
		Intent startChildActivityIntent = new Intent(context, destinationActivity);

		startChildActivityIntent.putExtra(Movie.TAG, movie);

		/*
		 * Once the Intent has been created, we can use Activity's method, "startActivity"
		 * to start the ChildActivity.
		 */
		startActivity(startChildActivityIntent);
	}
}
