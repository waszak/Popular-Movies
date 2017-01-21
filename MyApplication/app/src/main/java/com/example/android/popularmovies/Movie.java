/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/*
* Class represents movie object. That was create from Json.
*
*/
public class Movie implements Parcelable {

	public static final String TAG =  Movie.class.getSimpleName();
	private final String mTitle;
	private final String mPosterFileName;
	private final String mReleaseDate;
	private final double mVoteAverage;
	private final String mPlotSynopsis;

	public Movie(String title, String posterFileName, String ReleaseDate, double voteAverage, String plotSynopsis){
		mTitle = title;
		mPosterFileName = posterFileName;
		mReleaseDate = ReleaseDate;
		mVoteAverage = voteAverage;
		mPlotSynopsis = plotSynopsis;
	}

	public Movie(Parcel in){
		String[] data= new String[5];
		in.readStringArray(data);

		mTitle= data[0];
		mPosterFileName= data[1];
		mReleaseDate = data[2];
		mVoteAverage = Float.parseFloat(data[3]);
		mPlotSynopsis = data[4];
	}
	public String getPosterFileName(){
		return mPosterFileName;
	}

	public String getTitle(){
		return mTitle;
	}

	public String getReleaseDate(){
		return  mReleaseDate;
	}

	public String getPlotSynopsis(){
		return mPlotSynopsis;
	}

	public double getScore(){
		return mVoteAverage;
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[]{this.mTitle,this.mPosterFileName, this.mReleaseDate, Double.toString(mVoteAverage), mPlotSynopsis});
	}

	public static final Parcelable.Creator<Movie> CREATOR= new Parcelable.Creator<Movie>() {

		@Override
		public Movie createFromParcel(Parcel source) {
			return new Movie(source);
		}

		@Override
		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};
}
