package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.List;

import backend.be.ChatBE;

/**
 * Created by ich on 14.10.2017.
 */

public class OverviewActivity extends AbstractActivity {

    LinearLayout mainContainer;
    List<ChatBE> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_layout);
    }

    @Override
    protected void workingThread() {
        getData();
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    private void getData() {

    }

    private void populateUI() {
        mainContainer = (LinearLayout)findViewById(R.id.mainContent);

    }
}
