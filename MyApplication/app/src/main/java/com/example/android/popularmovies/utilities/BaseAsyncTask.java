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
package com.example.android.popularmovies.utilities;


import android.os.AsyncTask;

public class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private ICallbackTask<Result> mCallbackTask;
    private ITask<Result> mTask;

    public interface ICallbackTask<Result>{
        void onStart();
        void onSuccess(Result result);
        void onError();
    }

    public interface ITask<Result>{
        Result task();
    }

    public BaseAsyncTask<Params, Progress, Result> setCallback(ICallbackTask<Result> callbackTask){
        mCallbackTask = callbackTask;
        return this;
    }

    public BaseAsyncTask<Params, Progress, Result> setTask(ITask<Result> task){
        mTask = task;
        return this;
    }

    @Override
    protected Result doInBackground(Params... params) {
        if(mTask != null){
            return  mTask.task();
        }
        return  null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mCallbackTask != null) {
            mCallbackTask.onStart();
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if(mCallbackTask != null) {
            if (result != null) {
                mCallbackTask.onSuccess(result);
            } else {
                mCallbackTask.onError();
            }
        }
    }
}
