package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import backend.be.ChatBE;
import backend.be.ContactBE;

/**
 * Created by ich on 17.10.2017.
 */

public class ContactActivity extends AbstractActivity {

    ContactBE contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void workingThread() {
        contact = getModel().curContact;
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_conversation:
                openConversation();
                return true;
            case R.id.push:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateUI() {
        this.setContentView(R.layout.activity_contact);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        setTitle(contact.getName());
        buildDrawer();
    }

    private void openConversation() {
        ChatBE chat = getController().saveNewChatToDB(contact);
        if (chat == null) {
            return;
        }
        getController().setCurContact(contact);
        getController().setCurChat(chat);
        startActivity(ConversationActivity.class);
    }
}
