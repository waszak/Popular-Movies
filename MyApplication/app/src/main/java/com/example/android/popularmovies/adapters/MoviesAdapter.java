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

package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    public void removeMovie(Movie movieToDelete) {
        int position = 0;
        for (Movie movie: mMovies) {
            if( movie.equals(movieToDelete)){
                break;
            }
            position++;
        }
        if(position < mMovies.size()) {
            mMovies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public enum SORT_MODE{
        TOP_RATED(0),
        MOST_POPULAR(1);

        private final int value;
        SORT_MODE(int value) {
            this.value = value;
        }

        public static SORT_MODE getInstance(int id){
            switch (id){
                case 0:
                    return TOP_RATED;
                case 1:
                    return MOST_POPULAR;
                default:
                    throw new IllegalArgumentException("Enum Id not found: "+id);
            }
        }

        public int getValue() {
            return value;
        }
    }

    final private MoviesAdapterOnClickHandler mClickHandler;

    private SORT_MODE  mSortMode;
    private ArrayList<Movie> mMovies;

    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
        mSortMode = SORT_MODE.TOP_RATED;
    }

    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new MovieViewHolder that holds the View for each grid item
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_grid_item, viewGroup, false);
        return new MovieViewHolder(view, context);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the grid for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        RequestCreator request = NetworkUtils.buildPosterRequest(holder.mContext,movie.getPosterFileName());
        request.into(holder.mImageViewMovie, new Callback() {
            @Override
            public void onSuccess() {
                holder.mImageViewMovie.setVisibility(View.VISIBLE);
                holder.mProgressBar.setVisibility(View.GONE);
                holder.mErrorTextView.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.mErrorTextView.setVisibility(View.VISIBLE);
                holder.mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void setSortMode(SORT_MODE sortMode){
        if(sortMode != mSortMode){
            setMoviesData(null);
        }
        mSortMode = sortMode;
    }

    @Override
    public int getItemCount() {
        return (mMovies ==  null) ? 0 : mMovies.size();
    }

    /**
     * This method is used to set the movie list on a MovieAdapter if we've already
     * created one.
     *
     * @param movies The new weather data to be displayed.
     */
    public void setMoviesData(ArrayList<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
    /**
     * This method is used to add new movies to existing list
     * @param movies The new weather data to be displayed.
     */
    public void addMoviesData(ArrayList<Movie> movies) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a list item.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        // Will display the position in the grid, ie 0 through getItemCount() - 1
        @BindView(R.id.img_movie_poster) ImageView mImageViewMovie;
        @BindView(R.id.tv_error_poster_message_display) TextView mErrorTextView;
        @BindView(R.id.pb_poster_loading_indicator)ProgressBar mProgressBar;

        final Context mContext;
        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextView,ImageViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link MoviesAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        MovieViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = context;
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    public ArrayList<Movie> getList() {
        if(mMovies == null){
            return null;
        }
        return new ArrayList<>(mMovies);
    }
}
