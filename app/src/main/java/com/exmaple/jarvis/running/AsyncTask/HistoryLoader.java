package com.exmaple.jarvis.running.AsyncTask;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;

public class HistoryLoader extends AsyncTaskLoader {
    private String mUrl;
    private String mDuration;

    public HistoryLoader(Context context, String url, String duration) {
        super(context);
        mUrl = url;
        mDuration = duration;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        if (mUrl == null)
            return null;

        Object history = HistoryUtil.postHistory(mUrl, mDuration);
        return history;
    }
}
