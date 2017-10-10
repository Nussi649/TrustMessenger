package com.piddnbuddn.we.trustmessenger;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import backend.Model;
import backend.Controller;

/**
 * Created by ich on 07.10.2017.
 */

public abstract class AbstractActivity extends Activity {
    Model model;
    Controller controller;

    public Model getModel() {
        return model;
    }

    public Controller getController() {
        return controller;
    }

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
}
