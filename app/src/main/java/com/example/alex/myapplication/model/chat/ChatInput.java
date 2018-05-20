package com.example.alex.myapplication.model.chat;

/**
 *  聊天輸入的訊息物件.
 * Created by alex on 2018/5/4.
 */

public class ChatInput extends ChatObject {
    @Override
    public int getType() {
        return ChatObject.INPUT_OBJECT;
    }
}
