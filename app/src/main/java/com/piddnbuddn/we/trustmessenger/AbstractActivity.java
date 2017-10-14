package com.piddnbuddn.we.trustmessenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
        if (getController().checkPasswordUsage()) {
            showSetPasswordDialog();
        }
    }

    public Model getModel() {
        return model;
    }

    public Controller getController() {
        return controller;
    }

    protected void startThreadWithFinish(final int workID) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork(workID);
                afterWorkFinish(workID);
            }
        });
    }

    protected void startThreadWithoutFinish(final int workID) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork(workID);
            }
        });
    }

    protected void doWork(final int workID) {

    }

    protected void afterWorkFinish(final int workID) {

    }

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

    protected void startActivity(Class<? extends AbstractActivity> target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }

    protected void showSetPasswordDialog() {
        DialogInterface.OnClickListener accept = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        showCustomDialog(getString(R.string.dialog_title_password), getString(R.string.dialog_message_password), R.layout.set_password_dialog, accept, getDoNothingClickListener());
    }



    protected DialogInterface.OnClickListener getDoNothingClickListener() {
        DialogInterface.OnClickListener re = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        return re;
    }
}
