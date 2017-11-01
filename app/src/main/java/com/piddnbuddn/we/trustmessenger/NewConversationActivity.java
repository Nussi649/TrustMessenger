package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import backend.CListSegment;
import backend.be.ContactBE;

/**
 * Created by ich on 01.11.2017.
 */

public class NewConversationActivity extends AbstractActivity {
    List<ContactBE> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void workingThread() {
        contacts = getModel().contacts;
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    private void populateUI() {
        setContentView(R.layout.activity_contact_list);
        setTitle(R.string.new_conversation_title);
        setSupportActionBar((Toolbar)findViewById(R.id.drawer_layout).findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayout mainContent = (LinearLayout)findViewById(R.id.main_content);
        for (ContactBE contact : contacts) {
            final ContactBE finalContact = contact;
            CListSegment segment = new CListSegment(this, finalContact);
            segment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().setCurContact(finalContact);
                    getController().setCurChat(getController().saveNewChatToDB(finalContact));
                    startActivity(ConversationActivity.class);
                }
            });
            mainContent.addView(segment);
        }
        buildDrawer();
    }
}
