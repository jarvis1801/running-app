package com.exmaple.jarvis.running.AsyncTask;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class deleteHistoryLoader extends AsyncTaskLoader {
    private String mUrl;

    public deleteHistoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
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

        Object history = HistoryUtil.deleteHistory(mUrl);
        return history;
    }
}