package com.example.alex.myapplication.model.chat;

import com.example.alex.myapplication.model.chat.ChatObject;

/**
 *  聊天回覆的訊息物件.
 * Created by alex on 2018/5/4.
 */

public class ChatResponse extends ChatObject {
    @Override
    public int getType() {
        return ChatObject.RESPONSE_OBJECT;
    }
}
