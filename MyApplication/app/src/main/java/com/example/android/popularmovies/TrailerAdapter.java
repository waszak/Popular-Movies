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

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    final private TrailerAdapterOnClickHandler mClickHandler;

    private ArrayList<Trailer> mTrailers;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
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
     * @return A new TrailerViewHolder that holds the View for each grid item
     */
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.trailer_list_item, viewGroup, false);
        return new TrailerViewHolder(view, context);
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
    public void onBindViewHolder(final TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        String url = NetworkUtils.buildThumbnailUrl(trailer.getKey());
        holder.mTextViewTitle.setText(trailer.getName());
        holder.mTextViewType.setText(trailer.getType());
        holder.mTextViewDescription.setText(trailer.getDescription());
        Picasso.with(holder.mContext).load(url).into(holder.mImageViewMovie, new Callback() {
            @Override
            public void onSuccess() {
                holder.mImageViewMovie.setVisibility(View.VISIBLE);
                //holder.mProgressBar.setVisibility(View.GONE);
                //holder.mErrorTextView.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                //holder.mErrorTextView.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (mTrailers ==  null) ? 0 : mTrailers.size();
    }

    /**
     * This method is used to set the movie list on a MovieAdapter if we've already
     * created one.
     *
     * @param trailers The new trailers data displayed
     */
    public void setTrailersData(ArrayList<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }
    /**
     * This method is used to add new movies to existing list
     * @param trailers The new trailers data displayed
     */
    public void addTrailersData(ArrayList<Trailer> trailers) {
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a list item.
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        // Will display the position in the grid, ie 0 through getItemCount() - 1
        @BindView(R.id.img_trailer) ImageView mImageViewMovie;
        //@BindView(R.id.tv_error_poster_message_display) TextView mErrorTextView;
        //@BindView(R.id.pb_poster_loading_indicator)ProgressBar mProgressBar;
        @BindView(R.id.tv_trailer_title) TextView mTextViewTitle;
        @BindView(R.id.tv_trailer_type) TextView mTextViewType;
        @BindView(R.id.tv_trailer_desc) TextView mTextViewDescription;

        Context mContext;
        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextView,ImageViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link MoviesAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        TrailerViewHolder(View itemView, Context context) {
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
            Trailer trailer = mTrailers.get(adapterPosition);
            mClickHandler.onClick(trailer);
        }
    }

    public ArrayList<Trailer> getList() {
        if(mTrailers == null){
            return null;
        }
        return new ArrayList<Trailer>(mTrailers);
    }
}
