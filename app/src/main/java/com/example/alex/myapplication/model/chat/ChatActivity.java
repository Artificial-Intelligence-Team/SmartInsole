package com.example.alex.myapplication.model.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.DisplayMetrics;
import com.example.alex.myapplication.R;
import com.example.alex.myapplication.model.data_presenter.DataActivity;
import com.example.alex.myapplication.view.ChatAdapter;

/**
 * 此塊唯一開始初始化的狀態處理與顯示.
 * Created by alex on 2018/5/4.
 */

public class ChatActivity extends AppCompatActivity implements ChatContract.View {

    // View
    private RecyclerView rvChatList;
    private ChatAdapter chatAdapter;

    // Presenter
    private ChatPresenter presenter;
    private DisplayMetrics metrics = new DisplayMetrics();

    // Image Button
    private ImageButton androidImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        rvChatList = findViewById(R.id.rv_chat);
        //etSearchBox = (EditText) findViewById(R.id.et_search_box);
        //etSearchBox.setOnEditorActionListener(searchBoxListener);

        // Instantiate presenter and attach view
        this.presenter = new ChatPresenter();
        presenter.attachView(this);

        // Instantiate the adapter and give it the list of chat objects
        this.chatAdapter = new ChatAdapter(presenter.getChatObjects());

        // Set up the RecyclerView with adapter and layout manager
        rvChatList.setAdapter(chatAdapter);
        rvChatList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvChatList.setItemAnimator(new DefaultItemAnimator());
        androidImageButton = findViewById(R.id.imageButton2);
        androidImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatActivity.this  , DataActivity.class);
                startActivity(intent);
            }
        });
        try {
            //取的 intent 中的 bundle 物件
            Bundle bundle =this.getIntent().getExtras();
            int temperature = bundle.getInt("temperature");
            int humidity = bundle.getInt("humidity");
            chatRespond(temperature, humidity);
        } catch (Exception e) {
            System.out.println("error to catch");
        }
    }

    /**
     * 根據不同的溫溼度給予相對的回應建議
     * @param temperature 溫度
     * @param humidity 濕度
     */
    public void chatRespond(int temperature, int humidity) {
        ChatResponse chatResponse = new ChatResponse();
        if(temperature == 0 && humidity == 0) {
            chatResponse.setText("Nothing to Do! " + temperature + " and " + humidity);
            presenter.bulletin(chatResponse);
        } else if(temperature == 10 && humidity == 10) {
            chatResponse.setText("Nothing to Do!"  + temperature + " and " + humidity);
            presenter.bulletin(chatResponse);
        } else if (humidity >= 75) {
            chatResponse.setText("你現在的濕度為" + humidity + "%，現在濕度有點高喔！提供給你除濕的小撇步，" +
                    "可將蘇打粉做成小除濕包，將它放到鞋子中不只可以除濕還可以防霉喔");
            presenter.bulletin(chatResponse);
        } else if(temperature > 21 && humidity >= 62) {
            chatResponse.setText("你目前鞋子中的溫度為" + temperature + "度，濕度為"
                    + humidity + "%，在此環境下很適合黴菌生長，提醒你要適時的讓腳通風才不會得香港腳喔!");
            presenter.bulletin(chatResponse);
        } else {
            chatResponse.setText("你目前鞋子中的溫度為" + temperature + "度，濕度為"
                    + humidity + "%，在此環境下黴菌可能會增長~ 如果可以請脫下鞋子通風一下喔");
            presenter.bulletin(chatResponse);
        }
    }

    @Override
    public void notifyAdapterObjectAdded(int position) {
        this.chatAdapter.notifyItemInserted(position);
    }

    @Override
    public void scrollChatDown() {
        this.rvChatList.scrollToPosition(presenter.getChatObjects().size() - 1);
    }

    /**
     *  當使用者有進行任何輸入時，我將給予  Presenter 做處理，並將原始訊息做清除。
     */
    private EditText.OnEditorActionListener searchBoxListener = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView tv, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!TextUtils.isEmpty(tv.getText())) {
                    presenter.onEditTextActionDone(tv.getText().toString());
                    //etSearchBox.getText().clear();
                    return true;
                }
            }
            return false;
        }
    };
}
