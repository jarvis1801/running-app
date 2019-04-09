package com.exmaple.jarvis.running;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.exmaple.jarvis.running.AsyncTask.getHistoryLoader;
import com.exmaple.jarvis.running.List.ResultListItemAdapter;
import com.exmaple.jarvis.running.Model.History;

import java.util.ArrayList;

public class ResultActivity extends FragmentActivity {
    private RecyclerView rv_history_list;
    private ResultListItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        rv_history_list = findViewById(R.id.rv_history_list);

        rv_history_list.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rv_history_list.setLayoutManager(mLayoutManager);

        final LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(1, null, getHistory).forceLoad();
    }

    private LoaderManager.LoaderCallbacks<ArrayList<History>> getHistory
            = new LoaderManager.LoaderCallbacks<ArrayList<History>>() {
        @Override
        public Loader<ArrayList<History>> onCreateLoader(int id, Bundle args) {
            String url = Config.url + "history";

            return new getHistoryLoader(ResultActivity.this, url);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<History>> loader, ArrayList<History> historyList) {
            mAdapter = new ResultListItemAdapter(ResultActivity.this, getSupportFragmentManager(), getSupportLoaderManager(), historyList);
            rv_history_list.setAdapter(mAdapter);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<History>> loader) {
        }
    };

}
