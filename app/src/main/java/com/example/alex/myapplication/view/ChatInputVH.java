package com.example.alex.myapplication.view;

import android.view.View;
import android.widget.TextView;

import com.example.alex.myapplication.R;
import com.example.alex.myapplication.model.chat.ChatObject;

/**
 * 聊天輸入的 UI 顯示物件.
 * Created by alex on 2018/5/4.
 */

public class ChatInputVH extends BaseViewHolder {

    private TextView tvInputText;

    public ChatInputVH(View itemView) {
        super(itemView);
        this.tvInputText = (TextView) itemView.findViewById(R.id.tv_input_text);
    }

    @Override
    public void onBindView(ChatObject object) {
        this.tvInputText.setText(object.getText());
    }
}
