package com.example.alex.myapplication.view;

import android.view.View;
import android.widget.TextView;

import com.example.alex.myapplication.R;
import com.example.alex.myapplication.model.chat.ChatObject;

/**
 * 聊天回覆的 UI 顯示物件.
 * Created by alex on 2018/5/4.
 */

public class ChatResponseVH extends BaseViewHolder {

    private TextView tvResponseText;

    public ChatResponseVH(View itemView) {
        super(itemView);
        this.tvResponseText = (TextView) itemView.findViewById(R.id.tv_response_text);
    }

    @Override
    public void onBindView(ChatObject object) {
        this.tvResponseText.setText(object.getText());
    }
}
