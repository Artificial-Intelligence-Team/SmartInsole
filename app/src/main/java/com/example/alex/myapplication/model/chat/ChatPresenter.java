package com.example.alex.myapplication.model.chat;

import java.util.ArrayList;

/**
 * 主要負責處理訊息的部分(Nature Language Precess)
 * Created by alex on 2018/5/4.
 */

public class ChatPresenter implements ChatContract.Presenter {

    private ArrayList<ChatObject> chatObjects;
    private ChatContract.View view;

    public ChatPresenter() {
        // Create the ArrayList for the chat objects
        this.chatObjects = new ArrayList<>();

        // Add an initial greeting message
        ChatResponse greetingMsg = new ChatResponse();
        greetingMsg.setText("哈囉~  我叫 Smart Insole 很高興為你服務~");
        chatObjects.add(greetingMsg);
        // 測試用
        ChatResponse responseObject = new ChatResponse();
        responseObject.setText("點選右下角~可以看到很多數據喔^0^...................................................................... ");
        chatObjects.add(responseObject);
    }

    @Override
    public void attachView(ChatContract.View view) {
        this.view = view;
    }

    @Override
    public ArrayList<ChatObject> getChatObjects() {
        return this.chatObjects;
    }

    @Override
    public void onEditTextActionDone(String inputText) {
        // Create new input object
        ChatInput inputObject = new ChatInput();
        inputObject.setText(inputText);

        // Add it to the list and tell the adapter we added something
        this.chatObjects.add(inputObject);
        view.notifyAdapterObjectAdded(chatObjects.size() - 1);
        // Add it to the list and tell the adapter we added something

        // 測試用
        ChatResponse responseObject = new ChatResponse();
        if (inputObject.getText().equals("love alex")) {
            responseObject.setText("I love you too! ");
        } else {
            responseObject.setText("Smart Insole 忙線中....");
        }
        this.chatObjects.add(responseObject);
        //view.notifyAdapterObjectAdded(chatObjects.size() - 1);

        // Also scroll down if we aren't at the bottom already
        view.scrollChatDown();
    }
}
