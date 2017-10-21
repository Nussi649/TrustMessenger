package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import backend.be.ChatBE;
import backend.be.ContactBE;
import backend.be.MessageBE;

/**
 * Created by ich on 16.10.2017.
 */

public class ConversationActivity extends AbstractActivity {
    ContactBE partner;
    ChatBE chat;
    List<MessageBE> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void workingThread() {
        chat = model.curChat;
        partner = chat.partner;
        loadMessages();
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    private void loadMessages() {
        messages = getController().loadMessagesOfChatFromDB(chat.id);
    }

    private void populateUI() {
        setTitle(partner.getName());
        setContentView(R.layout.activity_conversation);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buildDrawer();
    }
}
