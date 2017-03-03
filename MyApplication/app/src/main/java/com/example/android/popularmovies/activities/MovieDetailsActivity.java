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

package com.example.android.popularmovies.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.PagerAdapterMovie;
import com.example.android.popularmovies.fragments.MovieDescriptionFragment;
import com.example.android.popularmovies.fragments.MovieReviewsFragment;
import com.example.android.popularmovies.fragments.MovieTrailersFragment;
import com.example.android.popularmovies.models.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* Details activity shows detailed information about selected movie.
*/
public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.toolbar)  Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.details)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.reviews)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.trailers)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentManager fragmentManager = getSupportFragmentManager();

        final PagerAdapterMovie pagerViewAdapterDetails = new PagerAdapterMovie(fragmentManager);
        Intent intentThatStartedThisActivity = getIntent();

        Movie movie = null;
        if (intentThatStartedThisActivity.hasExtra(Movie.TAG)) {
            movie = intentThatStartedThisActivity.getParcelableExtra(Movie.TAG);
        }
        pagerViewAdapterDetails.addTab(MovieDescriptionFragment.newInstance(movie));
        pagerViewAdapterDetails.addTab(new MovieReviewsFragment());
        pagerViewAdapterDetails.addTab(MovieTrailersFragment.newInstance(movie));
        //setOffscreenPageLimit allows us to fetch trailer before going opening it.
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

    }
}