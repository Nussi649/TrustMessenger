package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

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
        Button saveDB = (Button)findViewById(R.id.button_save_db);
        saveDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDatabase();
            }
        });
    }

    private void saveDatabase() {
        if (getController().copyDbToExternal()) {
            showToast(R.string.settings_save_db_success);
        } else {
            showToast(R.string.settings_save_db_error);
        }
    }
}
