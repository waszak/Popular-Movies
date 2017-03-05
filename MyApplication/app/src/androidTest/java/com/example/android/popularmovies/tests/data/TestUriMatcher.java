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

package com.example.android.popularmovies.tests.data;

/**
 * Created by Waszak on 25.02.2017.
 */

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.example.android.popularmovies.tests.data.TestUtilities.getStaticIntegerField;
import static com.example.android.popularmovies.tests.data.TestUtilities.studentReadableNoSuchField;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;


@RunWith(AndroidJUnit4.class)
public class TestUriMatcher {

    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_DIR_WITH_ID = MovieContract.MovieEntry.buildMovieUriWithId(TestUtilities.MOVIE_ID);

    private static final String movieCodeVariableName = "CODE_MOVIE";
    private static int REFLECTED_MOVIE_CODE;

    private static final String movieCodeWithIdVariableName = "CODE_MOVIE_WITH_ID";
    private static int REFLECTED_MOVIE_CODE_WITH_ID;

    private UriMatcher testMatcher;

    @Before
    public void before() {
        try {

            Method buildUriMatcher = MovieProvider.class.getDeclaredMethod("buildUriMatcher");
            testMatcher = (UriMatcher) buildUriMatcher.invoke(MovieProvider.class);

            REFLECTED_MOVIE_CODE = getStaticIntegerField(
                    MovieProvider.class,
                    movieCodeVariableName);

            REFLECTED_MOVIE_CODE_WITH_ID = getStaticIntegerField(
                    MovieProvider.class,
                    movieCodeWithIdVariableName);

        } catch (NoSuchFieldException e) {
            fail(studentReadableNoSuchField(e));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            String noBuildUriMatcherMethodFound =
                    "It doesn't appear that you have created a method called buildUriMatcher in " +
                            "the MovieProvider class.";
            fail(noBuildUriMatcherMethodFound);
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testUriMatcher() {

        /* Test that the code returned from our matcher matches the expected movie code */
        String movieUriDoesNotMatch = "Error: The CODE_MOVIE URI was matched incorrectly.";
        int actualMovie = testMatcher.match(TEST_MOVIE_DIR);
        int expectedMovieCode = REFLECTED_MOVIE_CODE;
        assertEquals(movieUriDoesNotMatch,
                expectedMovieCode,
                actualMovie);

        /*
         * Test that the code returned from our matcher matches the expected movie with id code
         */
        String movieWithIdUriCodeDoesNotMatch =
                "Error: The CODE_MOVIE WITH ID URI was matched incorrectly.";
        int actualMovieWithIdCode = testMatcher.match(TEST_MOVIE_DIR_WITH_ID);
        int expectedMovieWithIdCode = REFLECTED_MOVIE_CODE_WITH_ID;
        assertEquals(movieWithIdUriCodeDoesNotMatch,
                expectedMovieWithIdCode,
                actualMovieWithIdCode);
    }
}
