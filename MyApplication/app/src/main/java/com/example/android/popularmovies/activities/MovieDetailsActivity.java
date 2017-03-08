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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.PagerAdapterMovie;
import com.example.android.popularmovies.fragments.MovieDescriptionFragment;
import com.example.android.popularmovies.fragments.MovieReviewsFragment;
import com.example.android.popularmovies.fragments.MovieTrailersFragment;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.IMovieListListener;
import com.example.android.popularmovies.utilities.NetworkUtils;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
* Details activity shows detailed information about selected mMovie.
*/
public class MovieDetailsActivity extends AppCompatActivity
    implements IMovieListListener {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.toolbar)  Toolbar mToolbar;
    @BindBool(R.bool.isPaneLayout) boolean mIsPaneLayout;

    private Movie mMovie;
    private static final String MOVIE_ACTIVE =  Movie.TAG;
    public static final String MOVIES_ADAPTER_STATE = MainActivity.MOVIES_ADAPTER_STATE;

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

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Movie.TAG)) {
            mMovie = intentThatStartedThisActivity.getParcelableExtra(Movie.TAG);
        }
        onRestore(intentThatStartedThisActivity.getExtras());

    }

    private void onRestore(Bundle savedInstanceState){
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(MOVIE_ACTIVE)){
                mMovie = savedInstanceState.getParcelable(MOVIE_ACTIVE);
            }
        }
        //If pane layout recreate activity.
        if(mIsPaneLayout){
            Log.d(MovieDetailsActivity.TAG,"Recreating activity in pane layout");
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            mainActivityIntent.putExtras(savedInstanceState);
            startActivity(mainActivityIntent);
        }
        setupFragments();
    }

    private void setupFragments(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        final PagerAdapterMovie pagerViewAdapterDetails = new PagerAdapterMovie(fragmentManager);

        pagerViewAdapterDetails.addTab(MovieDescriptionFragment.newInstance(mMovie));
        pagerViewAdapterDetails.addTab(MovieReviewsFragment.newInstance(mMovie));
        pagerViewAdapterDetails.addTab(MovieTrailersFragment.newInstance(mMovie));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if(mMovie!= null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Movie.TAG, mMovie);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    @Override
    public void UpdateMovie(Movie movie, boolean add) {
        //do nothing
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        //We need this to recover fragment details on tablet.
        if (mMovie != null) {
            state.putParcelable(MOVIE_ACTIVE, mMovie);
        }
        super.onSaveInstanceState(state);
    }

}
