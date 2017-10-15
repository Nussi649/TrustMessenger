package com.piddnbuddn.we.trustmessenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import backend.Model;
import backend.Controller;

/**
 * Created by ich on 07.10.2017.
 */

public abstract class AbstractActivity extends Activity {
    Controller controller = Controller.instance;
    Model model;

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
    }

    private void startWorkingThread() {
        Thread workingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProgressDialog progress = getWaitDialog();
                progress.show();
                workingThread();
                endWorkingThread();
                progress.dismiss();
            }
        });
    }

    protected void workingThread() {

    }

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

    protected void showSetPasswordDialog() {
        DialogInterface.OnClickListener accept = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText pw = (EditText) ((AlertDialog)dialog).findViewById(R.id.setPassword_editText);
                setPassword(pw.getText().toString());
                dialog.dismiss();
            }
        };
        showCustomDialog(getString(R.string.dialog_title_password), getString(R.string.dialog_message_password), R.layout.set_password_dialog, accept, getDoNothingClickListener());
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

    private ProgressDialog getWaitDialog() {
        ProgressDialog re = new ProgressDialog(this);
        re.setTitle(R.string.loadingscreen_title);
        re.setMessage(getString(R.string.loadingscreen_body));
        re.setCancelable(false);
        return re;
    }
}
