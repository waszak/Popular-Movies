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

package com.example.android.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.PagerAdapterMovie;
import com.example.android.popularmovies.models.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;


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

/**
 * Created by Waszak on 01.03.2017.
 */

public class MovieDetailsFragment extends Fragment {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_movie_details, container, false);
        ButterKnife.bind(this, view);


        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.details)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.reviews)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.trailers)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentManager fragmentManager = getChildFragmentManager();

        final PagerAdapterMovie pagerViewAdapterDetails = new PagerAdapterMovie(fragmentManager);
        Bundle bundleThatStartedThisActivity = getArguments();

        Movie movie = null;
        if (bundleThatStartedThisActivity.containsKey(Movie.TAG)) {
            movie = bundleThatStartedThisActivity.getParcelable(Movie.TAG);
        }
        pagerViewAdapterDetails.addTab(MovieDescriptionFragment.newInstance(movie));
        pagerViewAdapterDetails.addTab(MovieReviewsFragment.newInstance(movie));
        pagerViewAdapterDetails.addTab(MovieTrailersFragment.newInstance(movie));
        mViewPager.setOffscreenPageLimit(mTabLayout.getTabCount());
        mViewPager.setAdapter(pagerViewAdapterDetails);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return  view;
    }

    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment =  new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Movie.TAG,movie);
        fragment.setArguments(args);
        return fragment;
    }
}
