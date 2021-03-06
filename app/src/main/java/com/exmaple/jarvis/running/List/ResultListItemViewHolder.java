package com.exmaple.jarvis.running.List;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultListItemViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_duration_value, tv_created_at_value, tv_original_value, tv_destination_value;
    public ImageView img_original, img_destination, img_delete;

    public ResultListItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
