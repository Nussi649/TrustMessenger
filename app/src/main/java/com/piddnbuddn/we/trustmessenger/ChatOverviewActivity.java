package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        TextView sdt_value = (TextView)findViewById(R.id.sdt_value);
        TextView toolbar_title = (TextView)findViewById(R.id.toolbar_title);

        sdt_value.setText(Util.secondsToString(chat.destructionTimer));
        toolbar_title.setText(getString(R.string.chat_overview_title_prefix) + " " + contact.getName());
        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().setCurContact(contact);
                startActivity(ContactActivity.class);
            }
        });
    }
}
