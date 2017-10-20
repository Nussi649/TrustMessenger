package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import backend.ChatSegment;
import backend.be.ChatBE;

/**
 * Created by ich on 14.10.2017.
 */

public class OverviewActivity extends AbstractActivity {

    LinearLayout mainContainer;
    List<ChatBE> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
    }

    @Override
    protected void workingThread() {
        getData();
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    private void getData() {
        controller.loadContactList();
        controller.loadChatList();
        chats = model.chats;
    }

    private void populateUI() {
        mainContainer = (LinearLayout)findViewById(R.id.main_content);
        setTitle(R.string.activity_overview);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        if (chats != null) {
            for (ChatBE chat : chats) {
                final ChatBE finalChat = chat;
                ChatSegment chatSegment = new ChatSegment(this, finalChat);
                chatSegment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getController().setCurChat(finalChat);
                        startActivity(ConversationActivity.class);
                    }
                });
                mainContainer.addView(chatSegment);
            }
        }
        buildDrawer();
    }
}
