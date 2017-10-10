package com.piddnbuddn.we.trustmessenger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ich on 10.10.2017.
 */

public class RegisterActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        configureButtons();
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

    public void doRegister() {
        String name = ((EditText)findViewById(R.id.register_name_editText)).getText().toString();
        if (name.equals("")) {
            showToast(R.string.error_name_empty);
        } else {

        }
    }
}
