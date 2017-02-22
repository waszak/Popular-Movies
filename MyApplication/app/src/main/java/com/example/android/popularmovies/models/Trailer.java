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

/**
 * Created by Waszak on 20.02.2017.
 */

public class Trailer implements Parcelable {

    private String mName;
    private String mKey;
    private int mSize;
    private String mType;
    private String mSite;

    public  Trailer(String name,String key,int size,String type,String site){
        mName = name;
        mKey = key;
        mSize = size;
        mType = type;
        mSite = site;
    }

    public String getKey(){
        return mKey;
    }

    public String getName(){
        return mName;
    }

    public String getDescription(){
        return mSite+" "+mSize+"p";
    }

    public String getType(){
        return mType;
    }


    public Trailer(Parcel in) {
        String[] data= new String[5];
        in.readStringArray(data);

        mName= data[0];
        mKey= data[1];
        mSize = Integer.parseInt(data[2]);
        mType = data[3];
        mSite = data[4];
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{mName,mKey, Integer.toString(mSize),
                mType, mSite});
    }
}
