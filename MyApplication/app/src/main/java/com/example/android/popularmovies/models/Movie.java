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

package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
* Class represents movie object. That was create from Json.
*
*/
public class Movie implements Parcelable {

    public static final String TAG =  Movie.class.getSimpleName();

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("poster_path")
    @Expose
    private String mPosterFileName;

    @SerializedName("release_date")
    @Expose
    private String mReleaseDate;

    @SerializedName("vote_average")
    @Expose
    private float mVoteAverage;

    @SerializedName("overview")
    @Expose
    private String mPlotSynopsis;

    @SerializedName("backdrop_path")
    @Expose
    private String mBackDrop;

    @SerializedName("id")
    @Expose
    private int mId;

    private boolean mIsFavourite;

    public Movie(Parcel in){
        mTitle= in.readString();
        mPosterFileName= in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readFloat();
        mPlotSynopsis = in.readString();
        mId = in.readInt();
        mBackDrop = in.readString();
        mIsFavourite = in.readByte() == 1 ? Boolean.TRUE: Boolean.FALSE;
    }

    public Movie(){

    }

    public void setFavourite(boolean isFavourite){
        mIsFavourite = isFavourite;
    }

    public boolean isFavourite(){return  mIsFavourite;}

    public int getId(){ return mId; }

    public String getPosterFileName(){
        return mPosterFileName;
    }

    public String getBackDropFileName(){
        return mBackDrop;
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

    public float getScore(){ return mVoteAverage; }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPosterFileName);
        dest.writeString(mReleaseDate);
        dest.writeFloat(mVoteAverage);
        dest.writeString(mPlotSynopsis);
        dest.writeInt(mId);
        dest.writeString(mBackDrop);
        dest.writeByte((byte)(mIsFavourite ? 1:0));
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        mPlotSynopsis = plotSynopsis;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setScore(float score) {
        mVoteAverage = score;
    }

    public void setBackDropFileName(String backDropFileName) {
        mBackDrop = backDropFileName;
    }

    public void setPosterFileName(String posterFileName) {
        mPosterFileName = posterFileName;
    }
}
