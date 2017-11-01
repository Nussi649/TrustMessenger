package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

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

    private void populateUI() {
        this.setContentView(R.layout.activity_contact);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        setTitle(contact.getName());
        buildDrawer();
    }
}
