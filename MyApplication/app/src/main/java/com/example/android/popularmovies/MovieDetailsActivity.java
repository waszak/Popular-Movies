/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* Details activity shows detailed information about selected movie.
*/
public class MovieDetailsActivity extends AppCompatActivity {

	@BindView(R.id.img_details_movie_poster) ImageView mImageView;
	@BindView(R.id.tv_title) TextView mTitle;
	@BindView(R.id.tv_synopsis) TextView mSynopsis;
	@BindView(R.id.tv_score) TextView mScore;
	@BindView(R.id.tv_release_date) TextView mReleaseYear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_details);
		ButterKnife.bind(this);

		Intent intentThatStartedThisActivity = getIntent();
		if (intentThatStartedThisActivity.hasExtra(Movie.TAG)) {

			Movie movie = intentThatStartedThisActivity.getParcelableExtra(Movie.TAG);
			String fileName = movie.getPosterFileName();
			mTitle.setText(movie.getTitle());
			mScore.setText("Score: "+String.format( "%.2f",movie.getScore())+"/10");
			mSynopsis.setText(movie.getPlotSynopsis());
			mReleaseYear.setText(movie.getReleaseDate());
			Picasso.with(this).load(NetworkUtils.buildPosterStringUrl(fileName)).into(mImageView);
		}

	}
}
