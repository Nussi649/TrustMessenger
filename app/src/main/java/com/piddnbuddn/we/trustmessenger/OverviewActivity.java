package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import backend.ChatSegment;
import backend.be.ChatBE;
import backend.be.ContactBE;
import backend.be.IncMessageBE;
import backend.be.MessageBE;

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
    protected void onResume() {
        super.onResume();
        reloadChatsUI();
    }

    @Override
    protected void workingThread() {
        getData();
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_overview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_conversation:
                startActivity(NewConversationActivity.class);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        controller.loadContactList();
        controller.loadChatList();
        chats = model.chats;
    }

    private void reloadChatsUI() {
        mainContainer = (LinearLayout)findViewById(R.id.main_content);
        if (chats != null) {
            mainContainer.removeAllViews();
            for (int i = 0; i < chats.size(); i++) {
                final ChatBE finalChat = chats.get(chats.size()-(i+1));
                ChatSegment chatSegment = new ChatSegment(this, finalChat, controller.loadFirstMessageOfChatFromDB(finalChat.id));
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
    }

    private void populateUI() {
        setTitle(R.string.activity_overview);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        reloadChatsUI();
        buildDrawer();
    }

    private MessageBE getAnyMessage() {
        IncMessageBE re = new IncMessageBE("some content", new Date(2017,10,21,01,36,06), controller.getContactByName(getString(R.string.buddy_2)));
        return re;
    }
}
