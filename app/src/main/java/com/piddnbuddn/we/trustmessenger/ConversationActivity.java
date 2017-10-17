package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;

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
        chat = model.curChat;
        partner = chat.partner;
    }

    @Override
    protected void workingThread() {
        loadMessages();
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    private void loadMessages() {
        messages = new ArrayList<>();
    }

    private void populateUI() {

    }
}
