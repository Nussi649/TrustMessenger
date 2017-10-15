package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import backend.Const;
import backend.PrivateKey;
import backend.PublicKey;

/**
 * Created by ich on 10.10.2017.
 */

public class RegisterActivity extends AbstractActivity {

    private static final int GENERATE_KEY = 1;
    private static final int SEND_TO_SERVER = 2;
    private static final int TEST = 3;

    private PrivateKey newPrivKey;
    private PublicKey newPubKey;
    private String username;
    private String signedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        configureButtons();
    }

    @Override
    protected void doWork(final int workID) {
        switch (workID) {
            case GENERATE_KEY:
                newPrivKey = PrivateKey.generateRandomKey();
                newPubKey = PublicKey.calculateFromPrivateKey(newPrivKey);
                signedUsername = newPrivKey.sign(username);
                break;
            case SEND_TO_SERVER:
                sendToServer(newPubKey, signedUsername);
                break;
            case TEST:
                getModel().username = username;
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
                startActivity(OverviewActivity.class);
                break;
            case TEST:
                startActivity(LoginActivity.class);
                break;
            default:
                break;
        }
    }

    private void configureButtons() {
        Button acceptButton = (Button) findViewById(R.id.register_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
    }

    private void sendToServer(PublicKey publicKey, String signedName) {

    }

    public void doRegister() {
        String name = ((EditText)findViewById(R.id.register_name_editText)).getText().toString();
        if (name.equals("")) {
            showToast(R.string.error_name_empty);
        } else {
            username = name;
            startThreadWithFinish(TEST);
        }
    }
}
