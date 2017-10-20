package com.piddnbuddn.we.trustmessenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import backend.Model;
import backend.Controller;

/**
 * Created by ich on 07.10.2017.
 */

public abstract class AbstractActivity extends AppCompatActivity {
    Controller controller = Controller.instance;
    Model model;
    ActionBarDrawerToggle drawerToggle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (model == null) {
            model = getModel();
        }
        startWorkingThread();
        if (getController().checkPasswordUsage()) {
            showSetPasswordDialog();
        }
    }

    public Model getModel() {
        return controller.getModel();
    }

    public Controller getController() {
        return controller;
    }

    protected void doWork(final int workID) {

    }

    protected void afterWorkFinish(final int workID) {

    }

    //region Thread starting Funtions
    protected void startThreadWithFinish(final int workID) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork(workID);
                afterWorkFinish(workID);
            }
        });
        thread.start();
    }

    protected void startThreadWithoutFinish(final int workID) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork(workID);
            }
        });
        thread.start();
    }

    private void startWorkingThread() {
        Thread workingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                ProgressDialog progress = getWaitDialog();
                progress.show();
                workingThread();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endWorkingThread();
                    }
                });
                progress.dismiss();
            }
        });
        workingThread.start();
    }

    protected void workingThread() {

    }

    // Runs on UI-Thread
    protected void endWorkingThread() {

    }

    protected void startActivity(Class<? extends AbstractActivity> target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }
    //endregion

    //region Toast Functions
    protected void showToast(int resId) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void showToast(String text) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void showToastLong(int resId) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        toast.show();
    }

    protected void showToastLong(String text) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }
    //endregion

    //region Dialog Functions
    protected void showDialog(String title, String message, DialogInterface.OnClickListener acceptListener, DialogInterface.OnClickListener declineListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton(R.string.dialog_accept, acceptListener);
        builder.setNeutralButton(R.string.dialog_cancel, cancelListener);
        builder.setNegativeButton(R.string.dialog_decline, declineListener);
        builder.create().show();
    }

    protected void showDialog(String title, String message, DialogInterface.OnClickListener acceptListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton(R.string.dialog_accept, acceptListener);
        builder.setNegativeButton(R.string.dialog_cancel, cancelListener);
        builder.create().show();
    }

    protected void showDialog(String title, String message, DialogInterface.OnClickListener acceptListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton(R.string.dialog_accept, acceptListener);
        builder.create().show();
    }

    protected void showCustomDialog(String title, String message, int layoutID, DialogInterface.OnClickListener acceptListener, DialogInterface.OnClickListener declineListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.dialog_accept, acceptListener);
        builder.setNeutralButton(R.string.dialog_cancel, cancelListener);
        builder.setNegativeButton(R.string.dialog_decline, declineListener);
        builder.setView(layoutID);
        builder.create().show();
    }

    protected void showCustomDialog(String title, String message, int layoutID, DialogInterface.OnClickListener acceptListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.dialog_accept, acceptListener);
        builder.setNegativeButton(R.string.dialog_cancel, cancelListener);
        builder.setView(layoutID);
        builder.create().show();
    }

    protected void showCustomDialog(String title, String message, int layoutID, DialogInterface.OnClickListener acceptListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.dialog_accept, acceptListener);
        builder.setView(layoutID);
        builder.create().show();
    }
    //endregion

    protected void buildDrawer() {
        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (drawerLayout != null) {
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_open, R.string.navigation_close) {
                @Override
                public void onDrawerClosed(final View view) {
                    super.onDrawerClosed(view);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    invalidateOptionsMenu();
                }

                @Override
                public void onDrawerOpened(final View drawerView) {
                    super.onDrawerOpened(drawerView);
                    drawerLayout.openDrawer(Gravity.LEFT);
                    invalidateOptionsMenu();
                }
            };
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.addDrawerListener(drawerToggle);

            // set Click Listeners
            TextView newConversation = (TextView)findViewById(R.id.navigation_new_conversation);
            TextView newGroup = (TextView)findViewById(R.id.navigation_new_group);
            TextView newContact = (TextView)findViewById(R.id.navigation_new_contact);
            TextView contactList = (TextView)findViewById(R.id.navigation_contact_list);
            TextView settings = (TextView)findViewById(R.id.navigation_settings);

            contactList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ContactListActivity.class);
                }
            });
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(SettingsActivity.class);
                }
            });
            newConversation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToastLong(R.string.functionality_not_implemented);
                }
            });
            newGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToastLong(R.string.functionality_not_implemented);
                }
            });
            newContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToastLong(R.string.functionality_not_implemented);
                }
            });
        }
    }

    protected void showSetPasswordDialog() {
        DialogInterface.OnClickListener accept = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText pw = (EditText) ((AlertDialog)dialog).findViewById(R.id.setPassword_editText);
                setPassword(pw.getText().toString());
                dialog.dismiss();
            }
        };
        showCustomDialog(getString(R.string.dialog_title_password), getString(R.string.dialog_message_password), R.layout.dialog_set_password, accept, getDoNothingClickListener());
    }

    private void setPassword(String newPW) {
        if (getController().setPassword(newPW, this)) {
            showToastLong(R.string.toast_password_set_success);
        } else {
            showToastLong(R.string.toast_password_set_fail);
        }

    }


    protected DialogInterface.OnClickListener getDoNothingClickListener() {
        DialogInterface.OnClickListener re = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        return re;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected ProgressDialog getWaitDialog() {
        ProgressDialog re = new ProgressDialog(this);
        re.setTitle(R.string.loadingscreen_title);
        re.setMessage(getString(R.string.loadingscreen_body));
        re.setCancelable(false);
        return re;
    }
}
