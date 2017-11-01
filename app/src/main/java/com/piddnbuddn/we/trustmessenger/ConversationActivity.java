package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import backend.MessageSegment;
import backend.be.ChatBE;
import backend.be.ContactBE;
import backend.be.MessageBE;
import backend.be.OutMessageBE;

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

    @Override
    public void onBackPressed() {
        startActivity(OverviewActivity.class);
    }

    private void loadMessages() {
        messages = getController().loadMessagesOfChatFromDB(chat.id);
        if (messages == null) {
            messages = new ArrayList<>();
        }
    }

    private void populateUI() {
        setTitle(partner.getName());
        setContentView(R.layout.activity_conversation);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayout container = (LinearLayout)findViewById(R.id.messages_container);
        for (MessageBE msg : messages) {
            container.addView(new MessageSegment(this, msg));
        }
        Button send = (Button)findViewById(R.id.conversation_button_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }
        });
        buildDrawer();
    }

    private void reloadMessageUI() {
        LinearLayout container = (LinearLayout)findViewById(R.id.messages_container);
        container.removeAllViews();
        for (MessageBE msg : messages) {
            container.addView(new MessageSegment(this, msg));
        }
    }

    public void sendMessage() {
        TextView msgText = (TextView)findViewById(R.id.editText);
        String msg = msgText.getText().toString();
        if (msg.equals("")) {
            return;
        }
        MessageBE msgBE = new OutMessageBE(msg, new Date(), partner);
        messages.add(msgBE);
        controller.sendMessage(msgBE);
        controller.saveMessageOfChat(msgBE, chat);
        reloadMessageUI();
        msgText.setText("");
    }
}
