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
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.popularmovies.activities.MovieDetailsActivity;
import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.core.ScrollListener;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MoviePreferences;
import com.example.android.popularmovies.fragments.MovieDetailsFragment;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.FetchMovieTask;
import com.example.android.popularmovies.utilities.FetchMovieTaskDB;
import com.example.android.popularmovies.utilities.IMovieListListener;

import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
    implements  MoviesAdapter.MoviesAdapterOnClickHandler,
        IMovieListListener {

    @BindView(R.id.rv_movies) RecyclerView mMovieList;
    @Nullable @BindView(R.id.placeholder) FrameLayout mPlaceHolder;
    @BindInt(R.integer.number_of_column) int mNumberOfColumns;
    @BindBool(R.bool.isPaneLayout) boolean mIsPaneLayout;

    private MoviesAdapter mMoviesAdapter;
    private ScrollListener mScrollListener;
    private GridLayoutManager mGridLayoutManager;
    private Movie mMovie;

    private static final String MOVIE_ACTIVE = "MOVIE_ACTIVE";
    private static final String MOVIES_ADAPTER_STATE = "MOVIES_ADAPTER_STATE";

    public static final int MOVIE_DETAILS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGridLayoutManager = new GridLayoutManager(this, mNumberOfColumns);
        mScrollListener = new ScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(!MoviePreferences.getFavorites(MainActivity.this)){
                    loadMoviesData(page+1);
                }
            }

        };

        mMovieList.setLayoutManager(mGridLayoutManager);
        mMovieList.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mMovieList.setAdapter(mMoviesAdapter);
        mMovieList.addOnScrollListener(mScrollListener);
        mMoviesAdapter.setSortMode(MoviePreferences.getSortMode(this));

        onRestoreState(savedInstanceState);

    }

    private void showMoviesDataView() {
        mMovieList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMovieList.setVisibility(View.INVISIBLE);
    }

    private void loadMoviesData(final int page){
        FetchMovieTask.ICallbackMovieTask callbackMovieTask = new FetchMovieTask.ICallbackMovieTask() {
            @Override
            public void onStart() {}
            @Override
            public void onSuccess(ArrayList<Movie>movies) {
                showMoviesDataView();
                if(page == 1) {
                    mMoviesAdapter.setMoviesData(movies);
                }else{
                    mMoviesAdapter.addMoviesData(movies);
                }
            }

            @Override
            public void onError() {
                showErrorMessage();
            }
        };
        if(!MoviePreferences.getFavorites(this)) {
            FetchMovieTask.with(this)
                    .setOrder(MoviePreferences.getSortMode(this))
                    .setPage(page)
                    .setCallback(callbackMovieTask)
                    .execute();
        }else{
            new FetchMovieTaskDB(this).setCallback(callbackMovieTask).execute();
        }
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
                setSortModePreferences(MoviesAdapter.SORT_MODE.MOST_POPULAR);
                loadMoviesData(1);

                return true;
            case R.id.action_top_rated:
                invalidateData();
                setSortModePreferences(MoviesAdapter.SORT_MODE.TOP_RATED);
                loadMoviesData(1);
                return true;
            case R.id.action_favorites:
                invalidateData();
                setFavoritePreferences();
                loadMoviesData(1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSortModePreferences(MoviesAdapter.SORT_MODE sortMode) {
        MoviePreferences.setFavorites(this,false);
        MoviePreferences.setSortMode(this,sortMode);
    }

    private void setFavoritePreferences(){
        MoviePreferences.setFavorites(this,true);
    }

    @Override
    public void onClick(Movie movie) {
        mMovie = movie;
        //fetch is favorite only when vies isn't favorites.
        if(!MoviePreferences.getFavorites(this)){
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.buildMovieUriWithId(movie.getId()),
                    new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},null,null,null);
            movie.setFavourite(cursor != null && cursor.moveToFirst());
            if(cursor != null){
                cursor.close();
            }
        }
        if(!mIsPaneLayout) {
            Context context = MainActivity.this;
            Class destinationActivity = MovieDetailsActivity.class;
            Intent startChildActivityIntent = new Intent(context, destinationActivity);
            startChildActivityIntent.putExtra(Movie.TAG, movie);
            startActivityForResult(startChildActivityIntent, MOVIE_DETAILS_REQUEST);
        }else{
            MovieDetailsFragment fragment = MovieDetailsFragment.newInstance(movie);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.placeholder, fragment);
            ft.commit();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == MOVIE_DETAILS_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (intent.hasExtra(Movie.TAG)) {
                    Movie movieUpdated = intent.getParcelableExtra(Movie.TAG);
                    UpdateMovie(movieUpdated,false);

                }
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putParcelableArrayList(MOVIES_ADAPTER_STATE, mMoviesAdapter.getList());
        //We need this to recover fragment details on tablet.
        if (mMovie != null) {
            state.putParcelable(MOVIE_ACTIVE, mMovie);
        }
        super.onSaveInstanceState(state);
    }


    protected void onRestoreState(Bundle savedState) {
        if(savedState != null ){
            mMoviesAdapter.setSortMode(MoviePreferences.getSortMode(this));
            if(savedState.containsKey(MOVIES_ADAPTER_STATE)){
                ArrayList<Movie> movieArrayList = savedState.getParcelableArrayList(MOVIES_ADAPTER_STATE);
                mMoviesAdapter.setMoviesData(movieArrayList);
            }if(savedState.containsKey(MOVIE_ACTIVE)){
                mMovie = savedState.getParcelable(MOVIE_ACTIVE);
            }
            if(mMovie!= null){
                if(!mIsPaneLayout) {
                    Context context = MainActivity.this;
                    Class destinationActivity = MovieDetailsActivity.class;
                    Intent startChildActivityIntent = new Intent(context, destinationActivity);
                    startChildActivityIntent.putExtras(savedState);
                    startActivityForResult(startChildActivityIntent, MOVIE_DETAILS_REQUEST);
                }else{
                    MovieDetailsFragment fragment = new MovieDetailsFragment();
                    fragment.setArguments(savedState);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.placeholder, fragment);
                    ft.commit();
                }
            }
        }
        else{
            loadMoviesData(1);
        }
    }


    @Override
    public void UpdateMovie(Movie movieUpdated, boolean add) {
        if(!add){
            for (Movie movie : mMoviesAdapter.getList()) {
                if (movie.getId() == movieUpdated.getId()) {
                    movie.setFavourite(movieUpdated.isFavourite());
                    if(MoviePreferences.getFavorites(this) && !movie.isFavourite()){
                        mMoviesAdapter.removeMovie(movie);
                    }
                }
            }
        }else{
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(movieUpdated);
            mMoviesAdapter.addMoviesData(movies);
        }
    }
}
