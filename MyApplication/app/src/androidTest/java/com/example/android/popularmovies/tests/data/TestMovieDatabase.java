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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import static com.example.android.popularmovies.tests.data.TestUtilities.getStaticIntegerField;
import static com.example.android.popularmovies.tests.data.TestUtilities.getStaticStringField;
import static com.example.android.popularmovies.tests.data.TestUtilities.studentReadableClassNotFound;
import static com.example.android.popularmovies.tests.data.TestUtilities.studentReadableNoSuchField;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by Waszak on 25.02.2017.
 */


/**
 * Used to test the database we use in Movie to cache movie data. Within these tests, we
 * test the following:
 * <p>
 * <p>
 * 1) Creation of the database with proper table(s)
 * 2) Insertion of single record into our movie table
 * 3) When a record is already stored in the movie table with a movie id, a new record
 * with the same date will overwrite that record.
 * 4) Verify that NON NULL constraints are working properly on record inserts
 * 5) Verify auto increment is working with the ID
 * 6) Test the onUpgrade functionality of the MovieDbHelper
 */
@RunWith(AndroidJUnit4.class)
public class TestMovieDatabase {

    /*
     * Context used to perform operations on the database and create WeatherDbHelpers.
     */
    private final Context context = InstrumentationRegistry.getTargetContext();

    /*
     * In order to verify that you have set up your classes properly and followed our TODOs, we
     * need to create what's called a Change Detector Test. In almost any other situation, these
     * tests are discouraged, as they provide no real value in a production setting. However, using
     * reflection to verify that you have set your classes up correctly will help provide more
     * useful errors if you've missed a step in our instructions.
     *
     * Additionally, using reflection for these tests allows you to run the tests when they
     * normally wouldn't compile, as they depend on pieces of your classes that you might not
     * have created when you initially run the tests.
     */
    private static final String packageName = "com.example.android.popularmovies";
    private static final String dataPackageName = packageName + ".data";

    private Class movieEntryClass;
    private Class movieDbHelperClass;
    private static final String movieContractName = ".MovieContract";
    private static final String movieEntryName = movieContractName + "$MovieEntry";
    private static final String movieDbHelperName = ".MovieDbHelper";

    private static final String databaseNameVariableName = "DATABASE_NAME";
    private static String REFLECTED_DATABASE_NAME;

    private static final String databaseVersionVariableName = "DATABASE_VERSION";
    private static int REFLECTED_DATABASE_VERSION;

    private static final String tableNameVariableName = "TABLE_NAME";
    private static String REFLECTED_TABLE_NAME;

