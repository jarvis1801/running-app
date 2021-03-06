package com.exmaple.jarvis.running.Dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.exmaple.jarvis.running.AsyncTask.deleteHistoryLoader;
import com.exmaple.jarvis.running.AsyncTask.putHistoryLoader;
import com.exmaple.jarvis.running.Config;
import com.exmaple.jarvis.running.R;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class EditDialog extends DialogFragment {
    private EditText et_location;
    private TextView btn_submit;
    private String mLocation, mKey, mId;
    private int mPosition;
    private LoaderManager mLoaderManager;
    private RequestBody mFormBody;

    private Listener mListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_edit_location, container, false);

        getBundle();
        initView(v);
        setOnClick();

        mLoaderManager = getActivity().getSupportLoaderManager();

        return v;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public static interface Listener {
        void returnData(int position, String type, String val);
    }

    private void getBundle() {
        Bundle mArgs = getArguments();
        mKey = mArgs.getString("key");
        mLocation = mArgs.getString("location");
        mPosition = mArgs.getInt("position");
        mId = mArgs.getString("id");
    }

    private void initView(View v) {
        et_location = v.findViewById(R.id.et_location);

        et_location.setText(mLocation);

        btn_submit = v.findViewById(R.id.btn_submit);
    }

    private void setOnClick() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mLocation.equals(et_location.getText().toString())) {
                    mFormBody = new FormBody.Builder()
                            .add(mKey, et_location.getText().toString())
                            .build();
                    mLoaderManager.initLoader(5, null, putHistoryLoader).forceLoad();
                }
                dismiss();
            }
        });
    }

    private LoaderManager.LoaderCallbacks<String> putHistoryLoader = new LoaderManager.LoaderCallbacks<String>() {
        @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                String url = Config.url + "history/id/" + mId;
                return new putHistoryLoader(getContext(), url, mFormBody);
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String response) {
                if (mListener != null) {
                    mListener.returnData(mPosition, mKey, et_location.getText().toString());
                }
                mLoaderManager.destroyLoader(5);
            }

            @Override
            public void onLoaderReset(Loader<String> loader) {
            }
    };
}
