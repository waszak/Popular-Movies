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

package com.example.android.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MoviesAdapter;

/**
 * Created by Waszak on 05.03.2017.
 */

public class MoviePreferences {

    public static final String PREF_SORT_MODE = "sort_mode";
    public static final String PREF_FAVORITES = "favorites";

    public static void setSortMode(Context context, MoviesAdapter.SORT_MODE mode) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(PREF_SORT_MODE, mode.getValue());
        editor.apply();
    }

    public static void setFavorites(Context context, boolean isFavorites) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(PREF_FAVORITES, isFavorites);
        editor.apply();
    }

    public static MoviesAdapter.SORT_MODE getSortMode(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int sortMode = sp.getInt(PREF_SORT_MODE,
                context.getResources().getInteger(R.integer.sort_mode));
        return MoviesAdapter.SORT_MODE.getInstance(sortMode);
    }

    public static boolean getFavorites(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_FAVORITES, context.getResources().getBoolean(R.bool.favorite));
    }

}
