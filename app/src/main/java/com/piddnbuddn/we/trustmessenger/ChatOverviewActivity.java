package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.widget.TextView;

import Util.Util;
import backend.be.ChatBE;
import backend.be.ContactBE;

/**
 * Created by ich on 01.11.2017.
 */

public class ChatOverviewActivity extends AbstractActivity {
    ChatBE chat;
    ContactBE contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void workingThread() {
        chat = getModel().curChat;
        contact = chat.partner;
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    private void populateUI() {
        setContentView(R.layout.activity_chat_overview);

        TextView sdt_value = (TextView)findViewById(R.id.sdt_value);

         sdt_value.setText(Util.secondsToString(chat.destructionTimer));
    }
}
