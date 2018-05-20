package com.example.alex.myapplication.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.alex.myapplication.model.chat.ChatObject;

/**
 * Created by alex on 2018/5/4.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBindView(ChatObject object);
}