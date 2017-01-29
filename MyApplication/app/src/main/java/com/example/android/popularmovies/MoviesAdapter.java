/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

	final private MoviesAdapterOnClickHandler mClickHandler;

	private boolean mTopRated;
	private ArrayList<Movie> mMovies;

	public interface MoviesAdapterOnClickHandler {
		void onClick(Movie movie);
	}

	public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler){
		mClickHandler = clickHandler;
		mTopRated = true;
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
		int layoutIdForListItem = R.layout.movie_grid_item;
		LayoutInflater inflater = LayoutInflater.from(context);
		boolean shouldAttachToParentImmediately = false;

		View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
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
		String url = NetworkUtils.buildPosterStringUrl(movie.getPosterFileName());

		Picasso.with(holder.mContext).load(url).into(holder.mImageViewMovie, new Callback() {
			@Override
			public void onSuccess() {
				holder.mImageViewMovie.setVisibility(View.VISIBLE);
				holder.mProgressBar.setVisibility(View.GONE);
				holder.mErrorTextView.setVisibility(View.GONE);
			}

			@Override
			public void onError() {
				holder.mErrorTextView.setVisibility(View.VISIBLE);
			}
		});
    }

	public boolean topRated(){
		return  mTopRated;
	}

	public boolean popular(){
		return !mTopRated;
	}

	public void setTopRated(){
		if(!mTopRated){
			setMoviesData(null);
		}
		mTopRated = true;
	}

	public void setPopular(){
		if(mTopRated){
			setMoviesData(null);
		}
		mTopRated = false;
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

		Context mContext;
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
}
