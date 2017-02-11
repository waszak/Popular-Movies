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

package com.example.android.popularmovies;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

   /* @BindView(R.id.img_details_movie_poster) ImageView mImageView;
    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.tv_synopsis) TextView mSynopsis;
    @BindView(R.id.tv_score) TextView mScore;
    @BindView(R.id.tv_release_date) TextView mReleaseYear;*/

    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 3"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final PagerAdapter pagerViewAdapterDetails = new PagerAdapterMovie(fragmentManager);

        mViewPager.setAdapter(pagerViewAdapterDetails);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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


        /*Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Movie.TAG)) {

            Movie movie = intentThatStartedThisActivity.getParcelableExtra(Movie.TAG);
            String fileName = movie.getPosterFileName();
            mTitle.setText(movie.getTitle());
            mScore.setText("Score: "+String.format( "%.2f",movie.getScore())+"/10");
            mSynopsis.setText(movie.getPlotSynopsis());
            mReleaseYear.setText(movie.getReleaseDate());
            Picasso.with(this).load(NetworkUtils.buildPosterStringUrl(fileName)).into(mImageView);
        }*/

    }
}