    private static final String columnMovieIdVariableName = "COLUMN_MOVIE_ID";
    static String REFLECTED_COLUMN_MOVIE_ID;


    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    @Before
    public void before() {
        try {

            movieEntryClass = Class.forName(dataPackageName + movieEntryName);
            if (!BaseColumns.class.isAssignableFrom(movieEntryClass)) {
                String movieEntryDoesNotImplementBaseColumns = "MovieEntry class needs to " +
                        "implement the interface BaseColumns, but does not.";
                fail(movieEntryDoesNotImplementBaseColumns);
            }

            REFLECTED_TABLE_NAME = getStaticStringField(movieEntryClass, tableNameVariableName);

            REFLECTED_COLUMN_MOVIE_ID = getStaticStringField(movieEntryClass,
                    columnMovieIdVariableName);

            movieDbHelperClass = Class.forName(dataPackageName + movieDbHelperName);

            Class movieDbHelperClassSuperclass = movieDbHelperClass.getSuperclass();

            if (movieDbHelperClassSuperclass == null
                    || movieDbHelperClassSuperclass.equals(Object.class)) {
                String noExplicitSuperclass =
                        "MovieDbHelper needs to extend SQLiteOpenHelper, but yours currently doesn't extend a class at all.";
                fail(noExplicitSuperclass);
            } else if (movieDbHelperClassSuperclass != null) {
                String movieDbHelperClassSuperclassSimpleName = movieDbHelperClassSuperclass.getSimpleName();
                String doesNotExtendOpenHelper =
                        "MovieDbHelper needs to extend SQLiteOpenHelper but yours extends "
                                + movieDbHelperClassSuperclassSimpleName;

                assertTrue(doesNotExtendOpenHelper,
                        SQLiteOpenHelper.class.isAssignableFrom(movieDbHelperClassSuperclass));
            }

            REFLECTED_DATABASE_NAME = getStaticStringField(
                    movieDbHelperClass, databaseNameVariableName);

            REFLECTED_DATABASE_VERSION = getStaticIntegerField(
                    movieDbHelperClass, databaseVersionVariableName);

            Constructor movieDbHelperClassConstructor = movieDbHelperClass.getConstructor(Context.class);

            dbHelper = (SQLiteOpenHelper) movieDbHelperClassConstructor.newInstance(context);

            context.deleteDatabase(REFLECTED_DATABASE_NAME);

            Method getWritableDatabase = SQLiteOpenHelper.class.getDeclaredMethod("getWritableDatabase");
            database = (SQLiteDatabase) getWritableDatabase.invoke(dbHelper);

        } catch (ClassNotFoundException e) {
            fail(studentReadableClassNotFound(e));
        } catch (NoSuchFieldException e) {
            fail(studentReadableNoSuchField(e));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDatabaseVersionWasIncremented() {
        int expectedDatabaseVersion = MovieDbHelper.DATABASE_VERSION;
        String databaseVersionShouldBe1 = "Database version should be "
                + expectedDatabaseVersion + " but isn't."
                + "\n Database version: ";

        assertEquals(databaseVersionShouldBe1,
                expectedDatabaseVersion,
                REFLECTED_DATABASE_VERSION);
    }

    /**
     * Tests to ensure that inserts into your database results in automatically incrementing row
     * IDs and that row IDs are not reused.
     */
    @Test
    public void testDuplicateMovieIdInsertBehaviorShouldReplace() {

        /* Obtain movie values from TestUtilities */
        ContentValues testMovieValues = TestUtilities.createTestMovieContentValues();

        /*
         * Get the original movie ID of the testMovieValues to ensure we use a different
         * movie ID for our next insert.
         */
        long originalMovieId = testMovieValues.getAsLong(REFLECTED_COLUMN_MOVIE_ID);

        /* Insert the ContentValues with old movie ID into database */
        database.insert(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                testMovieValues);

        //The same ID for movie
        long newMovieId = originalMovieId ;

        testMovieValues.put(REFLECTED_COLUMN_MOVIE_ID, newMovieId);

        /* Insert the ContentValues with new movie ID into database */
        database.insert(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                testMovieValues);

        /* Query for a movie record with our new movie ID */
        Cursor newMovieIdCursor = database.query(
                REFLECTED_TABLE_NAME,
                new String[]{REFLECTED_COLUMN_MOVIE_ID},
                null,
                null,
                null,
                null,
                null);

        String recordWithNewIdNotFound =
                "New record did not overwrite the previous record for the same date.";
        assertTrue(recordWithNewIdNotFound,
                newMovieIdCursor.getCount() == 1);

        assertTrue(recordWithNewIdNotFound,
                newMovieId == originalMovieId);

        /* Always close the cursor after you're done with it */
        newMovieIdCursor.close();
    }


    /**
     * This method tests inserting a single record into an empty table from a brand new database.
     * It will fail for the following reasons:
     * <p>
     * 1) Problem creating the database
     * 2) A value of -1 for the ID of a single, inserted record
     * 3) An empty cursor returned from query on the movie table
     * 4) Actual values of movie data not matching the values from TestUtilities
     */
    @Test
    public void testInsertSingleRecordIntoMovieTable() {

        /* Obtain movie values from TestUtilities */
        ContentValues testMovieContentValues = TestUtilities.createTestMovieContentValues();

        /* Insert ContentValues into database and get a row ID back */
        long movieRowId = database.insertOrThrow(
                REFLECTED_TABLE_NAME,
                null,
                testMovieContentValues);

        /* If the insert fails, database.insert returns -1 */
        int valueOfIdIfInsertFails = -1;
        String insertFailed = "Unable to insert into the database";
        assertNotSame(insertFailed,
                valueOfIdIfInsertFails,
                movieRowId);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor movieCursor = database.query(
                /* Name of table on which to perform the query */
                REFLECTED_TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from movie query";
        assertTrue(emptyQueryError,
                movieCursor.moveToFirst());

        /* Verify that the returned results match the expected results */
        String expectedMovie =
                "Expected movie values didn't match actual values.";
        TestUtilities.validateCurrentRecord(expectedMovie,
                movieCursor,
                testMovieContentValues);

        /*
         * Since before every method annotated with the @Test annotation, the database is
         * deleted, we can assume in this method that there should only be one record in our
         * Movie table because we inserted it. If there is more than one record, an issue has
         * occurred.
         */
        assertFalse("Error: More than one record returned from movie query",
                movieCursor.moveToNext());

        /* Close cursor */
        movieCursor.close();
    }


    /**
     * This method tests that our database contains all of the tables that we think it should
     * contain. Although in our case, we just have one table that we expect should be added
     * <p>
     * {@link com.example.android.popularmovies.data.MovieContract.MovieEntry#TABLE_NAME}.
     * <p>
     * Despite only needing to check one table name in PopularMovies, we set this method up so that
     * you can use it in other apps to test databases with more than one table.
     */
    @Test
    public void testCreateDb() {
        /*
         * Will contain the name of every table in our database. Even though in our case, we only
         * have only table, in many cases, there are multiple tables. Because of that, we are
         * showing you how to test that a database with multiple tables was created properly.
         */
        final HashSet<String> tableNameHashSet = new HashSet<>();

        /* Here, we add the name of our only table in this particular database */
        tableNameHashSet.add(REFLECTED_TABLE_NAME);

        /* We think the database is open, let's verify that here */
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'",
                null);

        /*
         * If tableNameCursor.moveToFirst returns false from this query, it means the database
         * wasn't created properly. In actuality, it means that your database contains no tables.
         */
        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";
        assertTrue(errorInCreatingDatabase,
                tableNameCursor.moveToFirst());

        /*
         * tableNameCursor contains the name of each table in this database. Here, we loop over
         * each table that was ACTUALLY created in the database and remove it from the
         * tableNameHashSet to keep track of the fact that was added. At the end of this loop, we
         * should have removed every table name that we thought we should have in our database.
         * If the tableNameHashSet isn't empty after this loop, there was a table that wasn't
         * created properly.
         */
        do {
            tableNameHashSet.remove(tableNameCursor.getString(0));
        } while (tableNameCursor.moveToNext());

        /* If this fails, it means that your database doesn't contain the expected table(s) */
        assertTrue("Error: Your database was created without the expected tables.",
                tableNameHashSet.isEmpty());

        /* Always close the cursor when you are finished with it */
        tableNameCursor.close();
    }


    /**
     * This method tests the {@link com.example.android.popularmovies.data.MovieDbHelper#onUpgrade(SQLiteDatabase, int, int)}. The proper
     * behavior for this method in our case is to simply DROP (or delete) the movie table from
     * the database and then have the table recreated.
     */
    @Test
    public void testOnUpgradeBehavesCorrectly() {

        testInsertSingleRecordIntoMovieTable();

        dbHelper.onUpgrade(database, 13, 14);

        /*
         * This Cursor will contain the names of each table in our database and we will use it to
         * make sure that our movie table is still in the database after upgrading.
         */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" + REFLECTED_TABLE_NAME + "'",
                null);

        /*
         * Our database should only contain one table, and so the above query should have one
         * record in the cursor that queried for our table names.
         */
        int expectedTableCount = 1;
        String shouldHaveSingleTable = "There should only be one table returned from this query.";
        assertEquals(shouldHaveSingleTable,
                expectedTableCount,
                tableNameCursor.getCount());

        /* We are done verifying our table names, so we can close this cursor */
        tableNameCursor.close();

        Cursor shouldBeEmptyMovieCursor = database.query(
                REFLECTED_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        int expectedRecordCountAfterUpgrade = 0;
        /* We will finally verify that our movie table is empty after */
        String movieTableShouldBeEmpty =
                "Movie table should be empty after upgrade, but wasn't."
                        + "\nNumber of records: ";
        assertEquals(movieTableShouldBeEmpty,
                expectedRecordCountAfterUpgrade,
                shouldBeEmptyMovieCursor.getCount());

        /* Test is over, close the cursor */
        shouldBeEmptyMovieCursor.close();
        database.close();
    }

}