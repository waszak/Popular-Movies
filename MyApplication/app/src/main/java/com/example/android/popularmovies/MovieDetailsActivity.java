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

/*
* Details activity shows detailed information about selected movie.
*/
public class MovieDetailsActivity extends AppCompatActivity {

	private ImageView mImageView;
	private TextView mTitle;
	private TextView mSynopsis;
	private TextView mScore;
	private TextView mReleaseYear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_details);
		mImageView = (ImageView)findViewById(R.id.img_details_movie_poster);
		mReleaseYear = (TextView) findViewById(R.id.tv_release_date);
		mSynopsis = (TextView) findViewById(R.id.tv_synopsis);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mScore = (TextView) findViewById(R.id.tv_score);

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
