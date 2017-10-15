package com.piddnbuddn.we.trustmessenger;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import backend.Const;
import backend.Controller;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.INTERNET;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (controller == null) {
            initController();
        }
        getController().getAccountInfo(this);
        mayRequestPermissions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        configureUI();
    }

    private void initController() {
        Controller.createInstance();
        controller = Controller.instance;
        model = controller.getModel();
        model.username = "Karl";
    }

    private boolean mayRequestPermissions() {
        boolean result = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_DENIED) {
            result = true;
        }
        return result;
    }

    private void requestPermission(int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return; }
        switch (requestCode) {
            case Const.REQUEST_INTERNET:
                ActivityCompat.requestPermissions(this, new String[]{INTERNET}, Const.REQUEST_INTERNET);
                break;
            default:
                break;
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return; }
        switch (requestCode) {
            case Const.REQUEST_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    requestPermission(Const.REQUEST_INTERNET);
                }
                break;
            default:
                break;
        }
    }

    private void configureUI() {
        Button delete = (Button) findViewById(R.id.login_button_delete);
        Button create = (Button) findViewById(R.id.login_button_create);
        if (model.username != null) {
            if (model.username != "") {
                TextView welcome = (TextView) findViewById(R.id.login_textview_welcome);
                welcome.setText(getString(R.string.welcome) + model.username);
                delete.setVisibility(View.VISIBLE);
                create.setText(getString(R.string.login_button));
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(OverviewActivity.class);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        } else {
            delete.setVisibility(View.GONE);
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(RegisterActivity.class);
                }
            });
        }
    }
}

