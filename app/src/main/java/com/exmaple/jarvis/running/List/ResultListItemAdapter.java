package com.exmaple.jarvis.running.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exmaple.jarvis.running.AsyncTask.HistoryLoader;
import com.exmaple.jarvis.running.AsyncTask.deleteHistoryLoader;
import com.exmaple.jarvis.running.Config;
import com.exmaple.jarvis.running.Dialog.EditDialog;
import com.exmaple.jarvis.running.MainActivity;
import com.exmaple.jarvis.running.Model.History;
import com.exmaple.jarvis.running.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ResultListItemAdapter extends RecyclerView.Adapter<ResultListItemViewHolder> implements EditDialog.Listener {
    private Context mContext;
    private ArrayList<History> mData;
    private FragmentManager mFragmentManager;
    private AlertDialog.Builder mDeleteDialog;
    private final LoaderManager mLoaderManager;

    private boolean isDeleted = false;


    public ResultListItemAdapter(Context context, FragmentManager fragmentManager, LoaderManager loaderManager, ArrayList<History> data) {
        mContext = context;
        mData = data;
        mFragmentManager = fragmentManager;
        mLoaderManager = loaderManager;
        mDeleteDialog = new AlertDialog.Builder(mContext)
            .setTitle("Are you sure to delete this record?");
    }

    @NonNull
    @Override
    public ResultListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = (View) LayoutInflater.from(mContext)
                .inflate(R.layout.result_list_item, viewGroup, false);

        ResultListItemViewHolder holder = new ResultListItemViewHolder(view);
        holder.tv_duration_value = view.findViewById(R.id.tv_duration_value);
        holder.tv_created_at_value = view.findViewById(R.id.tv_created_at_value);
        holder.tv_original_value = view.findViewById(R.id.tv_original_value);
        holder.tv_destination_value = view.findViewById(R.id.tv_destination_value);

        holder.img_original = view.findViewById(R.id.img_original);
        holder.img_destination = view.findViewById(R.id.img_destination);
        holder.img_delete = view.findViewById(R.id.img_delete);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultListItemViewHolder resultListItemViewHolder, int i) {
        final History currentHistory = mData.get(i);
        setElement(currentHistory, resultListItemViewHolder);

        setOnClick(currentHistory, resultListItemViewHolder, i);
    }

    private void setElement(History currentHistory, ResultListItemViewHolder resultListItemViewHolder) {
        resultListItemViewHolder.tv_duration_value.setText(currentHistory.getDuration());
        resultListItemViewHolder.tv_created_at_value.setText(dateFormat(currentHistory.getCreatedAt()));
        resultListItemViewHolder.tv_original_value.setText(currentHistory.getOriginal());
        resultListItemViewHolder.tv_destination_value.setText(currentHistory.getDestination());
    }

    private void setOnClick(final History currentHistory, final ResultListItemViewHolder resultListItemViewHolder, final int position) {
        resultListItemViewHolder.img_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog dialog = new EditDialog();
                Bundle args = new Bundle();
                args.putString("location", resultListItemViewHolder.tv_destination_value.getText().toString());
                args.putString("key", "destination");
                args.putInt("position", position);
                args.putString("id", currentHistory.getId());

                dialog.setArguments(args);
                dialog.show(mFragmentManager, "dialog");
            }
        });

        resultListItemViewHolder.img_original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog dialog = new EditDialog();
                Bundle args = new Bundle();
                args.putString("location", resultListItemViewHolder.tv_original_value.getText().toString());
                args.putString("key", "original");
                args.putInt("position", position);
                args.putString("id", currentHistory.getId());

                dialog.setArguments(args);
                dialog.setListener(ResultListItemAdapter.this);
                dialog.show(mFragmentManager, "dialog");
            }
        });

        resultListItemViewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog setting
                mDeleteDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        if (isDeleted) {
                            mLoaderManager.initLoader(3, null, deleteHistoryLoader(currentHistory.getId(), position)).forceLoad();
//                        } else {
//                            mLoaderManager.initLoader(3, null, deleteHistoryLoader(currentHistory.getId(), position)).forceLoad();
//                        }
                        dialog.dismiss();
                    }
                });
                mDeleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                mDeleteDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private String dateFormat(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINESE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.CHINESE);
        String value = null;
        try {
            value = dateFormat.format(sdf.parse(dateStr));
        } catch (ParseException e) {
                e.printStackTrace();
        }
        return value;
    }

    private LoaderManager.LoaderCallbacks<String> deleteHistoryLoader(final String mId, final int position) {
        return new LoaderManager.LoaderCallbacks<String>() {
            @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                String url = Config.url + "history/id/" + mId;
                return new deleteHistoryLoader(mContext, url);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<String> loader, String response) {
                mData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mData.size());
                mLoaderManager.destroyLoader(3);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<String> loader) {
            }
        };
    }

    @Override
    public void returnData(int position, String type, String val) {
        final History currentHistory = mData.get(position);
        if (type.equals("original")) {
            currentHistory.setOriginal(val);
        } else if (type.equals("destination")) {
            currentHistory.setDestination(val);
        }
        mData.remove(position);
        mData.add(position, currentHistory);
        notifyItemRangeChanged(position, mData.size());
    }
}
