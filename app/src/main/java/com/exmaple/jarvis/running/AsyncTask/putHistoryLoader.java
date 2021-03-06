package com.exmaple.jarvis.running.AsyncTask;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import okhttp3.RequestBody;

public class putHistoryLoader extends AsyncTaskLoader {
    private String mUrl;
    private RequestBody mFormBody;

    public putHistoryLoader(Context context, String url, RequestBody formBody) {
        super(context);
        mUrl = url;
        mFormBody = formBody;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        if (mUrl == null)
            return null;

        String history = HistoryUtil.putHistory(mUrl, mFormBody);

        return history;
    }
}