package com.piddnbuddn.we.trustmessenger;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Util.FeedReaderBackgroundDBHelper;
import backend.Const;
import backend.Controller;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AbstractActivity {

    List<String> possibleUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onAppStartup();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void workingThread() {
        possibleUsers = getController().loadLocalNames(this);
    }

    @Override
    protected void endWorkingThread() {
        populateUI();
    }

    private void initController() {
        Controller.createInstance();
        controller = Controller.instance;
        model = controller.getModel();
    }

    private void onAppStartup() {
        if (controller != null) {
            return;
        }
        ProgressDialog dialog = getWaitDialog();
        dialog.show();
        initController();
        if (mayRequestPermissions()) {
            requestPermission(getNextRequestedPermission());
        }
        controller.setResources(getResources());
        dialog.dismiss();
    }

    private boolean mayRequestPermissions() {
        boolean result = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_DENIED) {
            result = true;
        }
        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            result = true;
        }
        return result;
    }

    private int getNextRequestedPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return 0;
        }
        if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_DENIED) {
            return Const.REQUEST_INTERNET;
        }
        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            return Const.REQUEST_WRITE_EXTERNAL_STORAGE;
        }
        return -1;
    }

    private void requestPermission(int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || requestCode == -1) { return; }
        switch (requestCode) {
            case Const.REQUEST_INTERNET:
                ActivityCompat.requestPermissions(this, new String[]{INTERNET}, Const.REQUEST_INTERNET);
                break;
            case Const.REQUEST_WRITE_EXTERNAL_STORAGE:
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, Const.REQUEST_WRITE_EXTERNAL_STORAGE);
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
                    if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermission(Const.REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    requestPermission(Const.REQUEST_INTERNET);
                }
                break;
            case Const.REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_DENIED) {
                        requestPermission(Const.REQUEST_INTERNET);
                    }
                } else {
                    requestPermission(Const.REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                break;
            default:
                break;
        }
    }

    private void doLogin() {
        getModel().DATABASE_NAME = getModel().username + Const.DATABASE_NAME_SUFFIX;
        FeedReaderBackgroundDBHelper dbHelper = new FeedReaderBackgroundDBHelper(this, getModel().DATABASE_NAME);
        dbHelper.openDataBase(getModel().DATABASE_NAME);
        controller.setDb(dbHelper.db);
        startActivity(OverviewActivity.class);
    }

    private void populateUI() {
        setContentView(R.layout.activity_login);
        TextView welcome = (TextView) findViewById(R.id.login_textview_welcome);
        Button delete = (Button) findViewById(R.id.login_button_delete);
        Button login = (Button) findViewById(R.id.login_button_login);
        Button create = (Button) findViewById(R.id.login_button_create);
        final Spinner choice = (Spinner) findViewById(R.id.selectionSpinner);

        if (possibleUsers.size() > 0) {
            welcome.setText(getText(R.string.prompt_register_name));
        }
        choice.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, possibleUsers));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUsername(choice.getSelectedItem().toString());
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.class);
            }
        });
    }

    private void deleteUsername(String name) {
        // TODO
        List<String> newList = new ArrayList<>();
        String write = "";
        if (possibleUsers.size() > 0) {
            for (String s : possibleUsers) {
                if (!s.equals(name)) {
                    write += s + "\n";
                    newList.add(s);
                }
            }
            if (write.length() >= 2) {
                write = write.substring(0, write.length() - 1);
            }
            controller.writeInternal(this, Const.FILENAME_USERNAMES, write);
        } else {
            controller.deleteInternal(this, Const.FILENAME_USERNAMES);
        }
        possibleUsers = newList;
        if (possibleUsers.size() == 0) {
            String[] empty = {""};
            ((Spinner) findViewById(R.id.selectionSpinner)).setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, empty));
        } else {
            ((Spinner) findViewById(R.id.selectionSpinner)).setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, possibleUsers));
        }
        controller.deleteKey(name, this);
    }
}

