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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<Review> mReviews = new ArrayList<>();



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
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.review_grid_item, viewGroup, false);
        return new ReviewViewHolder(view, context);
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
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.mContentTextView.setText(review.getContent());
        holder.mAuthorTextView.setText(review.getAuthor());
    }


    @Override
    public int getItemCount() {
        return (mReviews ==  null) ? 0 : mReviews.size();
    }

    /**
     * This method is used to set the movie list on a MovieAdapter if we've already
     * created one.
     *
     * @param reviews The new reviews data to be displayed.
     */
    public void setReviewsData(ArrayList<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }
    /**
     * This method is used to add new reviews to existing list
     * @param reviews The new weather data to be displayed.
     */
    public void addReviewsData(ArrayList<Review> reviews) {
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a list item.
     */
    class ReviewViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the grid, ie 0 through getItemCount() - 1
        @BindView(R.id.tv_author) TextView mAuthorTextView;
        @BindView(R.id.tv_content) TextView mContentTextView;

        final Context mContext;
        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextView,ImageViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link ReviewAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        ReviewViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = context;
        }


    }

    public ArrayList<Review> getList() {
        if(mReviews == null){
            return null;
        }
        return new ArrayList<>(mReviews);
    }
}
