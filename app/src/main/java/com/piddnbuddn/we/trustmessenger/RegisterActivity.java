package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import backend.Const;
import backend.PrivateKey;
import backend.PublicKey;

/**
 * Created by ich on 10.10.2017.
 */

public class RegisterActivity extends AbstractActivity {

    private static final int GENERATE_KEY = 1;
    private static final int SEND_TO_SERVER = 2;
    private static final int UPDATE_USER_STORAGE = 3;

    private PrivateKey newPrivKey;
    private PublicKey newPubKey;
    private String username;
    private String signedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.activity_register);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configureButtons();
    }

    @Override
    protected void doWork(final int workID) {
        switch (workID) {
            case GENERATE_KEY:
                newPrivKey = PrivateKey.generateRandomKey();
                newPubKey = newPrivKey.publicKey;
                signedUsername = newPrivKey.sign(username);
                break;
            case SEND_TO_SERVER:
                sendToServer(newPubKey, signedUsername);
                break;
            case UPDATE_USER_STORAGE:
                getModel().username = username;
                List<String> users = getController().loadLocalNames(this);
                if (users.get(0).equals("")) {
                    users.clear();
                }
                users.add(username);
                String write = users.get(0);
                int index = 1;
                while (users.size() > index) {
                    write += "\n" + users.get(index);
                    index++;
                }
                getController().writeInternal(this, Const.FILENAME_USERNAMES, write);
                break;
            default:
                break;
        }
    }

    @Override
    protected void afterWorkFinish(final int workID) {
        switch (workID) {
            case GENERATE_KEY:
                startThreadWithFinish(SEND_TO_SERVER);
                break;
            case SEND_TO_SERVER:
                getModel().username = username;
                getModel().privateKey = newPrivKey;
                getModel().publicKey = newPubKey;
                startThreadWithFinish(UPDATE_USER_STORAGE);
                break;
            case UPDATE_USER_STORAGE:
                startActivity(LoginActivity.class);
                break;
            default:
                break;
        }
    }

    private void configureButtons() {
        Button acceptButton = (Button) findViewById(R.id.register_button);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
        buildDrawer();
    }

    private void sendToServer(PublicKey publicKey, String signedName) {
        controller.setUserServer(publicKey, signedName);
    }

    public void doRegister() {
        String name = ((EditText)findViewById(R.id.register_name_editText)).getText().toString();
        if (name.equals("")) {
            showToast(R.string.error_name_empty);
        } else {
            username = name;
            startThreadWithFinish(UPDATE_USER_STORAGE);
        }
    }
}
