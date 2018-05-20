package com.example.alex.myapplication.model.chat;

import java.util.ArrayList;

/**
 * Created by alex on 2018/5/4.
 */

public interface ChatContract {

    interface View {

        void notifyAdapterObjectAdded(int position);

        void scrollChatDown();
    }

    interface Presenter {

        void attachView(ChatContract.View view);

        ArrayList<ChatObject> getChatObjects();

        void onEditTextActionDone(String inputText);
    }
}