package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;

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

    }
}
