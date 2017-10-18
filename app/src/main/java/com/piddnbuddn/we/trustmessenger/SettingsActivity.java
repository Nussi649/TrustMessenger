package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Created by ich on 18.10.2017.
 */

public class SettingsActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void workingThread() {
        loadSettings();
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    private void loadSettings() {

    }

    private void populateUI() {
        setContentView(R.layout.activity_settings);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.activity_settings);
        buildDrawer();
    }
}
