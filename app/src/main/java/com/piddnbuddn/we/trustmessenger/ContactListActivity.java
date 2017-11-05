package com.piddnbuddn.we.trustmessenger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import backend.CListSegment;
import backend.be.ContactBE;

/**
 * Created by ich on 17.10.2017.
 */

public class ContactListActivity extends AbstractActivity {
    List<ContactBE> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void workingThread() {
        if (contacts == null) {
            if (getModel().contacts == null) {
                getController().loadContactList();
            }
            contacts = getModel().contacts;
        }
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_menu_add:
                showFindUserDialog();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateUI() {
        setContentView(R.layout.activity_contact_list);
        setTitle(R.string.activity_contact_list);
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
                    startActivity(ContactActivity.class);
                }
            });
            mainContent.addView(segment);
        }
        buildDrawer();
    }
}
