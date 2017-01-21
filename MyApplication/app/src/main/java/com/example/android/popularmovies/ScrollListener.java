/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.popularmovies;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * ScrollListener allow us to endless scrolling.
 * It fetches more data when we scroll.
 * Based on guide https://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 */

public abstract class ScrollListener extends RecyclerView.OnScrollListener{
	// The minimum number of items to have below your current scroll position
	// before loading more.

	private int mVisibleThreshold = 40;

	private int mCurrentPage = 0;

	private int previousTotalItemCount = 0;

	private boolean loading = true;


	private int mStartingPageIndex = 0;

	RecyclerView.LayoutManager mLayoutManager;

	public ScrollListener(GridLayoutManager layoutManager) {
		this.mLayoutManager = layoutManager;
		mVisibleThreshold = mVisibleThreshold * layoutManager.getSpanCount();
	}


	// This happens many times a second during a scroll, so be wary of the code you place here.
	// We are given a few useful parameters to help us work out if we need to load some more data,
	// but first we check if we are waiting for the previous load to finish.
	@Override
	public void onScrolled(RecyclerView view, int dx, int dy) {
		int lastVisibleItemPosition = 0;
		int totalItemCount = mLayoutManager.getItemCount();


		 if (mLayoutManager instanceof GridLayoutManager) {
			lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
		 }

		// If the total item count is zero and the previous isn't, assume the
		// list is invalidated and should be reset back to initial state
		if (totalItemCount < previousTotalItemCount) {
			this.mCurrentPage = this.mStartingPageIndex;
			this.previousTotalItemCount = totalItemCount;
			if (totalItemCount == 0) {
				this.loading = true;
			}
		}
		// If it’s still loading, we check to see if the dataset count has
		// changed, if so we conclude it has finished loading and update the current page
		// number and total item count.
		if (loading && (totalItemCount > previousTotalItemCount)) {
			loading = false;
			previousTotalItemCount = totalItemCount;
		}

		// If it isn’t currently loading, we check to see if we have breached
		// the visibleThreshold and need to reload more data.
		// If we do need to reload some more data, we execute onLoadMore to fetch the data.
		// threshold should reflect how many total columns there are too
		if (!loading && (lastVisibleItemPosition + mVisibleThreshold) > totalItemCount) {
			mCurrentPage++;
			onLoadMore(mCurrentPage, totalItemCount, view);
			loading = true;
		}
	}

	public void resetState() {
		this.mCurrentPage = this.mStartingPageIndex;
		this.previousTotalItemCount = 0;
		this.loading = true;
	}

	// Defines the process for actually loading more data based on page
	public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

}