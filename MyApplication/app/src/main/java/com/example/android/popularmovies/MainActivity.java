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
import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.example.android.popularmovies.utilities.FetchMovieTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
    implements  MoviesAdapter.MoviesAdapterOnClickHandler{

    @BindView(R.id.rv_movies) RecyclerView mMovieList;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private MoviesAdapter mMoviesAdapter;
    private ScrollListener mScrollListener;
    private GridLayoutManager mGridLayoutManager;
    private Parcelable mGridState;

    private static final int NUMBERS_OF_COLUMN = 2;
    private static final String LIST_STATE_KEY = "LIST_STATE_KEY";
    private static final String MOVIES_ADAPTER_STATE = "MOVIES_ADAPTER_STATE";
    private static final String SORT_TOP_RATED = "SORT_TOP_RATED";
    private static final String CURRENT_PAGE = "CURRENT_PAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGridLayoutManager = new GridLayoutManager(this, NUMBERS_OF_COLUMN);
        mScrollListener= new ScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoviesData(page+1);
            }

        };

        mMovieList.setLayoutManager(mGridLayoutManager);
        mMovieList.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mMovieList.setAdapter(mMoviesAdapter);
        mMovieList.addOnScrollListener(mScrollListener);
        if(mGridState != null){
            onRestoreInstanceState(savedInstanceState);
        }else {
            loadMoviesData(1);
        }
    }

    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then,  hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mMovieList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mMovieList.setVisibility(View.INVISIBLE);
        /* Then,  hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void loadMoviesData(final int page){
        FetchMovieTask.ICallbackMovieTask callbackMovieTask = new FetchMovieTask.ICallbackMovieTask() {
            @Override
            public void onStart() {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(ArrayList<Movie>movies) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                showMoviesDataView();
                if(page == 1) {
                    mMoviesAdapter.setMoviesData(movies);
                }else{
                    mMoviesAdapter.addMoviesData(movies);
                }
            }

            @Override
            public void onError() {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                showErrorMessage();
            }
        };
        MoviesAdapter.SORT_MODE sortMode;
        if(mMoviesAdapter.popular()){
            sortMode = MoviesAdapter.SORT_MODE.MOST_POPULAR;
        }else{
            sortMode = MoviesAdapter.SORT_MODE.TOP_RATED;
        }
        FetchMovieTask.with(this)
                .setOrder(sortMode)
                .setPage(page)
                .setCallback(callbackMovieTask)
                .execute();
    }
    /**
     * This method is used when we are resetting data, so that at one point in time during a
     * refresh of our data, you can see that there is no data showing.
     */
    private void invalidateData() {
        mMoviesAdapter.setMoviesData(new ArrayList<Movie>());
        mScrollListener.resetState();
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
                invalidateData();
                loadMoviesData(1);
                return true;
            case R.id.action_sort_popular:
                invalidateData();
                mMoviesAdapter.setPopular();
                loadMoviesData(1);
                return true;
            case R.id.action_top_rated:
                invalidateData();
                mMoviesAdapter.setTopRated();
                loadMoviesData(1);
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return  true;
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

    @Override
    protected void onSaveInstanceState(Bundle state) {

        mGridState = mGridLayoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mGridState);
        state.putParcelableArrayList(MOVIES_ADAPTER_STATE,mMoviesAdapter.getList());
        state.putBoolean(SORT_TOP_RATED, mMoviesAdapter.topRated());
        state.putInt(CURRENT_PAGE, mScrollListener.getCurrentPage());
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if(savedState != null ){
            if (savedState.containsKey(SORT_TOP_RATED) && savedState.getBoolean(SORT_TOP_RATED) ){
                mMoviesAdapter.setTopRated();
            }else{
                mMoviesAdapter.setPopular();
            }

            if(savedState.containsKey(MOVIES_ADAPTER_STATE)){
                ArrayList<Movie> movieArrayList = savedState.getParcelableArrayList(MOVIES_ADAPTER_STATE);
                mMoviesAdapter.setMoviesData(movieArrayList);
            }

            if (savedState.containsKey(LIST_STATE_KEY)){
                mGridState = savedState.getParcelable(LIST_STATE_KEY);
            }

            if (savedState.containsKey(CURRENT_PAGE)){
                mScrollListener.setCurrentPage(savedState.getInt(CURRENT_PAGE));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGridState != null){
            mGridLayoutManager.onRestoreInstanceState(mGridState);
        }
    }

    @Override
    protected void onPause() {
        mGridState = mGridLayoutManager.onSaveInstanceState();
        super.onPause();
    }


    /*@Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        mLoadingIndicator.setVisibility(View.VISIBLE);

        return FetchMovieTask.with(this)
                .setOrder(MoviesAdapter.SORT_MODE.TOP_RATED) ;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        loader.
        mMoviesAdapter.setMoviesData(data);
        if (data == null) {
            showErrorMessage();
        } else if (1 == 1) {
            mMoviesAdapter.setMoviesData(data);
            showMoviesDataView();
        } else {
            mMoviesAdapter.addMoviesData(data);
            showMoviesDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        ((FetchMovieTask )loader).setPage(1);
    }*/
}
