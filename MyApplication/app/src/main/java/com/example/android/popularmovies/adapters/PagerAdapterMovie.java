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


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;

/**
 * Created by Waszak on 13.02.2017.
 */

public class PagerAdapterMovie extends FragmentPagerAdapter {

    private final ArrayList<Fragment> mTabs;

    public PagerAdapterMovie(FragmentManager manager){
        super(manager);
        mTabs = new ArrayList<>();
    }

    public void addTab(Fragment tab){
        mTabs.add(tab);
    }

    public Fragment getItem(int position) {
        if(position > mTabs.size() -1){
            return  null;
        }
        return mTabs.get(position);

    }

    @Override
    public int getCount() {
        return mTabs.size();
    }
}
